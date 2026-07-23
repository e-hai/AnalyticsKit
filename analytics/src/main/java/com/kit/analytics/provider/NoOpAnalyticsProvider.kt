package com.kit.analytics.provider

import android.content.Context
import com.kit.analytics.AnalyticsEvent

/** Drop-all provider for tests, previews, or when analytics is disabled by build flavor. */
object NoOpAnalyticsProvider : AnalyticsProvider {
    override val name: String = "noop"

    override fun initialize(context: Context) = Unit

    override fun logEvent(event: AnalyticsEvent) = Unit

    override fun setUserId(userId: String?) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit

    override fun setAnalyticsCollectionEnabled(enabled: Boolean) = Unit
}
