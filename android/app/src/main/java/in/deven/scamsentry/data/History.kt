package `in`.deven.scamsentry.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Only user-approved derived metadata is retained. No input, URL or screenshot fields. */
@Entity(tableName="history")
data class HistoryEntry(@PrimaryKey val id: String, val createdAt: Long, val category: String, val summary: String, val modelId: String, val policyVersion: String = "ordinal_v1", @ColumnInfo(defaultValue="'unknown'") val inputSource: String = "unknown")
@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY createdAt DESC LIMIT 200") fun observe(): Flow<List<HistoryEntry>>
    @Insert(onConflict=OnConflictStrategy.IGNORE) suspend fun insert(entry: HistoryEntry)
    @Query("DELETE FROM history WHERE id=:id") suspend fun delete(id: String)
    @Query("DELETE FROM history") suspend fun clear()
}
@Database(entities=[HistoryEntry::class], version=2, exportSchema=true)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun entries(): HistoryDao
    companion object {
        val MIGRATION_1_2=object: Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) { db.execSQL("ALTER TABLE history ADD COLUMN inputSource TEXT NOT NULL DEFAULT 'unknown'") }
        }
        fun create(context: Context) = Room.databaseBuilder(context,HistoryDatabase::class.java,"history.db").addMigrations(MIGRATION_1_2).build()
    }
}
