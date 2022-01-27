package com.easyfoodvone.app_common.separation

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class ObservableUtils {
    companion object {
        fun <T> newAdapterListenerForObservableList(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): OnListChangedCallback<ObservableList<T>> {
            return object : OnListChangedCallback<ObservableList<T>>() {
                override fun onChanged(sender: ObservableList<T>) {
                    adapter.notifyDataSetChanged()
                }

                override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                    adapter.notifyItemRangeChanged(positionStart, itemCount)
                }

                override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                    adapter.notifyItemRangeInserted(positionStart, itemCount)
                }

                override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
                    if (itemCount != 1) {
                        throw IllegalArgumentException("Adapter only supports a single item for onItemRangeMoved, do this one at a time")
                    } else {
                        adapter.notifyItemMoved(fromPosition, toPosition)
                    }
                }

                override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                    adapter.notifyItemRangeRemoved(positionStart, itemCount)
                }
            }
        }
    }

    class TypedObserver<T>(val observableField: ObservableField<T>, val wrapped: Observer<T>) : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            wrapped.onChanged(observableField.get())
        }

        // Allow field.remove(new TypedObserver(observer)) to remove any TypedObserver with the same wrapped observer
        override fun equals(other: Any?): Boolean {
            return other === this || (other is TypedObserver<*> && other.wrapped == this.wrapped)
        }

        // Allow field.remove(new TypedObserver(observer)) to remove any TypedObserver with the same wrapped observer
        override fun hashCode(): Int {
            return wrapped.hashCode()
        }
    }
}