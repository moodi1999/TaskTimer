package com.example.ahmadreza.tasktimer

import android.util.Log
import java.io.Serializable
import java.util.*

/**
 * Simple timing object
 * Sets its start time when created, and calculates how long since creation,
 * when setDuration is called
 */
class Timing : Serializable{
    private val TAG = "Timing"

    private var m_Id:Long = 0
    lateinit var mTask: Task
    private var mStartTime: Long = 0
    private var mDuration: Long = 0

    constructor(mTask: Task) {
        this.mTask = mTask
        // Initialise the start time to now and the duration to zero for a new object.
        mStartTime = Date().time / 1000 // we are only tracking, whole seconds, not
        mDuration = 0
    }

    fun setDuration() {
        mDuration = (Date().time / 1000) - mStartTime // working in seconds, not milliseconds
        Log.i(TAG, "setDuration: ${mTask.m_Id} - start time: $mStartTime | Duration: $mDuration")
        
    }

    companion object {
        private val serialVersionUID = 20181120L
    }
}