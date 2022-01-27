package com.easyfoodvone.app_common.separation

import androidx.databinding.ObservableField

/**
 * The androidx.databinding.ObservableField class is extremely easy to use with DataBinding, but it
 * is a Java class, so has one problem when accessed from Kotlin - annoying null-safety.
 *
 * Use this Kotlin version to prevent frequently using .get()!! when accessing non-null fields.
 */
open class ObservableField<T: Any?>(initial: T) : ObservableField<T>(initial) {

    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        // We need to be sneaky to avoid using the !! operator in case T specified itself as a nullable type
        val t: T? = super.get()
        return t as T
    }

    override fun set(value: T) {
        super.set(value)
    }
}
