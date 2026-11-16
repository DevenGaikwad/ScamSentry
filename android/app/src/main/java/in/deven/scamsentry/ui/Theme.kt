package `in`.deven.scamsentry.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import `in`.deven.scamsentry.R
import `in`.deven.scamsentry.core.Risk

object Studio {
    val Background=Color(0xFF080C14)
    val Surface=Color(0xFF101722)
    val Raised=Color(0xFF172131)
    val Border=Color(0xFF26354B)
    val Text=Color(0xFFF2F5FA)
    val Muted=Color(0xFFB6C2D3)
    val Blue=Color(0xFF70A7FF)
    val Red=Color(0xFFFF6B7D)
    val Amber=Color(0xFFFFD166)
    val Green=Color(0xFF66D6A0)
    val Purple=Color(0xFFB89BFF)
    fun risk(risk: Risk)=when(risk) { Risk.HIGHER->Red; Risk.CHECKING->Amber; Risk.LOWER->Green; Risk.UNABLE->Amber }
    val Body=FontFamily(Font(R.font.inter))
    val Heading=FontFamily(Font(R.font.space_grotesk))
}
@Composable fun ScamSentryTheme(content: @Composable ()->Unit) {
    MaterialTheme(colorScheme=darkColorScheme(primary=Studio.Blue,onPrimary=Studio.Background,secondary=Studio.Purple,background=Studio.Background,onBackground=Studio.Text,surface=Studio.Surface,onSurface=Studio.Text,surfaceVariant=Studio.Raised,onSurfaceVariant=Studio.Muted,outline=Studio.Border,error=Studio.Red,onError=Studio.Background), typography=Typography(
        headlineLarge=TextStyle(fontFamily=Studio.Heading,fontSize=34.sp,lineHeight=40.sp,fontWeight=FontWeight.Bold),
        headlineMedium=TextStyle(fontFamily=Studio.Heading,fontSize=28.sp,lineHeight=34.sp,fontWeight=FontWeight.Bold),
        titleLarge=TextStyle(fontFamily=Studio.Heading,fontSize=22.sp,lineHeight=28.sp,fontWeight=FontWeight.Bold),
        titleMedium=TextStyle(fontFamily=Studio.Body,fontSize=17.sp,lineHeight=24.sp,fontWeight=FontWeight.SemiBold),
        bodyLarge=TextStyle(fontFamily=Studio.Body,fontSize=16.sp,lineHeight=25.sp),
        bodyMedium=TextStyle(fontFamily=Studio.Body,fontSize=14.sp,lineHeight=22.sp),
        labelLarge=TextStyle(fontFamily=Studio.Body,fontSize=14.sp,lineHeight=20.sp,fontWeight=FontWeight.SemiBold),
        labelMedium=TextStyle(fontFamily=Studio.Body,fontSize=12.sp,lineHeight=18.sp)
    ), content=content)
}
