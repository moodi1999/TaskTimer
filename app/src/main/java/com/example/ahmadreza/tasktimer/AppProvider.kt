package com.example.ahmadreza.tasktimer

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 * Created by ahmadreza on 8/23/18.
 *
 * Provider for the TaskTimer app.
 *
 * This is the only class that knows about [AppDatabase]
 *
 */

class AppProvider : ContentProvider() {

    private var mOpenHelper: AppDatabase? = null

    companion object {
        val CONTENT_AUTHORITY = "com.example.ahmadreza.tasktimer"
        val CONTENT_AUTHORITY_URI = Uri.parse("content://$CONTENT_AUTHORITY")
        val sUriMatcher = buildUriMatcher()

        private val TASKS = 100
        private val TASKS_ID = 101

        private val TIMINGS = 200
        private val TIMINGs_ID = 201

        /*
        private val TASKS_TIMINGS = 300
        private val TASKS_TIMINGD_ID = 301
        */

        private val TASKS_DUARITION = 400
        private val TASKS_DUARITION_ID = 401

        private fun buildUriMatcher(): UriMatcher{
            val matcher = UriMatcher(UriMatcher.NO_MATCH)

            matcher.run {
                // eg . content://com.example.ahmadreza.tasktimer.provider/Tasks
                addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME, TASKS)
                // eg . content://com.example.ahmadreza.tasktimer.provider/Tasks/8
                addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME + "/#", TASKS_ID)

//                addURI(CONTENT_AUTHORITY, TimingsContrract.TABLE_NAME, TIMINGS)
//                addURI(CONTENT_AUTHORITY, TimingsContrract.TABLE_NAME + "/#", TIMINGs_ID)
//
//                addURI(CONTENT_AUTHORITY, DuaritionContract.TABLE_NAME, TASKS_DUARITION)
//                addURI(CONTENT_AUTHORITY, DuaritionContract.TABLE_NAME + "/#", TASKS_DUARITION_ID)
            }

            return matcher
        }
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        Log.d("AppProvider", "query: called with URI  $uri")
        val match = sUriMatcher.match(uri)
        Log.d("AppProvider", "query: match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when(match){
            TASKS -> queryBuilder.tables = TaskContract.TABLE_NAME

            TASKS_ID -> {
                queryBuilder.tables = TaskContract.TABLE_NAME
                val taskId = TaskContract.getTaskId(uri!!)
                queryBuilder.appendWhere(TaskContract.Columns._ID + " = " + taskId)
            }

       /*     TIMINGS -> queryBuilder.tables = TimingsContrract.TABLE_NAME
            TIMINGs_ID -> {
                queryBuilder.tables = TimingsContrract.TABLE_NAME
                val timingId = TimingsContrract.getTimingId(uri)
                queryBuilder.appendWhere(TimingsContrract.Columns._ID + " = " + timingId)
            }

            TASKS_DUARITION -> queryBuilder.tables = DuaritionContract.TABLE_NAME
            TASKS_DUARITION_ID -> {
                queryBuilder.tables = DuaritionContract.TABLE_NAME
                val duaritionId = DuaritionContract.getDuaritionId(uri)
                queryBuilder.appendWhere(DuaritionContract.Columns._ID + " = " + duaritionId)
            }*/

            else -> {
                throw IllegalArgumentException("Unknown URI: " + uri)
            }
        }

        val db = mOpenHelper?.readableDatabase
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun onCreate(): Boolean {
        mOpenHelper = AppDatabase.getInstance(context)
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}