package `in`.deven.scamsentry.ui

import android.animation.ValueAnimator
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.isActive

/** Conceptual processing only. Private input has no effect on this drawing. */
@Composable
internal fun EditorialPipeline(animate: Boolean) {
    val palette = studio
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val systemMotion = systemAllowsMotion()
    var visible by remember { mutableStateOf(false) }
    val time = remember { mutableFloatStateOf(0f) }
    val active = animate && systemMotion && visible

    LaunchedEffect(active, lifecycle) {
        if (!active) {
            time.floatValue = 0f
            return@LaunchedEffect
        }
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            var previous = 0L
            try {
                while (isActive) {
                    withFrameNanos { now ->
                        if (previous != 0L && now - previous < 32_000_000L) return@withFrameNanos
                        if (previous != 0L) time.floatValue += ((now - previous) / 1_000_000f).coerceAtMost(50f)
                        previous = now
                    }
                }
            } finally {
                time.floatValue = 0f
            }
        }
    }

    Box(
        Modifier.widthIn(max = 460.dp).fillMaxWidth().height(44.dp)
            .onGloballyPositioned { visible = it.boundsInWindow().height > 0 }
            .clearAndSetSemantics {}
            .drawWithCache {
                val stroke = 1.dp.toPx()
                val x = size.width
                val y = size.height
                val nodes = listOf(Offset(x * .04f, y * .16f), Offset(x * .45f, y * .54f), Offset(x * .84f, y * .16f))
                val width = x * .12f
                val height = y * .30f
                val paths = nodes.zipWithNext().map { (from, to) ->
                    val turn = (from.x + width + to.x) / 2
                    Path().apply {
                        moveTo(from.x + width, from.y + height / 2)
                        lineTo(turn, from.y + height / 2)
                        lineTo(turn, to.y + height / 2)
                        lineTo(to.x, to.y + height / 2)
                    }
                }
                val tracks = paths.map { path -> PathMeasure().apply { setPath(path, false) } }
                val dot = 3.dp.toPx()
                onDrawBehind {
                    paths.forEach { drawPath(it, palette.control, style = Stroke(stroke)) }
                    nodes.forEachIndexed { i, node ->
                        drawRect(if (i == 1) palette.raised else palette.surface, node, Size(width, height))
                        drawRect(palette.control, node, Size(width, height), style = Stroke(stroke))
                        drawRect(palette.ink, Offset(node.x + width - dot / 2, node.y + height / 2 - dot / 2), Size(dot, dot))
                    }
                    if (active && time.floatValue > 0f) tracks.forEachIndexed { i, track ->
                        val phase = (time.floatValue / 4200f + i * .43f) % 1f
                        val point = track.getPosition(phase * track.length)
                        val opacity = minOf(1f, phase * 12f, (1f - phase) * 12f)
                        drawRect(palette.ink, point - Offset(dot / 2, dot / 2), Size(dot, dot), alpha = opacity)
                    }
                }
            },
    )
}

@Composable
private fun systemAllowsMotion(): Boolean {
    val resolver = LocalContext.current.contentResolver
    var enabled by remember { mutableStateOf(ValueAnimator.areAnimatorsEnabled()) }
    DisposableEffect(resolver) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                enabled = ValueAnimator.areAnimatorsEnabled()
            }
        }
        resolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE), false, observer)
        onDispose { resolver.unregisterContentObserver(observer) }
    }
    return enabled
}
