package com.example.ahmadreza.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.IllegalStateException

/**
 * Created by ahmadreza on 8/23/18.
 *
 * Basic Database class for application.
 * the only class that should use this is [AppProvider].
 */
class AppDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "TaskTimer.db"
        val DATABASE_VERSION = 1
        private var instance: AppDatabase? = null
        /**
         * Get an instance of the app's singleton database helper object
         *
         * @param [context] the content providers context
         * @return a SQLite database helper object
         *
         */
        fun getInstance(context: Context): AppDatabase {
            if (instance == null){
                println("AppDatabase.getInstance create new instance")
                Log.i("Appdatabade", "getInstance: start")
                instance = AppDatabase(context)
            }

            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {

        Log.i("AppDatabase", "onCreate: Start")
        var sSQL: String;
//        sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL,  Name TEXT NOT NULL, Descriprion TEXT, SortOrder INTEGER, CategoryID INTEGER);"

        sSQL = "CREATE TABLE " + TaskContract.TABLE_NAME + " (" +
                TaskContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                TaskContract.Columns.TASKS_NAME + " TEXT NOT NULL, " +
                TaskContract.Columns.TASKS_DESCRIPTION + " TEXT, " +
                TaskContract.Columns.TASK_SORTORDER + " INTEGER);"
        println("AppDatabase. $sSQL")
        db!!.execSQL(sSQL)
        Log.i("AppDatabase", "onCreate: End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("AppDatabase.onUpgrade starts")
        when(oldVersion){
            1 -> {
                //upgrade logic from version 1
            }
            else -> {
                throw IllegalStateException("onUpgrade() whith unknown newVersion" + newVersion)
            }
        }
    }
}