# Database

Fellesbibliotek for databaseintegrasjon i DigiHoT

## Kom i gang

PostgreSQL:

```kotlin
dependencies {
    // Database
    implementation(libs.hm.database) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:database-postgresql")
        }
    }

    // Testing
    testImplementation(libs.hm.database) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:database-testcontainers")
        }
    }
}
```

Oracle:

```kotlin
dependencies {
    // Database
    implementation(libs.hm.database) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:database-oracle")
        }
    }

    // Testing
    testImplementation(libs.hm.database) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:database-h2")
        }
    }
}
```

### JsonMapper

Man kan definere sin egen JsonMapper for bruk i JSON-støtten til PostgreSQL. Dette gjøres via
en java.util.ServiceLoader.

Opprett følgende fil:

```text
src/main/resources/META-INF/services/no.nav.hjelpemidler.serialization.jackson.JacksonObjectMapperProvider
```

Filen skal inneholde fully qualified name for en klasse som implementerer interfacet
no.nav.hjelpemidler.serialization.jackson.JacksonObjectMapperProvider, e.g.:

```text
no.nav.hjelpemidler.app.MyJacksonObjectMapperProviderProxy
```

Eksempel på implementasjon:

```kotlin
object MyJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    private val objectMapper: ObjectMapper = defaultJsonMapper() // tilpass ObjectMapper her
    override fun invoke(): ObjectMapper = objectMapper
}

@LoadOrder(0)
class MyJacksonObjectMapperProviderProxy :
    JacksonObjectMapperProvider by DefaultJacksonObjectMapperProvider
```

### Lage DataSource

```kotlin
// PostgreSQL
val dataSource = createDataSource(PostgreSQL) {
    envVarPrefix = "DB"
}

// Oracle
val dataSource = createDataSource(Oracle) {
    jdbcUrl = Configuration.DB_JDBC_URL
    username = Configuration.DB_USERNAME
    password = Configuration.DB_PASSWORD
    databaseName = Configuration.DB_NAME
}

// Testcontainers (PostgreSQL)
val dataSource = createDataSource(Testcontainers) {
    tag = "15-alpine"
}

// H2 (Oracle)
val dataSource = createDataSource(H2) {
    mode = H2Mode.Oracle
}
```

### Kjør Flyway

For å kjøre Flyway må følgende legges til i build.gradle.kts.

```kotlin
tasks.shadowJar {
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
```

### Transaction

```kotlin
val result: Map<String, Any?> = transaction(dataSource) {
    it.update("INSERT INTO table_v1 (name) VALUES (:name)", mapOf("name" to "test"))
    it.single("SELECT * FROM table_v1 WHERE id = 1") { row: Row -> row.toMap() }
}
```
