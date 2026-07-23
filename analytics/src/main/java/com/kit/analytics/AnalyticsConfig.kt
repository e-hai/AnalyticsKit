package com.kit.analytics

import com.kit.analytics.provider.AnalyticsProvider

/**
 * Kit configuration supplied once at [Analytics.initialize].
 *
 * @param providers concrete SDK adapters (Firebase, AppsFlyer, …). Empty list is allowed for no-op.
 * @param enabled master switch; when false, all calls are ignored after initialize.
 * @param debug when true, failures in providers are logged instead of being swallowed silently.
 */
data class AnalyticsConfig(
    val providers: List<AnalyticsProvider> = emptyList(),
    val enabled: Boolean = true,
    val debug: Boolean = false,
)
