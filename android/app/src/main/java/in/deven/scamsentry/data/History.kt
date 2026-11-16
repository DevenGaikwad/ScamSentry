package `in`.deven.scamsentry.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Only user-approved derived metadata is retained. No input, URL or screenshot fields. */
@Entity(tableName="history")
data class HistoryEntry(@PrimaryKey val id: String, val createdAt: Long, val category: String, val summary: String, val modelId: String, val policyVersion: String = "ordinal_v2", @ColumnInfo(defaultValue="'unknown'") val inputSource: String = "unknown")
@Dao
abstract class HistoryDao {
    @Query("SELECT * FROM history ORDER BY createdAt DESC, id DESC LIMIT 200") abstract fun observe(): Flow<List<HistoryEntry>>
    @Insert(onConflict=OnConflictStrategy.IGNORE) abstract suspend fun insertOnly(entry: HistoryEntry)
    @Query("DELETE FROM history WHERE id NOT IN (SELECT id FROM history ORDER BY createdAt DESC, id DESC LIMIT 200)") abstract suspend fun prune()
    @Query("SELECT COUNT(*) FROM history") abstract suspend fun count(): Int
    @Transaction open suspend fun insert(entry: HistoryEntry) { insertOnly(entry); prune() }
    @Query("DELETE FROM history WHERE id=:id") abstract suspend fun delete(id: String)
    @Query("DELETE FROM history") abstract suspend fun clear()
}
@Database(entities=[HistoryEntry::class], version=3, exportSchema=true)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun entries(): HistoryDao
    companion object {
        val MIGRATION_1_2=object: Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) { db.execSQL("ALTER TABLE history ADD COLUMN inputSource TEXT NOT NULL DEFAULT 'unknown'") }
        }
        val MIGRATION_2_3=object: Migration(2,3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM history WHERE id NOT IN (SELECT id FROM history ORDER BY createdAt DESC, id DESC LIMIT 200)")
            }
        }
        fun create(context: Context) = Room.databaseBuilder(context,HistoryDatabase::class.java,"history.db").addMigrations(MIGRATION_1_2,MIGRATION_2_3).build()
    }
}
