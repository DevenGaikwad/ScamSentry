package `in`.deven.scamsentry.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.Closeable
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import androidx.exifinterface.media.ExifInterface

data class OcrDraft(val text: String, val warning: String?, val preview: Bitmap) : Closeable {
    override fun close() { if (!preview.isRecycled) preview.recycle() }
}

object ImageOrientation {
    fun apply(bitmap: Bitmap, orientation: Int): Bitmap {
        val m = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> m.setScale(-1f,1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> m.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> m.setScale(1f,-1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> { m.setRotate(90f); m.postScale(-1f,1f) }
            ExifInterface.ORIENTATION_ROTATE_90 -> m.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> { m.setRotate(-90f); m.postScale(-1f,1f) }
            ExifInterface.ORIENTATION_ROTATE_270 -> m.setRotate(-90f)
            else -> return bitmap
        }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,m,true).also { if (it !== bitmap) bitmap.recycle() }
    }
}
interface OcrSource { suspend fun read(uri: Uri): OcrDraft }
class OcrReader(private val context: Context, private val decodeOverride: (suspend (Uri) -> OcrDraft)? = null) : OcrSource {
    override suspend fun read(uri: Uri): OcrDraft {
        // Assign ownership inside IO so prompt cancellation on dispatcher return cannot leak it.
        var owned: OcrDraft? = null
        try {
            withContext(Dispatchers.IO) { owned = decodeOverride?.invoke(uri) ?: decode(uri) }
            currentCoroutineContext().ensureActive()
            return requireNotNull(owned).also { owned = null }
        } finally { owned?.close() }
    }
    private suspend fun decode(uri: Uri): OcrDraft {
        if(uri.scheme != "content") throw InputFailure(InputFailureCode.UNSUPPORTED_IMAGE,"Select an image using the photo picker.")
        val mime = context.contentResolver.getType(uri)
        if(mime !in setOf("image/png", "image/jpeg", "image/webp")) throw InputFailure(InputFailureCode.UNSUPPORTED_IMAGE,"Choose a PNG, JPEG or WebP image.")
        val bytes = context.contentResolver.openInputStream(uri)?.use { stream ->
            val output = ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            var total = 0
            while (true) {
                val n = stream.read(buffer)
                if (n == -1) break
                total += n
                if(total > 12 * 1024 * 1024) throw InputFailure(InputFailureCode.IMAGE_TOO_LARGE,"Image exceeds the 12 MB import limit.")
                output.write(buffer,0,n)
            }
            output.toByteArray()
        } ?: throw InputFailure(InputFailureCode.PROVIDER_UNAVAILABLE,"The selected image could not be read. Select it again.")
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes,0,bytes.size,bounds)
        if(bounds.outWidth <= 0 || bounds.outHeight <= 0 || bounds.outMimeType != mime) throw InputFailure(InputFailureCode.DECODE_FAILED,"This image is malformed or its type does not match.")
        if(bounds.outWidth.toLong()*bounds.outHeight > 40_000_000 || maxOf(bounds.outWidth,bounds.outHeight) > 16000) throw InputFailure(InputFailureCode.IMAGE_TOO_LARGE,"Image dimensions exceed the import limit.")
        val orientation = bytes.inputStream().use { ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL) }
        var sample = 1
        while (maxOf(bounds.outWidth,bounds.outHeight)/sample > 2048) sample *= 2
        val decoded = BitmapFactory.decodeByteArray(bytes,0,bytes.size,BitmapFactory.Options().apply { inSampleSize=sample; inPreferredConfig=Bitmap.Config.ARGB_8888 }) ?: throw InputFailure(InputFailureCode.DECODE_FAILED,"Unable to decode image. Choose another screenshot.")
        var bitmap: Bitmap = decoded
        var recognizer: com.google.mlkit.vision.text.TextRecognizer? = null
        try {
            bitmap = ImageOrientation.apply(decoded,orientation)
            recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            // ML Kit Tasks are not coroutine-cancellable. Keep the bitmap alive until
            // native processing completes, then let the cancelled caller discard it.
            val task = recognizer.process(InputImage.fromBitmap(bitmap,0))
            val result = withContext(NonCancellable) { task.await() }
            val text = result.text
            val warning = when {
                text.isBlank() -> "No readable text was found. Replace the image or enter text manually."
                text.length < 20 -> "Very little text was found. Review it carefully."
                else -> "OCR can change a character in a link. Check and correct the extracted text before analysis."
            }
            return OcrDraft(text,warning,bitmap)
        } catch (e: Exception) {
            bitmap.recycle()
            throw e
        } finally {
            recognizer?.close()
        }
    }
}
