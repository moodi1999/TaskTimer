package com.example.ahmadreza.tasktimer

import android.database.Cursor
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.security.InvalidParameterException

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() , LoaderManager.LoaderCallbacks<Cursor>{

    companion object {
        val LoaderId = 0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(LoaderId, null, this)
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        Log.d("MainActivityFragment", "onCreateLoader: id = $id")
        val projection = arrayOf(TaskContract.Columns._ID, TaskContract.Columns.TASKS_NAME,
                TaskContract.Columns.TASKS_DESCRIPTION, TaskContract.Columns.TASK_SORTORDER)

        val sortOrder = TaskContract.Columns.TASK_SORTORDER + "," + TaskContract.Columns.TASKS_NAME

        when (p0){
            LoaderId -> {
                return CursorLoader(activity!!,
                        TaskContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder)
            }

            else -> {
                throw InvalidParameterException("MainActivity oncreateLoader called with invalid loader id $p0")
            }
        }
    }

    override fun onLoadFinished(p0: Loader<Cursor>, p1: Cursor?) {
        Log.e("MainActivityFragment", "onLoadFinished :::: Start")
        var count = -1

        if (p1 != null){
            while (p1.moveToNext()){
                for (i in 0 until p1.columnCount){
                    Log.d("MainActivityFragment", "onLoadFinished: ${p1.getColumnName(i)} : ${p1.getString(i)}")
                }
                Log.d("MainActivityFragment", "onLoadFinished: ===================")
            }
            count = p1.count
        }
        Log.d("MainActivityFragment", "onLoadFinished: count is $count")
    }

    override fun onLoaderReset(p0: Loader<Cursor>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}
