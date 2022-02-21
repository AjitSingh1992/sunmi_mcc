package com.easyfoodvone.app_common.separation

import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import java.util.*

/**
 * Adds item move functionality to ObservableArrayList
 */
class ObservableArrayListMoveable<T> : ObservableArrayList<T>() {
    private val cachedListeners = LinkedList<OnListChangedCallback<*>?>()

    val cachedRegistry = ListChangeRegistry()

    override fun addOnListChangedCallback(listener: OnListChangedCallback<out ObservableList<*>>?) {
        cachedListeners.add(listener)
        cachedRegistry.add(listener)
        super.addOnListChangedCallback(listener)
    }

    override fun removeOnListChangedCallback(listener: OnListChangedCallback<out ObservableList<*>>?) {
        cachedListeners.remove(listener)
        cachedRegistry.remove(listener)

        super.removeOnListChangedCallback(listener)

    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        // Remove all the underlying listeners so that we can manipulate the list without firing them
        for (listener in cachedListeners) {
            super.removeOnListChangedCallback(listener)
        }

        // Move the item (now without firing the underlying listeners)
        Collections.swap(this, fromPosition, toPosition)

        // Add the underlying listeners back as they should be
        for (listener in cachedListeners) {
            super.addOnListChangedCallback(listener);
        }

        cachedRegistry.notifyMoved(this, fromPosition, toPosition, 1)
    }

    fun moveItemDone(fromPosition: Int, toPosition: Int) {
        // Remove all the underlying listeners so that we can manipulate the list without firing them

    }
}