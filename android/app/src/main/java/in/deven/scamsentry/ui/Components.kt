package `in`.deven.scamsentry.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import `in`.deven.scamsentry.*
import `in`.deven.scamsentry.core.*
import `in`.deven.scamsentry.data.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable fun Mark(modifier: Modifier=Modifier, kind: String="shield", color: Color=Studio.Blue) {
    Canvas(modifier.size(24.dp).clearAndSetSemantics { }) {
        val w=size.width; val h=size.height; val stroke=Stroke(width=w*.075f)
        when(kind) {
            "history" -> { drawCircle(color,radius=w*.34f,style=stroke); drawLine(color,Offset(w*.5f,h*.28f),Offset(w*.5f,h*.52f),strokeWidth=w*.075f); drawLine(color,Offset(w*.5f,h*.52f),Offset(w*.67f,h*.6f),strokeWidth=w*.075f) }
            "learn" -> { drawRoundRect(color,topLeft=Offset(w*.12f,h*.18f),size=androidx.compose.ui.geometry.Size(w*.76f,h*.64f),cornerRadius=androidx.compose.ui.geometry.CornerRadius(w*.07f),style=stroke); drawLine(color,Offset(w*.5f,h*.2f),Offset(w*.5f,h*.8f),strokeWidth=w*.06f) }
            "settings" -> { drawCircle(color,radius=w*.32f,style=stroke); drawCircle(color,radius=w*.1f,style=stroke) }
            "image" -> { drawRoundRect(color,topLeft=Offset(w*.1f,h*.15f),size=androidx.compose.ui.geometry.Size(w*.8f,h*.7f),style=stroke); val p=Path().apply { moveTo(w*.16f,h*.73f); lineTo(w*.4f,h*.45f); lineTo(w*.59f,h*.63f); lineTo(w*.73f,h*.47f); lineTo(w*.87f,h*.71f) }; drawPath(p,color,style=stroke); drawCircle(color,radius=w*.055f,center=Offset(w*.68f,h*.33f)) }
            else -> { val p=Path().apply { moveTo(w*.5f,h*.08f); lineTo(w*.84f,h*.23f); lineTo(w*.8f,h*.58f); quadraticTo(w*.76f,h*.8f,w*.5f,h*.92f); quadraticTo(w*.24f,h*.8f,w*.2f,h*.58f); lineTo(w*.16f,h*.23f); close() }; drawPath(p,color,style=stroke); drawCircle(color,radius=w*.13f,center=Offset(w*.46f,h*.44f),style=stroke); drawLine(color,Offset(w*.56f,h*.55f),Offset(w*.68f,h*.69f),strokeWidth=w*.07f) }
        }
    }
}

@Composable internal fun Panel(modifier: Modifier=Modifier, color: Color=Studio.Surface, content: @Composable ColumnScope.()->Unit) {
    Column(modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(color).border(1.dp,Studio.Border,RoundedCornerShape(20.dp)).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),content=content)
}
@Composable internal fun Tag(text: String, color: Color=Studio.Blue) {
    Text(text,style=MaterialTheme.typography.labelMedium,color=color,modifier=Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(alpha=.1f)).padding(horizontal=10.dp,vertical=6.dp))
}
@Composable internal fun Primary(text: String, enabled: Boolean=true, action: ()->Unit) {
    Button(onClick=action,enabled=enabled,shape=RoundedCornerShape(14.dp),modifier=Modifier.fillMaxWidth().heightIn(min=54.dp),contentPadding=PaddingValues(16.dp)) { Text(text) }
}
@Composable internal fun Page(content: @Composable ColumnScope.()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(horizontal=24.dp,vertical=20.dp).widthIn(max=760.dp),verticalArrangement=Arrangement.spacedBy(20.dp),content=content)
}

