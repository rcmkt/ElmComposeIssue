package com.example.elmcompose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import coil.Coil
import com.example.elmcompose.ui.theme.ElmComposeTheme
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store

abstract class BaseFragment<Event : Any, Effect : Any, State : Any> : Fragment(),
    ElmDelegate<Event, Effect, State> {

    // region elmslie stuff
    protected val store
        get() = storeHolder.store

    @Suppress("LeakingThis", "UnusedPrivateMember")
    private val elm = ElmScreen(this, lifecycle) { requireActivity() }

    override val storeHolder: StoreHolder<Event, Effect, State> =
        LifecycleAwareStoreHolder(lifecycle, ::createStore)

    final override fun render(state: State) = Unit
    // endregion

    open var windowInsetsAnimationsEnabled = true

    @ExperimentalAnimatedInsets
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // Create an ViewWindowInsetObserver using this view
            val observer = ViewWindowInsetObserver(this)
            // Call start() to start listening now.
            // The WindowInsets instance is returned to us.
            val windowInsets = observer.start(
                windowInsetsAnimationsEnabled = windowInsetsAnimationsEnabled,
                consumeWindowInsets = false
            )
            setContent {
                ElmComposeTheme {
                    CompositionLocalProvider(
                        LocalImageLoader provides Coil.imageLoader(requireContext()),
                        LocalWindowInsets provides windowInsets
                    ) {
                        val state by store.states.subscribeAsState(initial = store.currentState)
                        Content(store, state)
                        DisposableEffect(this) {
                            val disposable = store.effects.subscribe {
                                handleEffect(it)
                            }
                            onDispose { disposable.dispose() }
                        }
                    }
                }
            }
        }
    }

    @Composable
    abstract fun Content(store: Store<Event, Effect, State>, state: State)
}