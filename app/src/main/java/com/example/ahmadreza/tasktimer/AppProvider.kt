package com.example.ahmadreza.tasktimer

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
    private val TAG = "AppProvider"
    private var mOpenHelper: AppDatabase? = null
    companion object {
        val CONTENT_AUTHORITY = "com.example.ahmadreza.tasktimer.provider"
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

        private fun buildUriMatcher(): UriMatcher {
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
        Log.i("AppProvider", "insert: called with uri = $uri")
        val match = sUriMatcher.match(uri)
        println("match is = ${match}")

        val db: SQLiteDatabase
        var returnUri: Uri? = null
        var recordId: Long? = null

        when (match) {
            TASKS -> {
                db = mOpenHelper!!.writableDatabase
                recordId = db.insert(TaskContract.TABLE_NAME, null, values)
                if (recordId >= 0) {
                    returnUri = TaskContract.buildTaskUri(recordId)
                } else {
                    throw android.database.SQLException("Faild to insert $uri")
                }
            }
            TIMINGS -> {
/*                db = mOpenHelper!!.writableDatabase
                recordId = db.insert(TaskContract.TABLE_NAME, null, values)
                if (recordId >= 0) {
                    returnUri = TaskContract.buildTaskUri(recordId)
                } else {
                    throw android.database.SQLException("Faild to insert $uri")
                }*/
            }

            else -> throw IllegalArgumentException("Unknown Uri $uri")
        }

        if (recordId!! >= 0) {
            // something was inserted
            Log.i("AppProvider", "insert: Setting notifychanged with $uri")
            context!!.contentResolver.notifyChange(uri!!, null)
        } else {
            Log.i("AppProvider", "insert: nothig insrted")
        }
        Log.i("AppProvider", "Exiting insert, returning $returnUri")
        return returnUri!!
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        try {
            Log.i("AppProvider", "query: called with URI  $uri")
            val match = sUriMatcher.match(uri)
            Log.i("AppProvider", "query: match is $match")

            val queryBuilder = SQLiteQueryBuilder()

            when (match) {
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
            //return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
            var cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
            Log.i("AppProvider", "query: row is returned cursor = ${cursor.count}")    // TODO: 8/30/18 Remove this line

            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }catch (e: Exception){
            return null
        }
    }

    override fun onCreate(): Boolean {
        mOpenHelper = AppDatabase.getInstance(context)
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.i("AppProvider", "update: called with uri : $uri")
        val match = sUriMatcher.match(uri)
        println("match = ${match}")
        var db: SQLiteDatabase?
        var count: Int

        var selectionCriteria: String

        when (match) {
            TASKS -> {
                db = mOpenHelper?.writableDatabase
                count = db!!.update(TaskContract.TABLE_NAME, values, selection, selectionArgs)
            }

            TASKS_ID -> {
                db = mOpenHelper!!.writableDatabase
                var taskId = TaskContract.getTaskId(uri!!)
                selectionCriteria = TaskContract.Columns._ID + " = " + taskId
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                count = db!!.update(TaskContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

        /*   TASKS -> {
               db = mOpenHelper?.writableDatabase
               count = db!!.update(TaskContract.TABLE_NAME, values, selection, selectionArgs)
           }

           TASKS_ID -> {
               db = mOpenHelper!!.writableDatabase
               var taskId = TaskContract.getTaskId(uri!!)
               selectionCriteria = TaskContract.Columns._ID + " = " + taskId
               if (selection != null && selection.isNotEmpty()){
                   selectionCriteria += " AND (" + selection + ")"
               }
               count = db!!.update(TaskContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
           }
           */

            else -> {
                throw IllegalArgumentException("Unknown uri $uri")
            }
        }

        if (count > 0){
            // something was deleted
            Log.i(TAG, "update: setting notifyChange with $uri")
            context.contentResolver.notifyChange(uri, null)
        }else{
            Log.i(TAG, "update: nothing deleted")
        }

        Log.i("AppProvider", "update() returned = ${count}")
        return count
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {

        Log.i("AppProvider", "Delete: called with uri : $uri")
        val match = sUriMatcher.match(uri)
        println("match = ${match}")
        var db: SQLiteDatabase? = null
        var count: Int

        var selectionCriteria: String

        when (match) {
            TASKS -> {
                db = mOpenHelper?.writableDatabase
                count = db!!.delete(TaskContract.TABLE_NAME, selection, selectionArgs)
            }

            TASKS_ID -> {
                db = mOpenHelper!!.writableDatabase
                var taskId = TaskContract.getTaskId(uri!!)
                selectionCriteria = TaskContract.Columns._ID + " = " + taskId
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND (" + selection + ")"
                }
                count = db!!.delete(TaskContract.TABLE_NAME, selectionCriteria, selectionArgs)
            }

        /*   TASKS -> {
               db = mOpenHelper?.writableDatabase
               count = db!!.delete(TaskContract.TABLE_NAME, selection, selectionArgs)
           }

           TASKS_ID -> {
               db = mOpenHelper!!.writableDatabase
               var taskId = TaskContract.getTaskId(uri!!)
               selectionCriteria = TaskContract.Columns._ID + " = " + taskId
               if (selection != null && selection.isNotEmpty()){
                   selectionCriteria += " AND (" + selection + ")"
               }
               count = db!!.delete(TaskContract.TABLE_NAME, selectionCriteria, selectionArgs)
           }
           */

            else -> {
                throw IllegalArgumentException("Unknown uri $uri")
            }
        }


        if (count > 0){
            // something was deleted 
            Log.i(TAG, "delete: setting notifyChange with $uri")
            context.contentResolver.notifyChange(uri, null)
        }else{
            Log.i(TAG, "delete: nothing deleted")
        }
        Log.i("AppProvider", "update() returned = ${count}")
        return count
    }

    override fun getType(uri: Uri?): String {
        Log.i("AppProvider", "query: called with URI  $uri")
        val match = sUriMatcher.match(uri)
        Log.i("AppProvider", "query: match is $match")
        return when (match) {

            TASKS -> TaskContract.CONTENT_TYPE

            TASKS_ID -> TaskContract.CONTENT_ITEM_TYPE

        /*    TIMINGS -> TaskContract.CONTENT_TYPE

            TIMINGs_ID ->TaskContract.CONTENT_ITEM_TYPE

            TASKS_DUARITION -> TaskContract.CONTENT_TYPE

            TASKS_DUARITION_ID -> TaskContract.CONTENT_ITEM_TYPE
    */
            else -> {
                throw IllegalArgumentException("Unknown URI: " + uri)
            }
        }

    }
}