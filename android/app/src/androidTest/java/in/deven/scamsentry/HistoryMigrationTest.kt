package `in`.deven.scamsentry

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import `in`.deven.scamsentry.data.HistoryDatabase
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryMigrationTest {
    @get:Rule val helper=MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),HistoryDatabase::class.java)
    @Test fun summariesSurviveWithUnknownLegacyInputSource() {
        helper.createDatabase("migration_fixture",1).apply {
            execSQL("INSERT INTO history(id,createdAt,category,summary,modelId,policyVersion) VALUES ('fixture',1,'checking','Fictional summary','old_model','old_policy')")
            close()
        }
        val db=helper.runMigrationsAndValidate("migration_fixture",2,true,HistoryDatabase.MIGRATION_1_2)
        db.query("SELECT summary,modelId,policyVersion,inputSource FROM history WHERE id='fixture'").use {
            assertTrue(it.moveToFirst())
            assertEquals("Fictional summary",it.getString(0));assertEquals("old_model",it.getString(1));assertEquals("old_policy",it.getString(2));assertEquals("unknown",it.getString(3))
        }
        db.execSQL("DELETE FROM history")
        db.query("SELECT COUNT(*) FROM history").use { assertTrue(it.moveToFirst());assertEquals(0,it.getInt(0)) }
        db.close()
    }
}
