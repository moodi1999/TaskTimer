package com.example.ahmadreza.tasktimer

import android.provider.BaseColumns

/**
 * Created by ahmadreza on 8/23/18.
 */
class TaskContract {

    companion object {
        val TABLE_NAME = "Tasks"

    }

    object Columns {
        val _ID = BaseColumns._ID
        val TASKS_NAME = "Name"
        val TASKS_DESCRIPTION = "Description"
        val TASK_SORTORDER = "SortOrder"
    }
}