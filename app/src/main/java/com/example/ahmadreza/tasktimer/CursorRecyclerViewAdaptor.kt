package com.example.ahmadreza.tasktimer

import android.annotation.SuppressLint
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.task_list_item.view.*

internal class CursorRecyclerViewAdaptor(var cursor: Cursor?) : RecyclerView.Adapter<CursorRecyclerViewAdaptor.TaskViewHolder>() {

    init {
        Log.d("CursorRecyclerViewAdapt", "initt: ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): TaskViewHolder {
        Log.d("CursorRecyclerViewAdapt", "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, i: Int) {
        Log.e("CursorRecyclerViewAdapt", "onBindViewHolder :::: Start")

        if (cursor == null || cursor?.count == 0){
            Log.d("CursorRecyclerViewAdapt", "onBindViewHolder: providing instruction")
            holder.name.text = "Instruction"
            holder.desc.text = "Use the add button (+) in the toolbar above to create new task + " +
                    "\n\n Tasks with lower sort orders woll be placed higher up the list"
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }else{
            if (!cursor!!.moveToPosition(i)){
                throw IllegalStateException("Couldnt move cursor to position $i")
            }
            holder.name.text = cursor!!.getString(cursor!!.getColumnIndex(TaskContract.Columns.TASKS_NAME))
            holder.desc.text = cursor!!.getString(cursor!!.getColumnIndex(TaskContract.Columns.TASKS_DESCRIPTION))
            holder.editButton.visibility = View.VISIBLE //TODO add on click listener
            holder.deleteButton.visibility = View.VISIBLE // TODO add on click listener
        }
    }

    override fun getItemCount(): Int {
        return if(cursor == null || cursor!!.count == 0) 1 else cursor!!.count
    }

    /**
     * swap in new Cursor , returning the cursor
     * the returnd old cursor is <em>not</em> close
     *
     * @param cursor the new cursor to be used
     * @return Rturns the perviesly set Cursor , or null if there wasn't one.
     * If the givdm new Cursor is same instance as the previously set
     * Cursor, null is also returned.
     * */
    fun swapCursor(newcursor: Cursor?): Cursor? {
        if (cursor == newcursor){
            return null
        }

        val oldCursor = cursor
        cursor = newcursor
        if (newcursor != null){
            // notify the observers about the new cursor
            notifyDataSetChanged()
        }else{
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, itemCount)
        }

        return oldCursor
    }
    
    internal class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tli_name
        val desc = itemView.tli_disceription
        val editButton = itemView.tli_edit
        val deleteButton = itemView.tli_delete
    }

    companion object {

        private val TAG = "CursorRecyclerViewAdapt"
    }
}