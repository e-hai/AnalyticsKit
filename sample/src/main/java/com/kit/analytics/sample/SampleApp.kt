package com.kit.analytics.sample

import android.app.Application
import com.kit.analytics.Analytics
import com.kit.analytics.AnalyticsConfig
import com.kit.analytics.provider.LoggingAnalyticsProvider

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Analytics.initialize(
            context = this,
            config = AnalyticsConfig(
                // Default: Logcat only. Add your real google-services.json, enable the
                // google-services plugin, then register FirebaseAnalyticsProvider().
                providers = listOf(LoggingAnalyticsProvider()),
                enabled = true,
                debug = true,
            ),
        )
    }
}
