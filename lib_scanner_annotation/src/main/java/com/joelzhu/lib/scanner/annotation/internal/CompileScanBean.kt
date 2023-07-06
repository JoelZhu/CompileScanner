package com.joelzhu.lib.scanner.annotation.internal

/**
 * Bean for storing information annotated in [CompileScan][com.joelzhu.lib.scanner.annotation.CompileScan]
 *
 * @author JoelZhu
 * @since 2023-04-12
 */
data class CompileScanBean(
    val className: String,
    val category: Int = BeanConstants.DEFAULT_CATEGORY,
    val isDefault: Boolean = BeanConstants.DEFAULT_IS_DEFAULT,
    val priority: Int = BeanConstants.DEFAULT_PRIORITY,
    val extensionClassName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompileScanBean

        if (className != other.className) return false
        if (category != other.category) return false
        if (isDefault != other.isDefault) return false
        if (priority != other.priority) return false
        if (extensionClassName != other.extensionClassName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = className.hashCode()
        result = 31 * result + category
        result = 31 * result + isDefault.hashCode()
        result = 31 * result + priority
        result = 31 * result + (extensionClassName?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CompileScan[ " +
                "class: $className, " +
                category.let { "category: $category, " } +
                (isDefault.let { "isDefault: $isDefault, " } ?: run { "" }) +
                (priority.let { "priority: $priority, " } ?: run { "" }) +
                (extensionClassName?.let { "extensionClass: $extensionClassName " } ?: run { "" }) +
                "]"
    }
}