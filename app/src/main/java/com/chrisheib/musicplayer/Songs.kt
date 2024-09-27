package com.chrisheib.musicplayer

import androidx.room.*

@Entity
data class Songs(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "path") val path: String?,
    @ColumnInfo(name = "play") val play: Int?
)

@Dao
interface SongsDao {
    @Query("SELECT * FROM Songs")
    fun getAll(): List<Songs>

    @Query("SELECT * FROM Songs WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Songs>

    //@Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
    //        "last_name LIKE :last LIMIT 1")
    //fun findByName(first: String, last: String): Songs

    @Insert
    fun insertAll(vararg users: Songs)

    @Delete
    fun delete(user: Songs)
}

@Database(entities = arrayOf(Songs::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao
}