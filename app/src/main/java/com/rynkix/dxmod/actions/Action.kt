package com.rynkix.dxmod.actions

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update

@Entity
data class Action (
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "equation") val equation: String,
    @ColumnInfo(name = "tags") val tags: List<String>
)

class ActionTypeConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}

@Dao
interface ActionDao {
    @Query("SELECT * FROM `action`")
    fun getAll(): List<Action>

    @Query("SELECT * FROM `action` WHERE uid IN (:actionIds)")
    fun loadAllByIds(actionIds: IntArray): List<Action>

    @Insert
    fun insertAll(vararg actions: Action)

    @Update
    fun update(action: Action)

    @Delete
    fun delete(action: Action)

    @Query("DELETE FROM `action` WHERE uid = :uid")
    fun deleteById(uid: Int)
}