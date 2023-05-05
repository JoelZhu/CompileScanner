package com.joelzhu.lib.scanner.annotation

/**
 * Key for scanner to compare, and stored in memory.
 *
 * @author JoelZhu
 * @since 2023-04-12
 */
data class ScannedClass(
    val clazz: Class<*>,
    val tag: String,
    val group: String? = null,
    val isDefault: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ScannedClass) {
            return false
        }

        return clazz == other.clazz && tag == other.tag && group == other.group
    }

    override fun hashCode(): Int {
        return clazz.hashCode() + (tag.hashCode() * 31 + group.hashCode()) * 31
    }

    override fun toString(): String {
        return "[ class: ${clazz.name}, tag: $tag, group: $group ]."
    }
}