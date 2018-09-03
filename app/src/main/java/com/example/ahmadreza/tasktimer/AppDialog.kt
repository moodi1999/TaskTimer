package com.example.ahmadreza.tasktimer

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahmadreza.tasktimer.R.id.message

class AppDialog : DialogFragment() {
    private val TAG = "AppDialog"
    var mDialogEvents: DialogEvents? = null

    companion object {
        val DIALOG_ID = "id"
        val DIALOG_MESSAGE = "message"
        val DIALOG_POSITIVE_RID = "positive_rid"
        val DIALOG_NEGATIVE_RID = "negative_rid"
    }

    /**
     * The dialogs callback interface to notify of user selected result (deletion confirmed, etc..)
     **/
    interface DialogEvents{
        fun onPositiveDialogResult(dialogId: Int, args: Bundle)
        fun onNegativeDialogResult(dialogId: Int, args: Bundle)
        fun onDialogCancelled(dialogId: Int)
    }

    override fun onAttach(activity: Activity?) {
        Log.i(TAG, "onAttach :::: Start")
        super.onAttach(activity)

        mDialogEvents = activity as? DialogEvents ?: throw ClassCastException("not impelements")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i(TAG, "onCreateDialog :::: Start")

        val builder = AlertDialog.Builder(activity)
        val arguments = arguments
        var dialogId = 0
        var messageString = ""
        var positiveStringId = 0
        var negativeStringId = 0

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID)
            messageString = arguments.getString(DIALOG_MESSAGE)

            if(dialogId == 0 || messageString == null){
                throw IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not peresent in the bundle")
            }
            positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID)
            if (positiveStringId == 0){
                positiveStringId = R.string.ok
            }
            negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID)
            if (negativeStringId == 0){
                negativeStringId = R.string.cancel
            }
        }else{
            throw IllegalArgumentException("Must pass DIALOG_MESSAGE and DIALOG_ID in the Bundle")
        }

        builder.setMessage(messageString)
                .setPositiveButton(positiveStringId) {
                    dialog, which -> mDialogEvents?.onPositiveDialogResult(dialogId, arguments)
                }
                .setNegativeButton(negativeStringId) { dialog, which ->
                    mDialogEvents?.onNegativeDialogResult(dialogId, arguments)
                }

        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface?) {
        Log.i(TAG, "onCancel :::: Start")
        if (mDialogEvents != null){
            val dialogId = arguments.getInt(DIALOG_ID)
            mDialogEvents!!.onDialogCancelled(dialogId)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        Log.i(TAG, "onDismiss :::: Start")
        super.onDismiss(dialog)
    }

    override fun onDetach() {
        Log.i(TAG, "onDetach: entering")
        super.onDetach()

        // Reset the activie callback interface, because we don't have an activity any longer.
        mDialogEvents = null
    }

}