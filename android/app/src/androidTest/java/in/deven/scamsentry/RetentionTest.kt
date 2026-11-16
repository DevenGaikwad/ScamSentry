package `in`.deven.scamsentry
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import `in`.deven.scamsentry.data.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RetentionTest {
    @Test fun retentionPrunesStorageTransactionally() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),HistoryDatabase::class.java).build()
        try {
            val dao=db.entries()
            for(i in 0..220) dao.insert(HistoryEntry("id-$i",i.toLong(),"checking","Redacted summary","fixture"))
            assertEquals(200,dao.count())
            dao.clear();assertEquals(0,dao.count())
        } finally { db.close() }
    }
}
