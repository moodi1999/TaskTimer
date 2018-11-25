package com.example.ahmadreza.tasktimer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.net.URL

class MainActivity : AppCompatActivity()
        , CursorRecyclerViewAdaptor.OnTaskClicklistener
        , AddEditActivityFragment.OnSavedClicked
        , AppDialog.DialogEvents {

    private enum class methodCall { SAVE, EDIT, OnCREATE, DIALOGcANCEL }

    private val TAG = "MainActivity"

    /**
     * whether or not the activity is in 2 pane mode
     * i.e running in lacndscape or a tablet
     */
    private var mTwoPane = false

    companion object {
        private const val DIALOG_ID_DELETE = 1
        private const val DIALOG_ID_CANCEL = 2
    }

    private var mDialog: AlertDialog? = null // moudle scope because we need to dismiss it in onStop
    // e.g when orientation changes to avoid memory leaks

    private var mCurrentTiming: Timing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "onCreate: Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /*    if (findViewById<FrameLayout>(R.id.task_detail_container) != null) {
                // the datail conteiner view will be peresent only in large screen layouts (res/values-sw600dp).
                // If this peresent, then activity shoulb be in two pane mode
                mTwoPane = true
            }*/

        mTwoPane = (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)

        Log.i(TAG, "onCreate: TwoPane is $mTwoPane ")

        checkTwoPane(methodCall.OnCREATE)
        // we need references to the contaners m so we can show or hide them as necessary
        // no need to cast them as we'r only calling a method that's available for all views

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menumain_setting -> true
            R.id.menumain_addTask -> {
                taskEditRequest(null)
            }
            R.id.menumain_showabout -> {
                showAboutDialog()
            }
            R.id.menumain_showDuration -> true
            R.id.menumain_generate -> true
            android.R.id.home -> {
                Log.i(TAG, "onOptionsItemSelected :: home peresed :: CALLED")
                val fragment = supportFragmentManager.findFragmentById(R.id.task_detail_container) as AddEditActivityFragment
                return if (fragment.canClose()) {
                    super.onOptionsItemSelected(item)
                } else {
                    showConfirmationDialog()
                    true // indicate we are handling this
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showAboutDialog() {
        val messageView = layoutInflater.inflate(R.layout.about, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setView(messageView)
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog?.dismiss()
            }
        }

        mDialog = builder.create()
        mDialog?.setCanceledOnTouchOutside(true)

        val tv = messageView.findViewById<TextView>(R.id.about_version)
        tv.text = "v ${BuildConfig.VERSION_NAME}"

        val link = messageView.findViewById<TextView>(R.id.about_url)
        link?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://" + (it as TextView).text.toString()
            intent.data = Uri.parse(url)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "no browser application found", Toast.LENGTH_SHORT).show();
            }
        }

        mDialog?.show()

    }

    private fun taskEditRequest(task: Task?) {
        Log.i("MainActivity", "taskEditRequest: Start")
        Log.i("MainActivity", "taskEditRequest: in wopane mode")
        val fragment = AddEditActivityFragment()

        val arguments = Bundle()
        arguments.putSerializable(Task::class.java.simpleName, task)
        fragment.arguments = arguments

        supportFragmentManager.beginTransaction()
                .replace(R.id.task_detail_container, fragment)
                .commit()
        checkTwoPane(methodCall.EDIT)
        Log.i(TAG, "taskEditRequest :::: FINISHED")
    }

    override fun onEditClick(task: Task) {
        taskEditRequest(task)
    }

    override fun onDeleteClick(task: Task) {
        Log.i(TAG, "onDeleteClick: starts")
        val dialog = AppDialog()
        val args = Bundle()
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE)
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldialog_message, task.m_Id, task.mName))
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_posetive_action)
        args.putLong("TaskId", task.m_Id)

        dialog.arguments = args
        dialog.show(fragmentManager, null)
    }

    @SuppressLint("SetTextI18n")
    override fun onTaskLongClick(task: Task) {
        Log.i(TAG, "onTaskLongClick: onTask Long clicked")
        if (mCurrentTiming != null) {
            if (task.m_Id == mCurrentTiming!!.mTask.m_Id) {
                // the current task was tapped a second time, so stop timing
                // TODO: 11/25/18 saveTiming(mcurrentTiming)
                mCurrentTiming = null
                current_task.text = getString(R.string.no_task_message)
            } else {
                // a new task is being timed, so stop the old one first
                // TODO: 11/25/18 saveTiming(mCurrentTiming)
                mCurrentTiming = Timing(task)
                current_task.text = "Timing ${mCurrentTiming!!.mTask.mName}"
            }
        } else {
            // no task being time start timing the new task
            mCurrentTiming = Timing(task)
            current_task.text = "Timing ${mCurrentTiming!!.mTask.mName}"
        }
    }

    override fun onSaveClicked() {
        Log.i(TAG, "onSaveClicked :::: Start")
        var fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.task_detail_container)
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            Log.i(TAG, "onSaveClicked: isif")
        }
        checkTwoPane(methodCall.SAVE)
    }

    override fun onPositiveDialogResult(dialogId: Int, args: Bundle) {
        Log.i(TAG, "onPositiveDialogResult: ")
        when (dialogId) {
            DIALOG_ID_DELETE -> {
                val taskId = args.getLong("TaskId")
                if (BuildConfig.DEBUG && taskId == 0L) throw AssertionError("task Id is zero")
                contentResolver.delete(TaskContract.buildTaskUri(taskId), null, null)
            }

            DIALOG_ID_CANCEL -> {
                // No action required
            }
        }

    }

    override fun onNegativeDialogResult(dialogId: Int, args: Bundle) {
        Log.i(TAG, "onNegativeDialogResult: ")
        when (dialogId) {
            DIALOG_ID_DELETE -> {
                // No action requaired
            }

            DIALOG_ID_CANCEL -> {
                val fmanager = supportFragmentManager
                val fragment = fmanager.findFragmentById(R.id.task_detail_container)
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                    if (mTwoPane) {
                        finish()
                    } else {
                        checkTwoPane(methodCall.DIALOGcANCEL)
                    }
                } else {
                    finish()
                }
            }
        }

    }

    override fun onDialogCancelled(dialogId: Int) {
        Log.i(TAG, "onDialogCancelled: ")
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed :::: Start")
        val fManager = supportFragmentManager
        val fragment: AddEditActivityFragment? = fManager.findFragmentById(R.id.task_detail_container) as? AddEditActivityFragment
        if (fragment == null || fragment.canClose()) {
            super.onBackPressed()
        } else {
            showConfirmationDialog()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog?.dismiss()
        }
    }

    override fun onAttachFragment(fragment: android.app.Fragment?) {
        Log.i(TAG, "onAttachFragment: called fragment")
        super.onAttachFragment(fragment)
    }

    private fun checkTwoPane(method: methodCall) {
        val addEditLayout = findViewById<View>(R.id.task_detail_container)
        val main = findViewById<View>(R.id.fragment2)
        when (method) {
            methodCall.OnCREATE -> {
                val manager = supportFragmentManager
                //If the AddeditActivity fragment exist , we are editing
                val editing = manager.findFragmentById(R.id.task_detail_container) != null
                Log.i(TAG, "onCreate: editing is $editing")

                if (mTwoPane) {
                    Log.i(TAG, "onCreate: twoPane mode")
                    main.visibility = View.VISIBLE
                    addEditLayout.visibility = View.VISIBLE
                } else if (editing) {
                    Log.i(TAG, "onCreate: single pane editing ")
                    //hide the left hand fragment , to make room for editing
                    main.visibility = View.GONE
                } else {
                    Log.i(TAG, "onCreate: not editing")
                    //show left hand fragment
                    main.visibility = View.VISIBLE
                    addEditLayout.visibility = View.GONE
                }
            }

            methodCall.EDIT -> {
                if (!mTwoPane) {
                    Log.i("MainActivity", "taskEditRequest: not twopane mode (phone)")
                    //hide the left hand fragment
                    main.visibility = View.GONE
                    addEditLayout.visibility = View.VISIBLE
                }
            }

            methodCall.SAVE -> {
                if (!mTwoPane) {
                    //we've just removed the editing fragment so hide the frame
                    addEditLayout.visibility = View.GONE
                    main.visibility = View.VISIBLE
                }
            }

            methodCall.DIALOGcANCEL -> {
                if (!mTwoPane) {
                    //we've just removed the editing fragment so hide the frame
                    addEditLayout.visibility = View.GONE
                    main.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showConfirmationDialog() {
        val dialog = AppDialog()
        val args = Bundle()
        args.run {
            putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL)
            putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDiag_message))
            putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption)
            putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDiag_negative_message)
        }

        dialog.arguments = args
        dialog.show(fragmentManager, null)
    }
}
