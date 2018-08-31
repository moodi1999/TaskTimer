package com.example.ahmadreza.tasktimer

import android.app.DialogFragment
import android.os.Bundle

class AppDialog : DialogFragment(){
    private val TAG = "AppDialog"
    
    /**
     * The dialogs callback interface to notify of user selected result (deletion confirmed, etc..)
     **/
    
    interface DialogEvents{
        fun onPositiveDialogResult(dialogId: Int, args: Bundle)
        fun onNegativeDialogResult(dialogId: Int, args: Bundle)
        fun onDialogCancelled(dialogId: Int)
    }
}