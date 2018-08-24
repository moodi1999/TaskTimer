package com.example.ahmadreza.tasktimer

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import com.example.ahmadreza.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY
import com.example.ahmadreza.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY_URI

/**
 * Created by ahmadreza on 8/23/18.
 */
class TaskContract {

    companion object {
        val TABLE_NAME = "Tasks"

        /**
         * The URI to Access the Tasks table
         * */
        val CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)
        val CONTENT_TYPE = "van.android.cursor.dir/vnd$CONTENT_AUTHORITY.$TABLE_NAME"
        val CONTENT_ITEM_TYPE = "van.android.cursor.item/vnd$CONTENT_AUTHORITY.$TABLE_NAME"

        fun buildTaskUri(taskId: Long): Uri{
            return ContentUris.withAppendedId(CONTENT_URI, taskId)
        }

        fun getTaskId(uri: Uri): Long {
            return ContentUris.parseId(uri)
        }
    }

    object Columns {
        val _ID = BaseColumns._ID
        val TASKS_NAME = "Name"
        val TASKS_DESCRIPTION = "Description"
        val TASK_SORTORDER = "SortOrder"
    }
}