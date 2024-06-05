# hm-database

Fellesbibliotek for databaseintegrasjon i Team Hjelpemiddelbehov.

WIP

## Kom i gang

### JsonMapper

### Lage DataSource

### Kjør Flyway

### Transaction

```kotlin
val dataSource = createDataSource {
    envVarPrefix = "DB"
}

val result: Map<String, Any?> = transaction(dataSource) {
    it.update("INSERT INTO table_v1 (name) VALUES (:name)", mapOf("name" to "test"))
    it.single("SELECT * FROM table_v1 WHERE id = 1") { row: Row -> row.toMap() }
}
```
