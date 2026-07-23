package com.kit.analytics.firebase

import android.os.Bundle
import com.kit.analytics.AnalyticsValue

internal fun Map<String, AnalyticsValue>.toFirebaseBundle(): Bundle {
    if (isEmpty()) return Bundle.EMPTY
    val bundle = Bundle(size)
    forEach { (key, value) ->
        when (value) {
            is AnalyticsValue.StringValue -> bundle.putString(key, value.value)
            is AnalyticsValue.LongValue -> bundle.putLong(key, value.value)
            is AnalyticsValue.DoubleValue -> bundle.putDouble(key, value.value)
            // Firebase Analytics Bundle does not accept Boolean; encode as "true"/"false".
            is AnalyticsValue.BooleanValue -> bundle.putString(key, value.value.toString())
        }
    }
    return bundle
}
