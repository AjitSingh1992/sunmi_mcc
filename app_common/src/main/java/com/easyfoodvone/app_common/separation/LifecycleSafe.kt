package com.easyfoodvone.app_common.separation

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

class LifecycleSafe {
    private val lifecycle: Lifecycle

    /** Create with a fragment VIEW LIFECYCLE to prevent mistakes using the wrong lifecycle */
    constructor(fragment: Fragment) : this(fragment.viewLifecycleOwner.lifecycle)

    /** Create with an activity to prevent mistakes using the wrong lifecycle */
    constructor(activity: AppCompatActivity) : this(activity.lifecycle)

    /**
     * DialogFragments usually have null getView(), so do not have Fragment's viewLifecycleOwner,
     * unless onCreateView is specifically implemented
     */
    constructor(dialogFragment: DialogFragment) : this(dialogFragment.lifecycle)

    /** UNSAFE - Only use this if you have a weird Lifecycle, not the usual Fragment/Activity one */
    constructor(rawUnsafe: Lifecycle) {
        this.lifecycle = rawUnsafe
    }

    /**
     * When fragment.onDestroyView / activity.onDestroy is called the given binding variable will
     * have unbind() called, and will be set to null
     */
    fun <T : ViewDataBinding> unbindOnDestroy(varField: KMutableProperty0<T?>) {
        actionOnceOnDestroy {
            varField.get()?.unbind()
            varField.set(null)
        }
    }

    /**
     * When fragment.onDestroyView / activity.onDestroy is called the given binding variable will
     * have unbind() called
     */
    fun <T : ViewDataBinding> unbindOnDestroy(valField: KProperty0<T>) {
        actionOnceOnDestroy { valField.get().unbind() }
    }

    /**
     * When fragment.onDestroyView / activity.onDestroy is called the given binding variable will
     * have unbind() called, and will be set to null
     */
    fun <T : Any> nullOnDestroy(varField: KMutableProperty0<T?>) {
        actionOnceOnDestroy { varField.set(null) }
    }

    /**
     * Sets now, and when fragment.onDestroyView / activity.onDestroy is called will nullify the
     * contents of a Kotlin var field
     */
    fun <T> setValueUntilDestroy(field: KMutableProperty0<T?>, setNowContents: T) {
        field.set(setNowContents)

        actionOnceOnDestroy { field.set(null) }
    }

    /**
     * Sets now, and when fragment.onDestroyView / activity.onDestroy is called will nullify the
     * contents of an ObservableField
     */
    fun <T> setValueUntilDestroy(field: ObservableField<T?>, setNowContents: T) {
        field.set(setNowContents)

        actionOnceOnDestroy { field.set(null) }
    }

    /**
     * Observes now, and when fragment.onDestroyView / activity.onDestroy is called will remove the observer
     *
     * Also provides a typed observable for cleaner fields change listeners
     *
     * You can call ObservableField.removeObserver(new TypedObserver(observer)) to remove it early without issue.
     */
    fun <T> addObserverOnceUntilDestroy(field: ObservableField<T>, observer: Observer<T>, callNow: Boolean) {
        val untypedObserver: Observable.OnPropertyChangedCallback = ObservableUtils.TypedObserver(field, observer)

        field.addOnPropertyChangedCallback(untypedObserver)
        actionOnceOnDestroy { field.removeOnPropertyChangedCallback(untypedObserver) }

        if (callNow) {
            observer.onChanged(field.get())
        }
    }

    /**
     * Observes now, and when fragment.onDestroyView / activity.onDestroy is called will remove the observer
     */
    fun <T> addObserverOnceUntilDestroy(
            list: ObservableList<T>,
            observer: ObservableList.OnListChangedCallback<ObservableList<T>>,
            callNow: Boolean) {

        list.addOnListChangedCallback(observer)
        actionOnceOnDestroy { list.removeOnListChangedCallback(observer) }

        if (callNow) {
            observer.onChanged(list)
        }
    }

    /**
     * Observes now, changes are notified to the adapter, and when fragment.onDestroyView /
     * activity.onDestroy is called will remove the observer
     */
    fun <T> addObserverOnceUntilDestroy(list: ObservableList<T>, adapter: RecyclerView.Adapter<*>, callNow: Boolean) {
        val observer: ObservableList.OnListChangedCallback<ObservableList<T>> =
            ObservableUtils.newAdapterListenerForObservableList(adapter)

        addObserverOnceUntilDestroy(list, observer, callNow)
    }

    /**
     * Adds an observer to a lifecycle object, but cleanly removes it once onDestroy is called.
     * You can still use Lifecycle.removeObserver() with the given observer to remove it early without issue.
     */
    fun addObserverOnceUntilDestroy(observer: DefaultLifecycleObserver) {
        val removingObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                lifecycle.removeObserver(observer)
                lifecycle.removeObserver(this)
            }
        }

        // Register the removingObserver first, as events are dispatched in reverse order.
        // That way observer will also get a call to onDestroy.
        lifecycle.addObserver(removingObserver)
        lifecycle.addObserver(observer)
    }

    /**
     * When fragment.onDestroyView / activity.onDestroy is called the given action is invoked
     */
    fun actionOnceOnDestroy(action: () -> Unit) {
        addObserverOnceUntilDestroy(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                action.invoke()
            }
        })
    }
}