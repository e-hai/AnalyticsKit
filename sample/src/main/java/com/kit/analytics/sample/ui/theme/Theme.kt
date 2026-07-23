package com.kit.analytics.sample.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KitColorScheme = lightColorScheme(
    primary = Ember,
    onPrimary = Foam,
    primaryContainer = Ember.copy(alpha = 0.16f),
    onPrimaryContainer = EmberDeep,
    secondary = Sea,
    onSecondary = Foam,
    secondaryContainer = SeaSoft.copy(alpha = 0.18f),
    onSecondaryContainer = Ink,
    tertiary = Signal,
    background = Mist,
    onBackground = Ink,
    surface = Mist,
    onSurface = Ink,
    surfaceVariant = MistDeep.copy(alpha = 0.55f),
    onSurfaceVariant = InkMuted,
    outline = InkMuted.copy(alpha = 0.35f),
)

@Composable
fun AnalyticsKitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KitColorScheme,
        typography = Typography,
        content = content,
    )
}

/** Scrim used behind ambient gradient layers. */
val AmbientTop: Color = Mist
val AmbientBottom: Color = MistDeep
