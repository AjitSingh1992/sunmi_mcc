package com.easyfoodvone.app_common.separation

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.easyfoodvone.app_common.separation.ObservableUtils.Companion.newAdapterListenerForObservableList
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0


/**
 * Sets now, and when activity onDestroy is called will nullify the contents of an ObservableField
 */
fun <T> ObservableField<T?>.setUntilViewDestroy(setNowContents: T, activity: AppCompatActivity) {
    setUntilViewDestroy(this@setUntilViewDestroy, setNowContents, activity.lifecycle)
}

/**
 * Sets now, and when fragment onDestroyView is called will nullify the contents of an ObservableField
 */
fun <T> ObservableField<T?>.setUntilViewDestroy(setNowContents: T, fragment: Fragment) {
    setUntilViewDestroy(this@setUntilViewDestroy, setNowContents, fragment.viewLifecycleOwner.lifecycle)
}

/**
 * When activity onDestroy is called the given binding variable will have unbind() called, and will be set to null
 */
fun <T : ViewDataBinding> KMutableProperty0<T?>.unbindOnDestroy(activity: AppCompatActivity) {
    actionOnDestroy(activity.lifecycle, {
        this@unbindOnDestroy.get()?.unbind()
        this@unbindOnDestroy.set(null)
    })
}

/**
 * When fragment onDestroyView is called the given binding variable will have unbind() called, and will be set to null
 */
fun <T : ViewDataBinding> KMutableProperty0<T?>.unbindOnDestroy(fragment: Fragment) {
    actionOnDestroy(fragment.viewLifecycleOwner.lifecycle, {
        this@unbindOnDestroy.get()?.unbind()
        this@unbindOnDestroy.set(null)
    })
}

/**
 * When activity onDestroy is called the given binding variable will have unbind() called
 */
fun <T : ViewDataBinding> KProperty0<T>.unbindOnDestroy(activity: AppCompatActivity) {
    actionOnDestroy(activity.lifecycle, {
        this@unbindOnDestroy.get().unbind()
    })
}

/**
 * When fragment onDestroyView is called the given binding variable will have unbind() called
 */
fun <T : ViewDataBinding> KProperty0<T>.unbindOnDestroy(fragment: Fragment) {
    actionOnDestroy(fragment.viewLifecycleOwner.lifecycle, {
        this@unbindOnDestroy.get().unbind()
    })
}

/**
 * When activity onDestroy is called the given binding variable will have unbind() called, and will be set to null
 */
fun <T : Any> KMutableProperty0<T?>.nullOnDestroy(activity: AppCompatActivity) {
    actionOnDestroy(activity.lifecycle, {
        this@nullOnDestroy.set(null)
    })
}

/**
 * When fragment onDestroyView is called the given binding variable will have unbind() called, and will be set to null
 */
fun <T : Any> KMutableProperty0<T?>.nullOnDestroy(fragment: Fragment) {
    actionOnDestroy(fragment.viewLifecycleOwner.lifecycle, {
        this@nullOnDestroy.set(null)
    })
}

/**
 * Observes now, and when activity onDestroy is called will remove the observer
 *
 * Also provides a typed observable for cleaner fields change listeners
 */
fun <T> ObservableField<T>.addObserverOnceUntilDestroy(observer: Observer<T>, activity: AppCompatActivity, callNow: Boolean) {
    val untypedObserver: Observable.OnPropertyChangedCallback = ObservableUtils.TypedObserver(this@addObserverOnceUntilDestroy, observer)
    observeUntilViewDestroy(this@addObserverOnceUntilDestroy, untypedObserver, activity.lifecycle)
    if (callNow) {
        observer.onChanged(this@addObserverOnceUntilDestroy.get())
    }
}

/**
 * Observes now, and when fragment onDestroyView is called will remove the observer
 *
 * Also provides a typed observable for cleaner fields change listeners
 */
fun <T> ObservableField<T>.addObserverOnceUntilDestroy(observer: Observer<T>, fragment: Fragment, callNow: Boolean) {
    val untypedObserver: Observable.OnPropertyChangedCallback = ObservableUtils.TypedObserver(this@addObserverOnceUntilDestroy, observer)
    observeUntilViewDestroy(this@addObserverOnceUntilDestroy, untypedObserver, fragment.viewLifecycleOwner.lifecycle)
    if (callNow) {
        observer.onChanged(this@addObserverOnceUntilDestroy.get())
    }
}

