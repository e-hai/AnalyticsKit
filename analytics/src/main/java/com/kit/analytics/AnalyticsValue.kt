package com.kit.analytics

/**
 * Typed event / property values aligned with common analytics SDK constraints
 * (e.g. Firebase Analytics Bundle types).
 */
sealed class AnalyticsValue {
    data class StringValue(val value: String) : AnalyticsValue()
    data class LongValue(val value: Long) : AnalyticsValue()
    data class DoubleValue(val value: Double) : AnalyticsValue()
    data class BooleanValue(val value: Boolean) : AnalyticsValue()

    companion object {
        fun of(value: String): AnalyticsValue = StringValue(value)
        fun of(value: Long): AnalyticsValue = LongValue(value)
        fun of(value: Int): AnalyticsValue = LongValue(value.toLong())
        fun of(value: Double): AnalyticsValue = DoubleValue(value)
        fun of(value: Float): AnalyticsValue = DoubleValue(value.toDouble())
        fun of(value: Boolean): AnalyticsValue = BooleanValue(value)
    }
}
