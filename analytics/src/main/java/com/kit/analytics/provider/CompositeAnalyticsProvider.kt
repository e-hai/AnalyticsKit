package com.kit.analytics.provider

import android.content.Context
import android.util.Log
import com.kit.analytics.AnalyticsEvent

/**
 * Fans out every call to multiple [AnalyticsProvider]s.
 * Failures in one provider do not block the others.
 */
class CompositeAnalyticsProvider(
    private val providers: List<AnalyticsProvider>,
    private val debug: Boolean = false,
) : AnalyticsProvider {

    override val name: String = "composite"

    override fun initialize(context: Context) {
        forEachProvider("initialize") { it.initialize(context) }
    }

    override fun logEvent(event: AnalyticsEvent) {
        forEachProvider("logEvent") { it.logEvent(event) }
    }

    override fun setUserId(userId: String?) {
        forEachProvider("setUserId") { it.setUserId(userId) }
    }

    override fun setUserProperty(name: String, value: String?) {
        forEachProvider("setUserProperty") { it.setUserProperty(name, value) }
    }

    override fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        forEachProvider("setAnalyticsCollectionEnabled") {
            it.setAnalyticsCollectionEnabled(enabled)
        }
    }

    override fun resetAnalyticsData() {
        forEachProvider("resetAnalyticsData") { it.resetAnalyticsData() }
    }

    private inline fun forEachProvider(action: String, block: (AnalyticsProvider) -> Unit) {
        providers.forEach { provider ->
            try {
                block(provider)
            } catch (t: Throwable) {
                if (debug) {
                    Log.w(TAG, "Provider '${provider.name}' failed on $action", t)
                }
            }
        }
    }

    private companion object {
        const val TAG = "AnalyticsKit"
    }
}
