package com.example.ahmadreza.tasktimer

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahmadreza.tasktimer.R.id.addedit_desc
import com.example.ahmadreza.tasktimer.R.id.addedit_sortorder
import kotlinx.android.synthetic.main.fragment_add_edit.*
import kotlinx.android.synthetic.main.fragment_add_edit.view.*
import java.io.Serializable

/**
 * A placeholder fragment containing a simple view.
 */
class AddEditActivityFragment : Fragment() {
    private val TAG = "AddEditActivityFragment"

    enum class FragmentEditMode { EDIT, ADD }

    private var mMode: FragmentEditMode? = null

    private var mSaveListener: OnSavedClicked? = null
    interface OnSavedClicked{
        fun onSaveClicked()
    }

    override fun onAttach(context: Context?) {
        Log.i(TAG, "onAttach :::: Start")
        super.onAttach(context)
        // Activities containing
        val acti = activity
        if (acti !is OnSavedClicked) {
            throw ClassCastException(acti!!.javaClass.simpleName + " \n it has to empelement OnSavedClicked interface")
        }
        mSaveListener = activity as OnSavedClicked
    }

    override fun onDetach() {
        Log.i(TAG, "onDetach :::: Start")
        super.onDetach()
        mSaveListener = null

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i("AddEditActivityFragment", "onCreateView: Start")
        val view = inflater.inflate(R.layout.fragment_add_edit, container, false)
        val edt_sort = view.addedit_sortorder
        val edt_name = view.addedit_name
        val edt_disc = view.addedit_desc

        //val argument = activity!!.intent.extras // FIXME: 8/31/18 change this line to recive data from fragment too
        val argument = arguments

        var task: Task? = null
        if (argument != null) {
            Log.i("AddEditActivityFragment", "onCreateView: retriening task detail");
            val serz: Serializable? = argument.getSerializable(Task::class.java.simpleName)
            if (argument.getSerializable(Task::class.java.simpleName) != null) {
                Log.i("AddEditActivityFragment", "onCreateView: TAsk detail found edditing...");
                task = serz as Task
                edt_name.setText(task.mName)
                edt_disc.setText(task.mDescription)
                edt_sort.setText(task.mSortOrder.toString())
                mMode = FragmentEditMode.EDIT
            } else {
                // NO Task, so we must be adding sa new task, and not editing an existing one
                mMode = FragmentEditMode.ADD
            }
        } else {
            task = null
            Log.i("AddEditActivityFragment", "onCreateView: No Arguments, adding new record");
            mMode = FragmentEditMode.ADD
        }
       
        view.button_save.setOnClickListener {
            // Update the database if at least one field has changed
            // - There's no need to hit the databade unless this has happend
            var so: Int? = null
            if (edt_sort.length() > 0) {
                so = edt_sort.text.toString().toInt()
            } else {
                so = 0
            }

            val contentres = activity!!.contentResolver
            val values = ContentValues()

            when (mMode) {
                FragmentEditMode.EDIT -> {
                    if (!edt_name.text.toString().equals(task!!.mName)) {
                        values.put(TaskContract.Columns.TASKS_NAME, edt_name.text.toString())

                    }
                    if (!edt_disc.text.toString().equals(task!!.mDescription)) {
                        values.put(TaskContract.Columns.TASKS_DESCRIPTION, edt_disc.text.toString())
                    }
                    if (so != task!!.mSortOrder) {
                        values.put(TaskContract.Columns.TASK_SORTORDER, so)
                    }
                    if (values.size() != 0) {
                        Log.i("AddEditActivityFragment", "onCreateView: Updating task ");
                        contentres.update(TaskContract.buildTaskUri(task!!.m_Id), values, null, null)
                    }
                }

                FragmentEditMode.ADD -> {
                    if (edt_name.length() > 0) {
                        Log.i("AddEditActivityFragment", "onCreateView: adding new task");
                        values.run {
                            put(TaskContract.Columns.TASKS_NAME, edt_name.text!!.toString())
                            put(TaskContract.Columns.TASKS_DESCRIPTION, edt_disc.text!!.toString())
                            put(TaskContract.Columns.TASK_SORTORDER, so)
                            contentres.insert(TaskContract.CONTENT_URI, this)
                        }
                    }
                }
            }

            Log.i("AddEditActivityFragment", "onCreateView: onclick : Done ei");

            if (mSaveListener != null) {
                mSaveListener!!.onSaveClicked()
            }
        }

        Log.i("AddEditActivityFragment", "onCreateView: Exiting");
        return view
    }

    fun canClose(): Boolean { // FIXME: 9/1/18
        return false
    }

}
