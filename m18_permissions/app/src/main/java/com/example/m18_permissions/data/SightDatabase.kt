package com.example.m18_permissions.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(entities = [Sight::class], version = 1)
abstract class SightDatabase : RoomDatabase() {
    abstract fun sightDao(): SightDao
}

@Dao
interface SightDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun savePhotoInfo(sight: Sight)

    @Query("SELECT * FROM sights")
    fun getPhotoList(): Flow<List<Sight>>

}

@Entity(tableName = "sights")
data class Sight(
    @PrimaryKey
    @ColumnInfo(name = "photoUri")
    val photoUri: String,
    @ColumnInfo(name = "date")
    val date: String
)
