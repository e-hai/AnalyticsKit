package com.kit.analytics

/**
 * Immutable analytics event payload dispatched to every registered [com.kit.analytics.provider.AnalyticsProvider].
 */
data class AnalyticsEvent(
    val name: String,
    val params: Map<String, AnalyticsValue> = emptyMap(),
)
