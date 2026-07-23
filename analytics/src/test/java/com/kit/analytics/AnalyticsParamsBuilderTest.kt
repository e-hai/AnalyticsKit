package com.kit.analytics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsParamsBuilderTest {
    @Test
    fun buildsTypedParams() {
        val params = AnalyticsParamsBuilder().apply {
            param("name", "sku")
            param("qty", 2)
            param("price", 9.9)
            param("vip", true)
        }.build()

        assertEquals(AnalyticsValue.StringValue("sku"), params["name"])
        assertEquals(AnalyticsValue.LongValue(2L), params["qty"])
        assertEquals(AnalyticsValue.DoubleValue(9.9), params["price"])
        assertEquals(AnalyticsValue.BooleanValue(true), params["vip"])
        assertEquals(4, params.size)
    }

    @Test
    fun eventKeepsNameAndParams() {
        val event = AnalyticsEvent(
            name = "purchase",
            params = mapOf("value" to AnalyticsValue.of(1.0)),
        )
        assertEquals("purchase", event.name)
        assertTrue(event.params.containsKey("value"))
    }
}
