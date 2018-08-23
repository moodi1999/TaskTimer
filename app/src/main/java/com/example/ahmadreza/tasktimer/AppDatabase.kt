package com.example.ahmadreza.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ahmadreza on 8/23/18.
 *
 * Basic Database class for application.
 * the only class that should use this as Appprovider.
 */
class AppDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "TaskTimer.db"
        val DATABASE_VERSION = 1
        private var instance: AppDatabase? = null
    }

    /**
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content providers context
     * @return a SQLite database helper object
     *
     */
    fun getInstance(context: Context): AppDatabase {
        if (instance == null){
            println("AppDatabase.getInstance create new instance")
            instance = AppDatabase(context)
        }

        return instance!!
    }

    override fun onCreate(db: SQLiteDatabase?) {
        println("AppDatabase.onCreate start")
        var sSQL: String;
//        sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL,  Name TEXT NOT NULL, Descriprion TEXT, SortOrder INTEGER, CategoryID INTEGER);"

        sSQL = "CREATE TABLE " + TaskContract.TABLE_NAME + "(" +
                TaskContract.Columns._ID +
                TaskContract.Columns.TASKS_NAME +
                TaskContract.Columns.TASKS_DESCRIPTION +
                TaskContract.Columns.TASK_SORTORDER
        println("AppDatabase. $sSQL")
        db!!.execSQL(sSQL)
        println("AppDatabase.onCreate End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}