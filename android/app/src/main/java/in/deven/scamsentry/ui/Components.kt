package `in`.deven.scamsentry.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.deven.scamsentry.R

@Composable
internal fun Mark(modifier: Modifier = Modifier, kind: String = "brand", color: Color = studio.ink) {
    if (kind == "brand") {
        Icon(painterResource(R.drawable.ic_signal_aperture), null, modifier.size(28.dp), tint = color)
        return
    }
    Canvas(modifier.size(24.dp).clearAndSetSemantics {}) {
        val w = size.width
        val h = size.height
        val stroke = w * .07f
        when (kind) {
            "history" -> {
                drawCircle(color, radius = w * .34f, style = Stroke(stroke))
                drawLine(color, Offset(w * .5f, h * .28f), Offset(w * .5f, h * .52f), stroke)
                drawLine(color, Offset(w * .5f, h * .52f), Offset(w * .67f, h * .6f), stroke)
            }
            "learn" -> {
                drawRect(color, Offset(w * .12f, h * .18f), Size(w * .76f, h * .64f), style = Stroke(stroke))
                drawLine(color, Offset(w * .5f, h * .2f), Offset(w * .5f, h * .8f), stroke)
            }
            "settings" -> {
                for (y in listOf(.26f, .5f, .74f)) {
                    val knob = if (y == .5f) .58f else .35f
                    drawLine(color, Offset(w * .14f, h * y), Offset(w * .86f, h * y), stroke)
                    drawRect(color, Offset(w * knob, h * y - stroke), Size(stroke * 2, stroke * 2))
                }
            }
            "image" -> {
                drawRect(color, Offset(w * .1f, h * .15f), Size(w * .8f, h * .7f), style = Stroke(stroke))
                val mountain = Path().apply {
                    moveTo(w * .16f, h * .73f)
                    lineTo(w * .4f, h * .45f)
                    lineTo(w * .59f, h * .63f)
                    lineTo(w * .73f, h * .47f)
                    lineTo(w * .87f, h * .71f)
                }
                drawPath(mountain, color, style = Stroke(stroke))
                drawCircle(color, radius = w * .055f, center = Offset(w * .68f, h * .33f))
            }
        }
    }
}

@Composable
internal fun Panel(
    modifier: Modifier = Modifier,
    color: Color = studio.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier.fillMaxWidth().background(color).border(1.dp, studio.rule).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content,
    )
}

@Composable
internal fun Tag(text: String, color: Color = studio.muted) {
    Text(text, style = MaterialTheme.typography.labelMedium, color = color)
}

@Composable
internal fun Heading(text: String, style: TextStyle = MaterialTheme.typography.headlineLarge) {
    Text(text, style = style, modifier = Modifier.semantics { heading() })
}

@Composable
internal fun Primary(text: String, enabled: Boolean = true, action: () -> Unit) {
    Button(
        onClick = action, enabled = enabled, shape = RectangleShape,
        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
    ) { Text(text) }
}

@Composable
internal fun Page(
    scroll: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(Modifier.fillMaxSize().imePadding(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier.widthIn(max = 760.dp).fillMaxSize().verticalScroll(scroll)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content,
        )
    }
}
