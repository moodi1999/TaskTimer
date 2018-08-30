package com.example.ahmadreza.tasktimer

import android.database.Cursor
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

    internal var mAdaptor: CursorRecyclerViewAdaptor? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("MainActivityFragment", "onActivityCreated: created")
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(LoaderId, null, this)
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        Log.d("MainActivityFragment", "onCreateLoader: id = $id")
        val projection = arrayOf(TaskContract.Columns._ID, TaskContract.Columns.TASKS_NAME,
                TaskContract.Columns.TASKS_DESCRIPTION, TaskContract.Columns.TASK_SORTORDER)

        val sortOrder = TaskContract.Columns.TASK_SORTORDER + "," + TaskContract.Columns.TASKS_NAME + " COLLATE NOCASE"

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
        mAdaptor!!.swapCursor(p1!!)
        var count = mAdaptor!!.itemCount
        Log.d("MainActivityFragment", "onLoadFinished: count is $count")
    }

    override fun onLoaderReset(p0: Loader<Cursor>) {
        Log.d("MainActivityFragment", "onLoaderReset: start")
        mAdaptor!!.swapCursor(null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val activ = activity as CursorRecyclerViewAdaptor.OnTaskClicklistener
        mAdaptor = CursorRecyclerViewAdaptor(null, activ)
        recyclerView.adapter = mAdaptor

        Log.d("MainActivityFragment", "onCreateView: returning ")
        return view
    }
}
