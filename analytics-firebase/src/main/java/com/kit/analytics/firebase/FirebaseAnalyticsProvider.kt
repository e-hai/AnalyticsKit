package com.kit.analytics.firebase

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.firebase.analytics.FirebaseAnalytics
import com.kit.analytics.AnalyticsEvent
import com.kit.analytics.provider.AnalyticsProvider

/**
 * [AnalyticsProvider] backed by [FirebaseAnalytics].
 *
 * Host apps must configure Firebase (typically via `google-services` plugin +
 * `google-services.json`) before calling [com.kit.analytics.Analytics.initialize].
 *
 * ```
 * Analytics.initialize(
 *     context,
 *     AnalyticsConfig(providers = listOf(FirebaseAnalyticsProvider())),
 * )
 * ```
 */
class FirebaseAnalyticsProvider : AnalyticsProvider {

    override val name: String = "firebase"

    @Volatile
    private var analytics: FirebaseAnalytics? = null

    @RequiresPermission(allOf = [Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK])
    override fun initialize(context: Context) {
        analytics = FirebaseAnalytics.getInstance(context.applicationContext)
    }

    override fun logEvent(event: AnalyticsEvent) {
        requireAnalytics().logEvent(event.name, event.params.toFirebaseBundle())
    }

    override fun setUserId(userId: String?) {
        requireAnalytics().setUserId(userId)
    }

    override fun setUserProperty(name: String, value: String?) {
        requireAnalytics().setUserProperty(name, value)
    }

    override fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        requireAnalytics().setAnalyticsCollectionEnabled(enabled)
    }

    override fun resetAnalyticsData() {
        requireAnalytics().resetAnalyticsData()
    }

    private fun requireAnalytics(): FirebaseAnalytics {
        return analytics
            ?: error("FirebaseAnalyticsProvider is not initialized. Call Analytics.initialize() first.")
    }
}
