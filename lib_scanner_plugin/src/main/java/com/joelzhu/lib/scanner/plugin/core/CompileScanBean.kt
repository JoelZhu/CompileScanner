package com.joelzhu.lib.scanner.plugin.core

/**
 * Bean for storing information annotated in [CompileScan][com.joelzhu.lib.scanner.annotation.CompileScan]
 *
 * @author JoelZhu
 * @since 2023-04-12
 */
data class CompileScanBean(
    var belongingClass: String,

    var tag: String? = null,

    var group: String? = null,

    var isDefault: Boolean = false,

    var priority: Int? = null
) {
    override fun toString(): String {
        return "CompileScan [ class: $belongingClass, tag: $tag, group: $group, priority: $priority, default: $isDefault ]"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CompileScanBean) {
            return false
        }

        return this.belongingClass == other.belongingClass && this.tag == other.tag &&
                this.group == other.group && this.isDefault == other.isDefault &&
                this.priority == other.priority
    }

    override fun hashCode(): Int {
        return (((belongingClass.hashCode() * 31 +
                tag.hashCode()) * 31 +
                group.hashCode()) * 31 +
                isDefault.hashCode()) * 31 +
                priority.hashCode()
    }
}