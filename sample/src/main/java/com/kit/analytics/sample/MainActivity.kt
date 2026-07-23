package com.kit.analytics.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kit.analytics.Analytics
import com.kit.analytics.sample.ui.theme.AmbientBottom
import com.kit.analytics.sample.ui.theme.AmbientTop
import com.kit.analytics.sample.ui.theme.AnalyticsKitTheme
import com.kit.analytics.sample.ui.theme.Ember
import com.kit.analytics.sample.ui.theme.Foam
import com.kit.analytics.sample.ui.theme.Ink
import com.kit.analytics.sample.ui.theme.InkMuted
import com.kit.analytics.sample.ui.theme.Sea
import com.kit.analytics.sample.ui.theme.SeaSoft
import com.kit.analytics.sample.ui.theme.Signal
import kotlin.math.PI
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Analytics.logScreenView(screenName = "main", screenClass = javaClass.simpleName)
        enableEdgeToEdge()
        setContent {
            AnalyticsKitTheme {
                SampleScreen(
                    onFireEvent = {
                        Analytics.logEvent("button_click") {
                            param("button_id", "track_demo")
                            param("screen", "main")
                        }
                    },
                    onSetUser = {
                        Analytics.setUserId("demo_user")
                        Analytics.setUserProperty("cohort", "sample")
                    },
                    onReset = {
                        Analytics.reset()
                    },
                )
            }
        }
    }
}

@Composable
fun SampleScreen(
    onFireEvent: () -> Unit = {},
    onSetUser: () -> Unit = {},
    onReset: () -> Unit = {},
) {
    val feed = remember { mutableStateListOf<String>() }
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
        feed.add(0, "screen_view · main")
    }

    fun push(label: String) {
        feed.add(0, label)
        if (feed.size > 5) feed.removeAt(feed.lastIndex)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AmbientTop, Color(0xFFE4F0ED), AmbientBottom),
                ),
            ),
    ) {
        SignalField(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.TopCenter),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp),
        ) {
            AnimatedVisibility(
                visible = entered,
                enter = fadeIn(tween(700)) + slideInVertically(
                    animationSpec = tween(700, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 8 },
                ),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "AnalyticsKit",
                        style = MaterialTheme.typography.displayLarge,
                        color = Ink,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Track once.\nFan out everywhere.",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Ink,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Events flow through the kit pipeline into every registered provider.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkMuted,
                        modifier = Modifier.fillMaxWidth(0.92f),
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    CtaRow(
                        onFireEvent = {
                            onFireEvent()
                            push("button_click · track_demo")
                        },
                        onSetUser = {
                            onSetUser()
                            push("setUserId · demo_user")
                        },
                    )
                    TextButton(
                        onClick = {
                            onReset()
                            push("reset · analytics data")
                        },
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        Text("Reset identity", color = InkMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            EventFeed(entries = feed)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CtaRow(
    onFireEvent: () -> Unit,
    onSetUser: () -> Unit,
) {
    val fireInteraction = remember { MutableInteractionSource() }
    val pressed by fireInteraction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = "ctaScale",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onFireEvent,
            interactionSource = fireInteraction,
            modifier = Modifier
                .scale(scale)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Ember,
                contentColor = Foam,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        ) {
            Text("Fire event", style = MaterialTheme.typography.labelLarge)
        }
        TextButton(onClick = onSetUser) {
            Text("Set user", color = Sea, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun EventFeed(entries: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "LIVE PIPE",
            style = MaterialTheme.typography.labelMedium,
            color = Sea,
        )
        AnimatedContent(
            targetState = entries.firstOrNull().orEmpty(),
            transitionSpec = {
                (fadeIn(tween(280)) + slideInVertically { it / 3 })
                    .togetherWith(fadeOut(tween(160)))
            },
            label = "feedHead",
        ) { latest ->
            if (latest.isNotEmpty()) {
                Text(
                    text = latest,
                    style = MaterialTheme.typography.titleLarge,
                    color = Ink,
                )
            }
        }
        entries.drop(1).forEach { line ->
            Text(
                text = line,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted.copy(alpha = 0.75f),
            )
        }
    }
}

@Composable
private fun SignalField(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "signal")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "phase",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Signal.copy(alpha = 0.28f * pulse), Color.Transparent),
                center = Offset(w * 0.82f, h * 0.35f),
                radius = w * 0.45f,
            ),
            radius = w * 0.45f,
            center = Offset(w * 0.82f, h * 0.35f),
        )

        val path = Path()
        val midY = h * 0.55f
        val amp = h * 0.12f
        path.moveTo(0f, midY)
        var x = 0f
        while (x <= w) {
            val y = midY + sin((x / w) * 4f * PI + phase).toFloat() * amp *
                (0.55f + 0.45f * sin((x / w) * PI).toFloat())
            path.lineTo(x, y)
            x += 6f
        }
        drawPath(
            path = path,
            color = SeaSoft.copy(alpha = 0.55f),
            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round),
        )

        val barCount = 18
        val barGap = 8.dp.toPx()
        val barWidth = ((w * 0.42f) - barGap * (barCount - 1)) / barCount
        val originX = w * 0.08f
        val baseY = h * 0.88f
        for (i in 0 until barCount) {
            val t = sin(phase + i * 0.45f).toFloat() * 0.5f + 0.5f
            val barH = (28.dp.toPx() + t * 72.dp.toPx()) * (0.7f + 0.3f * pulse)
            val left = originX + i * (barWidth + barGap)
            drawRoundRect(
                color = Sea.copy(alpha = 0.18f + t * 0.35f),
                topLeft = Offset(left, baseY - barH),
                size = Size(barWidth, barH),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SampleScreenPreview() {
    AnalyticsKitTheme {
        SampleScreen()
    }
}
