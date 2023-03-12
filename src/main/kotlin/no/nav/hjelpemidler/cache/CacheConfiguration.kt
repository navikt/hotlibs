package no.nav.hjelpemidler.cache

import kotlin.time.Duration

open class CacheConfiguration internal constructor() {
    var initialCapacity: Int? = null
    var maximumSize: Long? = null
    var maximumWeight: Long? = null
    var weakKeys: Boolean = false
    var weakValues: Boolean = false
    var softValues: Boolean = false
    var expireAfterWrite: Duration? = null
    var expireAfterAccess: Duration? = null
    var refreshAfterWrite: Duration? = null
    var recordStats: Boolean = false

    override fun toString(): String {
        return "CacheConfiguration(initialCapacity=$initialCapacity, maximumSize=$maximumSize, maximumWeight=$maximumWeight, weakKeys=$weakKeys, weakValues=$weakValues, softValues=$softValues, expireAfterWrite=$expireAfterWrite, expireAfterAccess=$expireAfterAccess, refreshAfterWrite=$refreshAfterWrite, recordStats=$recordStats)"
    }
}
