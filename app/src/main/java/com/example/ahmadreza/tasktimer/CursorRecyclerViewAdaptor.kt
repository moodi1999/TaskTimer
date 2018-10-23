package com.example.ahmadreza.tasktimer

import android.annotation.SuppressLint
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.task_list_item.view.*

internal class CursorRecyclerViewAdaptor(var cursor: Cursor?, val mlistener: OnTaskClicklistener?) : RecyclerView.Adapter<CursorRecyclerViewAdaptor.TaskViewHolder>() {

    private val TAG = "CursorRecyclerViewAd"

    interface OnTaskClicklistener{
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
    }

    init {
        Log.i("CursorRecyclerViewAdapt", "initt: ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): TaskViewHolder {
//        Log.i("CursorRecyclerViewAdapt", "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, i: Int) {
        Log.i("CursorRecyclerViewAdapt", "onBindViewHolder :::: Start")

        if (cursor == null || cursor?.count == 0) {
            Log.i("CursorRecyclerViewAdapt", "onBindViewHolder: providing instruction")
            holder.name.text = "Instruction"
            holder.desc.text = "Use the add button (+) in the toolbar above to create new task + " +
                    "\n\n Tasks with lower sort orders will be placed higher up the list"
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        } else {
            if (!cursor!!.moveToPosition(i)) {
                throw IllegalStateException("Couldnt move cursor to position $i")
            }

            val task = Task(cursor!!.getLong(cursor!!.getColumnIndex(TaskContract.Columns._ID)),
                    cursor!!.getString(cursor!!.getColumnIndex(TaskContract.Columns.TASKS_NAME)),
                    cursor!!.getString(cursor!!.getColumnIndex(TaskContract.Columns.TASKS_DESCRIPTION)), cursor!!.getInt(cursor!!.getColumnIndex(TaskContract.Columns.TASK_SORTORDER)))

            holder.name.text = task.mName
            holder.desc.text = task.mDescription
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE

            val buttonListener = View.OnClickListener{
//                Log.i(TAG, "onBindViewHolder :::: Start")

                when(it.id){
                    R.id.tli_edit -> {
                        if (mlistener != null){
                            mlistener.onEditClick(task)
                        }
                    }
                    R.id.tli_delete ->{
                        if (mlistener != null){
                            mlistener.onDeleteClick(task)
                        }
                    }
                    else ->{
                        Log.i(TAG, "onBindViewHolder: onclick")
                    }
                }

//                Log.i(TAG, "onBindViewHolder: button with id ${it.id} clicked")
//                Log.i(TAG, "onBindViewHolder: task name is ${task.mName}")
            }

            holder.editButton.setOnClickListener(buttonListener)
            holder.deleteButton.setOnClickListener(buttonListener)
        }
    }

    override fun getItemCount(): Int {
        return if (cursor == null || cursor!!.count == 0) 1 else cursor!!.count
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
        if (cursor == newcursor) {
            return null
        }

        val oldCursor = cursor
        cursor = newcursor
        if (newcursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged()
        } else {
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

}