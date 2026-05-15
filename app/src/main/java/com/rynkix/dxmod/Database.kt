package com.rynkix.dxmod

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.rynkix.dxmod.actions.Action
import com.rynkix.dxmod.actions.ActionDao
import com.rynkix.dxmod.actions.ActionTypeConverter

@Database(entities = [Action::class], version = 1)
@TypeConverters(ActionTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDao
}