/**
 * Observes now, changes are nofitied to the adapter, and when activity onDestroy is called will remove the observer
 */
fun <T> ObservableList<T>.addObserverOnceUntilDestroy(adapter: RecyclerView.Adapter<*>, activity: AppCompatActivity, callNow: Boolean) {
    val observer: ObservableList.OnListChangedCallback<ObservableList<T>> = newAdapterListenerForObservableList(adapter)
    this@addObserverOnceUntilDestroy.addObserverOnceUntilDestroy(observer, activity, callNow)
}

/**
 * Observes now, changes are nofitied to the adapter, and when fragment onDestroyView is called will remove the observer
 */
fun <T> ObservableList<T>.addObserverOnceUntilDestroy(adapter: RecyclerView.Adapter<*>, fragment: Fragment, callNow: Boolean) {
    val observer: ObservableList.OnListChangedCallback<ObservableList<T>> = newAdapterListenerForObservableList(adapter)
    this@addObserverOnceUntilDestroy.addObserverOnceUntilDestroy(observer, fragment, callNow)
}

/**
 * Observes now, and when activity onDestroy is called will remove the observer
 */
fun <T> ObservableList<T>.addObserverOnceUntilDestroy(observer: ObservableList.OnListChangedCallback<ObservableList<T>>, activity: AppCompatActivity, callNow: Boolean) {
    observeUntilViewDestroy(this@addObserverOnceUntilDestroy, observer, activity.lifecycle)
    if (callNow) {
        observer.onChanged(this@addObserverOnceUntilDestroy)
    }
}

/**
 * Observes now, and when fragment onDestroyView is called will remove the observer
 */
fun <T> ObservableList<T>.addObserverOnceUntilDestroy(observer: ObservableList.OnListChangedCallback<ObservableList<T>>, fragment: Fragment, callNow: Boolean) {
    observeUntilViewDestroy(this@addObserverOnceUntilDestroy, observer, fragment.viewLifecycleOwner.lifecycle)
    if (callNow) {
        observer.onChanged(this@addObserverOnceUntilDestroy)
    }
}

fun Fragment.actionOnceOnDestroy(action: () -> Unit) {
    actionOnDestroy(this@actionOnceOnDestroy.viewLifecycleOwner.lifecycle, action)
}

fun AppCompatActivity.actionOnceOnDestroy(action: () -> Unit) {
    actionOnDestroy(this@actionOnceOnDestroy.lifecycle, action)
}

/**
 *  PRIVATE METHODS FROM HERE - ONES WHICH OPERATE ON THE RAW LIFECYCLE.
 *  PUBLIC FUNCTIONS TAKE EITHER A FRAGMENT OR ACTIVITY TO PREVENT MISTAKES USING THE WRONG LIFECYCLE.
 */

/**
 * Adds an observer to a lifecycle object, but cleanly removes it once onDestroy is called.
 * You can still use Lifecycle.removeObserver() with the given observer to remove it early without issue.
 */
private fun Lifecycle.addObserverOnceUntilDestroy(observer: DefaultLifecycleObserver) {
    val lifecycle = this@addObserverOnceUntilDestroy

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

private fun <T> setUntilViewDestroy(observableField: ObservableField<T?>, setNowContents: T, lifecycle: Lifecycle) {
    observableField.set(setNowContents)

    actionOnDestroy(lifecycle, { observableField.set(null) })
}

private fun <T> observeUntilViewDestroy(observableField: ObservableField<T>, observer: Observable.OnPropertyChangedCallback, lifecycle: Lifecycle) {
    observableField.addOnPropertyChangedCallback(observer)

    actionOnDestroy(lifecycle, { observableField.removeOnPropertyChangedCallback(observer) })
}

private fun <T> observeUntilViewDestroy(observableList: ObservableList<T>, observer: ObservableList.OnListChangedCallback<ObservableList<T>>, lifecycle: Lifecycle) {
    observableList.addOnListChangedCallback(observer)

    actionOnDestroy(lifecycle, { observableList.removeOnListChangedCallback(observer) })
}

private fun actionOnDestroy(lifecycle: Lifecycle, action: () -> Unit) {
    lifecycle.addObserverOnceUntilDestroy(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            action.invoke()
        }
    })
}
