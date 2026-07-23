package com.kit.analytics

import android.content.Context
import android.util.Log
import com.kit.analytics.provider.AnalyticsProvider
import com.kit.analytics.provider.CompositeAnalyticsProvider
import com.kit.analytics.provider.NoOpAnalyticsProvider
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Unified analytics entry point for application code.
 *
 * ```
 * Analytics.initialize(context, AnalyticsConfig(
 *     providers = listOf(LoggingAnalyticsProvider(), /* FirebaseAnalyticsProvider() */),
 *     debug = BuildConfig.DEBUG,
 * ))
 *
 * Analytics.logEvent("button_click") {
 *     param("screen", "home")
 * }
 * ```
 */
object Analytics {
    private const val TAG = "AnalyticsKit"
    private const val SCREEN_VIEW = "screen_view"
    private const val SCREEN_NAME = "screen_name"
    private const val SCREEN_CLASS = "screen_class"

    private val initialized = AtomicBoolean(false)

    @Volatile
    private var enabled: Boolean = true

    @Volatile
    private var debug: Boolean = false

    @Volatile
    private var provider: AnalyticsProvider = NoOpAnalyticsProvider

    @JvmStatic
    fun initialize(context: Context, config: AnalyticsConfig = AnalyticsConfig()) {
        if (!initialized.compareAndSet(false, true)) {
            if (config.debug) {
                Log.w(TAG, "Analytics.initialize() called more than once; ignoring")
            }
            return
        }

        enabled = config.enabled
        debug = config.debug
        provider = when {
            config.providers.isEmpty() -> NoOpAnalyticsProvider
            config.providers.size == 1 -> config.providers.first()
            else -> CompositeAnalyticsProvider(config.providers, debug = config.debug)
        }

        val appContext = context.applicationContext
        runCatching { provider.initialize(appContext) }
            .onFailure { t ->
                if (debug) Log.e(TAG, "initialize failed", t)
            }
    }

    @JvmStatic
    fun isInitialized(): Boolean = initialized.get()

    @JvmStatic
    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        if (!initialized.get()) return
        runCatching { provider.setAnalyticsCollectionEnabled(enabled) }
            .onFailure { t -> if (debug) Log.e(TAG, "setEnabled failed", t) }
    }

    @JvmStatic
    fun isEnabled(): Boolean = enabled

    @JvmStatic
    fun logEvent(name: String, params: Map<String, AnalyticsValue> = emptyMap()) {
        dispatch { provider.logEvent(AnalyticsEvent(name, params)) }
    }

    inline fun logEvent(name: String, crossinline block: AnalyticsParamsBuilder.() -> Unit) {
        val params = AnalyticsParamsBuilder().apply(block).build()
        logEvent(name, params)
    }

    @JvmStatic
    fun logScreenView(screenName: String, screenClass: String? = null) {
        logEvent(SCREEN_VIEW) {
            param(SCREEN_NAME, screenName)
            if (screenClass != null) {
                param(SCREEN_CLASS, screenClass)
            }
        }
    }

    @JvmStatic
    fun setUserId(userId: String?) {
        dispatch { provider.setUserId(userId) }
    }

    @JvmStatic
    fun setUserProperty(name: String, value: String?) {
        dispatch { provider.setUserProperty(name, value) }
    }

    @JvmStatic
    fun reset() {
        dispatch { provider.resetAnalyticsData() }
    }

    private inline fun dispatch(block: () -> Unit) {
        if (!initialized.get()) {
            if (debug) Log.w(TAG, "Analytics call before initialize(); ignored")
            return
        }
        if (!enabled) return
        runCatching(block)
            .onFailure { t -> if (debug) Log.e(TAG, "dispatch failed", t) }
    }
}
