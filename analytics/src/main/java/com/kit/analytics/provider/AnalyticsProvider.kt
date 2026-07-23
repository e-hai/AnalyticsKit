package com.kit.analytics.provider

import android.content.Context
import com.kit.analytics.AnalyticsEvent

/**
 * Adapter contract for a concrete analytics SDK (Firebase, AppsFlyer, etc.).
 *
 * Implementations should be lightweight and thread-safe; the kit may invoke
 * methods from any thread.
 */
interface AnalyticsProvider {
    /** Stable identifier used in debug logs, e.g. `"firebase"`. */
    val name: String

    /** One-time setup. Called from [com.kit.analytics.Analytics.initialize]. */
    fun initialize(context: Context)

    fun logEvent(event: AnalyticsEvent)

    fun setUserId(userId: String?)

    fun setUserProperty(name: String, value: String?)

    fun setAnalyticsCollectionEnabled(enabled: Boolean)

    /** Clear persisted identity / data on logout. Default is no-op. */
    fun resetAnalyticsData() = Unit
}
