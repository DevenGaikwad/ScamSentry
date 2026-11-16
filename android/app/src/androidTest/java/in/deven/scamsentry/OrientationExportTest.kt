package `in`.deven.scamsentry
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import `in`.deven.scamsentry.data.ImageOrientation
import `in`.deven.scamsentry.data.ReportExport
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import androidx.test.core.app.ApplicationProvider
import android.content.Context

@RunWith(AndroidJUnit4::class)
class OrientationExportTest {
    @Test fun allEightExifOrientationsPreservePixelLayout() {
        val order=listOf(listOf(1,2,3,4,5,6),listOf(2,1,4,3,6,5),listOf(6,5,4,3,2,1),listOf(5,6,3,4,1,2),listOf(1,3,5,2,4,6),listOf(5,3,1,6,4,2),listOf(6,4,2,5,3,1),listOf(2,4,6,1,3,5))
        for(o in 1..8) {
            val b=Bitmap.createBitmap(2,3,Bitmap.Config.ARGB_8888)
            (1..6).forEachIndexed { i,v -> b.setPixel(i%2,i/2,0xff000000.toInt()+v*0x111111) }
            val transformed=ImageOrientation.apply(b,o);val actual=mutableListOf<Int>()
            for(y in 0 until transformed.height)for(x in 0 until transformed.width) actual.add((transformed.getPixel(x,y) and 0xff)/0x11)
            assertEquals("orientation $o",order[o-1],actual);transformed.recycle()
        }
    }
    @Test fun secondExportKeepsFirstGrantFileAndPrunesExpiredFiles() {
        val context=ApplicationProvider.getApplicationContext<Context>();val root=File(context.cacheDir,"export-test").apply { mkdirs() }
        try {
            val first=ReportExport.prepareFile(root,"First redacted summary")
            val second=ReportExport.prepareFile(root,"Second redacted summary")
            assertNotEquals(first.name,second.name);assertTrue(first.isFile);assertTrue(second.isFile)
            first.setLastModified(1)
            ReportExport.prepareFile(root,"Third summary")
            assertFalse(first.exists());assertTrue(second.exists())
        } finally { root.deleteRecursively() }
    }
}
