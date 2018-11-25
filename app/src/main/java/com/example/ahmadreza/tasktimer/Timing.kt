package com.example.ahmadreza.tasktimer

import java.io.Serializable

/**
 * Simple timing object
 * Sets its start time when created, and calculates how long since creation,
 * when setDuration is called
 */
class Timing : Serializable{

    companion object {
        private val serialVersionUID = 20181120L
    }
}