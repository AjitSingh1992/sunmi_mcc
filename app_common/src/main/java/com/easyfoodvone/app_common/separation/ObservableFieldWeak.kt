package com.easyfoodvone.app_common.separation

import androidx.databinding.BaseObservable
import java.lang.ref.WeakReference

/**
 * Class to help working with DataBinding and view reuse when using RecyclerViews and Adapters.
 *
 * Use ObservableFieldWeak when the view holds a reference to a controller or vice-versa to easily prevent
 * a memory leak of these parent classes.
 *
 * Normally you can just call .unbind() from onDestroyView, however there is no such onDestroyView
 * for RecyclerView adapters.
 * Any time you aren't able to call .unbind() on the inflated DataBinding object, then it is potentially
 * leaking something, most often a listener/observer inner class, which leaks the parent class also.
 *
 * Also note that when reusing views, to prevent issues animating them in future, it is wise to not
 * be changing the data which is bound to the view.
 */
open class ObservableFieldWeak<T : Any?>(initial: T) : BaseObservable() {

    // Note that this class won't notify the BaseObservable if the underlying WeakReference is nulled
    private var valReference: WeakReference<T>? = initial?.let { WeakReference(it) }

    fun get(): T {
        // We need to be sneaky to avoid using the !! operator in case T specified itself as a nullable type
        val t: T? = valReference?.get()
        return t as T
    }

    fun set(value: T) {
        if (value != valReference?.get()) {
            valReference = WeakReference(value)
            notifyChange()
        }
    }
}
