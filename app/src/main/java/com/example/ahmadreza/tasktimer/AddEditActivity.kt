package com.example.ahmadreza.tasktimer

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity(), AddEditActivityFragment.OnSavedClicked, AppDialog.DialogEvents {

    companion object {
        private const val DIALOG_ID_CANCEL = 1
        private const val TAG = "AddEditActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        Log.i("AddEditActivity", "onCreate: Start")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (savedInstanceState == null){
        val manager = supportFragmentManager
//        if (manager.findFragmentById(R.id.fragment) == null){

            val fragment = AddEditActivityFragment()

            val args = intent.extras
            fragment.arguments = args

            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragment, fragment)
            transaction.commit()
        }
    }

    override fun onSaveClicked() {
        Log.i(TAG, "onSaveClicked: finished")
        finish()
    }

    override fun onBackPressed() {
        val fManager = supportFragmentManager
        val fragment: AddEditActivityFragment = fManager.findFragmentById(R.id.fragment) as AddEditActivityFragment
        if (fragment.canClose()) {
            super.onBackPressed()
        } else {
           showConfirmationDialog()
        }
    }

    override fun onPositiveDialogResult(dialogId: Int, args: Bundle) {
        Log.i(TAG, "onPositiveDialogResult :::: CALLED")
    }

    override fun onNegativeDialogResult(dialogId: Int, args: Bundle) {
        when (dialogId) {
            DIALOG_ID_CANCEL -> {
                finish()
            }
        }
    }

    override fun onDialogCancelled(dialogId: Int) {
        Log.i(TAG, "onDialogCancelled :::: CALLED")
        // no action
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
    
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                Log.i(TAG, "onOptionsItemSelected :: home peresed :: CALLED")
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment2) as AddEditActivityFragment
                return if (fragment.canClose()) {
                    super.onOptionsItemSelected(item)
                } else {
                    showConfirmationDialog()
                    true // indicate we are handling this
                }
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
