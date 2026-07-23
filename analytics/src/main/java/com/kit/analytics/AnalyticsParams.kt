package com.kit.analytics

/**
 * DSL builder for event parameters.
 *
 * ```
 * Analytics.logEvent("purchase") {
 *     param("item_id", "sku_123")
 *     param("value", 9.99)
 *     param("quantity", 1)
 * }
 * ```
 */
class AnalyticsParamsBuilder {
    private val params = linkedMapOf<String, AnalyticsValue>()

    fun param(key: String, value: String) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: Long) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: Int) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: Double) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: Float) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: Boolean) {
        params[key] = AnalyticsValue.of(value)
    }

    fun param(key: String, value: AnalyticsValue) {
        params[key] = value
    }

    fun build(): Map<String, AnalyticsValue> = params.toMap()
}
