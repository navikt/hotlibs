package no.nav.hjelpemidler.domain.id

abstract class StringId(value: String) : Id<String>(value), CharSequence {
    override val length: Int get() = value.length
    override fun get(index: Int): Char = value[index]
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)
}
