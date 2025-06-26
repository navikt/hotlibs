package no.nav.hjelpemidler.database.repository

interface Repository<T : Any, ID : Any> : ReadOperations<T, ID>, WriteOperations<T, ID>
