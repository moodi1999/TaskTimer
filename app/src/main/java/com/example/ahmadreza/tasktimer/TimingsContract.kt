package com.example.ahmadreza.tasktimer

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import com.example.ahmadreza.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY
import com.example.ahmadreza.tasktimer.AppProvider.Companion.CONTENT_AUTHORITY_URI

/**
 * Created by ahmadreza on 8/23/18.
 */
class TimingsContract {

    companion object {
        val TABLE_NAME = "Timings"

        /**
         * The URI to Access the Timings table
         * */
        val CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)
        val CONTENT_TYPE = "van.android.cursor.dir/vnd$CONTENT_AUTHORITY.$TABLE_NAME"
        val CONTENT_ITEM_TYPE = "van.android.cursor.item/vnd$CONTENT_AUTHORITY.$TABLE_NAME"


        public fun buildTimingUri(TimingId: Long): Uri{
            val app = ContentUris.withAppendedId(CONTENT_URI, TimingId)
            return app
        }

        public fun getTimingId(uri: Uri): Long {
            return ContentUris.parseId(uri)
        }
    }

    object Columns {
        val _ID = BaseColumns._ID
        val TIMINGS_TASK_ID = "TaskId"
        val TIMINGS_START_TIME = "StartTime"
        val TIMINGS_DURATION = "Duration"
    }
}