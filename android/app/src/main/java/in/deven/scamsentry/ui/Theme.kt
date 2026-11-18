package `in`.deven.scamsentry.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import `in`.deven.scamsentry.R
import `in`.deven.scamsentry.core.Risk
import `in`.deven.scamsentry.data.AppTheme

@Immutable
internal data class StudioPalette(
    val paper: Color,
    val surface: Color,
    val raised: Color,
    val rule: Color,
    val ink: Color,
    val muted: Color,
    val control: Color,
    val danger: Color,
    val caution: Color,
    val positive: Color,
    val uncertain: Color,
) {
    fun risk(risk: Risk) = when (risk) {
        Risk.HIGHER -> danger
        Risk.CHECKING -> caution
        Risk.LOWER -> positive
        Risk.UNABLE -> uncertain
    }
}

private val light = StudioPalette(
    paper = Color(0xFFF5F5F5), surface = Color(0xFFFCFCFC), raised = Color(0xFFE7E7E7),
    rule = Color(0xFFB8B8B8), ink = Color(0xFF171717), muted = Color(0xFF555555),
    control = Color(0xFF6B6B6B), danger = Color(0xFF8F2C24), caution = Color(0xFF725006),
    positive = Color(0xFF2D6447), uncertain = Color(0xFF4D5866),
)
private val dark = StudioPalette(
    paper = Color(0xFF101010), surface = Color(0xFF161616), raised = Color(0xFF222222),
    rule = Color(0xFF555555), ink = Color(0xFFF4F4F4), muted = Color(0xFFB8B8B8),
    control = Color(0xFFAAAAAA), danger = Color(0xFFFFA293), caution = Color(0xFFF4CB76),
    positive = Color(0xFF9DD9B5), uncertain = Color(0xFFBDCADD),
)

private val LocalStudio = staticCompositionLocalOf { light }
internal val studio: StudioPalette
    @Composable get() = LocalStudio.current

internal object Studio {
    val Heading = FontFamily(Font(R.font.archivo_black, FontWeight.Black))
    val Mono = FontFamily(Font(R.font.geist_mono))
}

private val type = Typography(
    displayLarge = TextStyle(fontFamily = Studio.Heading, fontSize = 52.sp, lineHeight = 55.sp, fontWeight = FontWeight.Black, letterSpacing = (-.8).sp),
    headlineLarge = TextStyle(fontFamily = Studio.Heading, fontSize = 38.sp, lineHeight = 42.sp, fontWeight = FontWeight.Black, letterSpacing = (-.45).sp),
    headlineMedium = TextStyle(fontFamily = Studio.Heading, fontSize = 30.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black),
    titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 22.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontFamily = Studio.Mono, fontSize = 11.sp, lineHeight = 17.sp, letterSpacing = .35.sp),
)

private val sharp = androidx.compose.foundation.shape.RoundedCornerShape(CornerSize(0))
private val shapes = Shapes(sharp, sharp, sharp, sharp, sharp)

@Composable
internal fun usesDarkTheme(choice: AppTheme): Boolean = when (choice) {
    AppTheme.SYSTEM -> isSystemInDarkTheme()
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
}

@Composable
internal fun ScamSentryTheme(isDark: Boolean, content: @Composable () -> Unit) {
    val p = if (isDark) dark else light
    val colors = if (isDark) darkColorScheme() else lightColorScheme()
    CompositionLocalProvider(LocalStudio provides p) {
        MaterialTheme(
            colorScheme = colors.copy(
                primary = p.ink, onPrimary = p.paper,
                primaryContainer = p.raised, onPrimaryContainer = p.ink,
                inversePrimary = p.paper, primaryFixed = p.ink, primaryFixedDim = p.muted,
                onPrimaryFixed = p.paper, onPrimaryFixedVariant = p.raised,
                secondary = p.ink, onSecondary = p.paper,
                secondaryContainer = p.raised, onSecondaryContainer = p.ink,
                tertiary = p.ink, onTertiary = p.paper,
                tertiaryContainer = p.raised, onTertiaryContainer = p.ink,
                background = p.paper, onBackground = p.ink,
                surface = p.surface, onSurface = p.ink,
                surfaceVariant = p.raised, onSurfaceVariant = p.muted,
                surfaceTint = p.surface, inverseSurface = p.ink, inverseOnSurface = p.paper,
                surfaceBright = p.paper, surfaceDim = p.raised,
                surfaceContainerLowest = p.paper, surfaceContainerLow = p.surface,
                surfaceContainer = p.surface, surfaceContainerHigh = p.raised, surfaceContainerHighest = p.raised,
                outline = p.control, outlineVariant = p.rule,
                error = p.danger, onError = p.paper, errorContainer = p.raised, onErrorContainer = p.danger,
            ),
            typography = type, shapes = shapes, content = content,
        )
    }
}
