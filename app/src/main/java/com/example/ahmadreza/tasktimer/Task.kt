package com.example.ahmadreza.tasktimer

import java.io.Serializable

/**
 * Created by ahmadreza on 8/25/18.
 */
class Task(
        var m_Id: Long,
        val mName: String,
        val mDescription: String,
        val mSortOrder: String
) : Serializable{

    companion object {
        val serialVersionUID: Long = 20161120L
    }

    override fun toString(): String {

        return "//--  TASK === $m_Id  ---  $mName  ---  $$mDescription  ---  $mSortOrder --//"
    }
}