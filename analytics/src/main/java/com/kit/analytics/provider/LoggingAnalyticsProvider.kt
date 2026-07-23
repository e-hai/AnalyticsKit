package com.kit.analytics.provider

import android.content.Context
import android.util.Log
import com.kit.analytics.AnalyticsEvent
import com.kit.analytics.AnalyticsValue

/**
 * Debug provider that prints every call to Logcat.
 * Useful during development before a real SDK is wired.
 */
class LoggingAnalyticsProvider(
    private val tag: String = "AnalyticsKit",
) : AnalyticsProvider {

    override val name: String = "logging"

    override fun initialize(context: Context) {
        Log.d(tag, "initialize(package=${context.packageName})")
    }

    override fun logEvent(event: AnalyticsEvent) {
        val params = event.params.entries.joinToString { (k, v) -> "$k=${format(v)}" }
        Log.d(tag, "logEvent(name=${event.name}, params={$params})")
    }

    override fun setUserId(userId: String?) {
        Log.d(tag, "setUserId($userId)")
    }

    override fun setUserProperty(name: String, value: String?) {
        Log.d(tag, "setUserProperty($name=$value)")
    }

    override fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        Log.d(tag, "setAnalyticsCollectionEnabled($enabled)")
    }

    override fun resetAnalyticsData() {
        Log.d(tag, "resetAnalyticsData()")
    }

    private fun format(value: AnalyticsValue): String = when (value) {
        is AnalyticsValue.StringValue -> value.value
        is AnalyticsValue.LongValue -> value.value.toString()
        is AnalyticsValue.DoubleValue -> value.value.toString()
        is AnalyticsValue.BooleanValue -> value.value.toString()
    }
}
