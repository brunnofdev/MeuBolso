// Crie na pasta: model/AppDatabase.kt
package com.brunnofdev.meubolso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.MovimentacaoDao
import com.brunnofdev.meubolso.util.Converters

@Database(entities = [Movimentacao::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movimentacaoDao(): MovimentacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meubolso_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}