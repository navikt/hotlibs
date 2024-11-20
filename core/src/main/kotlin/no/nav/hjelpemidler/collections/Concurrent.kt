package no.nav.hjelpemidler.collections

import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentMap

fun <T> queueOf(vararg elements: T): Queue<T> = ConcurrentLinkedQueue(elements.toList())

fun <K, V> concurrentMapOf(vararg pairs: Pair<K, V>): ConcurrentMap<K, V> = ConcurrentHashMap(pairs.toMap())
