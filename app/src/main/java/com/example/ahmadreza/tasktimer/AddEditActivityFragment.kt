package com.example.ahmadreza.tasktimer

import android.content.ContentValues
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

/**
 * A placeholder fragment containing a simple view.
 */
class AddEditActivityFragment : Fragment() {

    enum class FragmentEditMode { EDIT, ADD }

    private var mMode: FragmentEditMode? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("AddEditActivityFragment", "onCreateView: Start")
        val view = inflater.inflate(R.layout.fragment_add_edit, container, false)
        val edt_sort = view.addedit_sortorder
        val edt_name = view.addedit_name
        val edt_disc = view.addedit_desc

        val argument = activity!!.intent.extras // the line we'll change later

        var task: Task? = null
        if (argument != null) {
            Log.d("AddEditActivityFragment", "onCreateView: retriening task detail");

            task = argument.getSerializable(Task::class.java.simpleName) as Task
            if (task != null) {
                Log.d("AddEditActivityFragment", "onCreateView: TAsk detail found edditing...");
                edt_name.setText(task.mName)
                edt_disc.setText(task.mDescription)
                edt_sort.setText(task.mSortOrder)
                mMode = FragmentEditMode.EDIT
            } else {
                // NO Task, so we must be adding sa new task, and not editing an existing one
                mMode = FragmentEditMode.ADD
            }
        } else {
            task = null
            Log.d("AddEditActivityFragment", "onCreateView: No Arguments, adding new record");
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
                        Log.d("AddEditActivityFragment", "onCreateView: Updating task ");
                        contentres.update(TaskContract.buildTaskUri(task!!.m_Id), values, null, null)
                    }
                }

                FragmentEditMode.ADD -> {
                    if (edt_name.length() > 0) {
                        Log.d("AddEditActivityFragment", "onCreateView: adding new task");
                        values.run {
                            put(TaskContract.Columns.TASKS_NAME, edt_name.text!!.toString())
                            put(TaskContract.Columns.TASKS_DESCRIPTION, edt_disc.text!!.toString())
                            put(TaskContract.Columns.TASK_SORTORDER, edt_sort.text!!.toString())
                            contentres.insert(TaskContract.CONTENT_URI, values)
                        }
                    }
                }
            }

            Log.d("AddEditActivityFragment", "onCreateView: onclick : Done ei");
        }

        return view
    }


}
