@file:Suppress("UNCHECKED_CAST")

package app.regate.map

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.viewModels(crossinline factory: () -> VM): Lazy<VM> =
    viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = factory() as T
        }
    }

@Suppress("LABEL_RESOLVE_WILL_CHANGE")
inline fun <reified VM : ViewModel> Fragment.viewModels(crossinline factory: (SavedStateHandle) -> VM): Lazy<VM> =
    viewModels {
        object : AbstractSavedStateViewModelFactory(this@viewModels, arguments) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T = factory(handle) as T
        }
    }
