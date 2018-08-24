package com.example.ahmadreza.tasktimer

import android.content.ContentValues
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate: Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val projection = arrayOf(TaskContract.Columns._ID,
                TaskContract.Columns.TASKS_NAME,
                TaskContract.Columns.TASKS_DESCRIPTION,
                TaskContract.Columns.TASK_SORTORDER)

        val values = ContentValues()
        values.run {
            put(TaskContract.Columns.TASKS_NAME, "Content Provider")
            put(TaskContract.Columns.TASKS_DESCRIPTION, "record content provider video")
        }
        val count = contentResolver.update(TaskContract.buildTaskUri(4), values, null, null)
        Log.d("MainActivity", "onCreate: count = $count")
       /* values.run {
            put(TaskContract.Columns.TASKS_NAME, "New Task 1")
            put(TaskContract.Columns.TASKS_DESCRIPTION, "New Description 1")
            put(TaskContract.Columns.TASK_SORTORDER, 2)
        }
        val uri = contentResolver.insert(TaskContract.CONTENT_URI, values)
*/
        val cursor = contentResolver.query(TaskContract.CONTENT_URI,
                projection,
                null,
                null,
                TaskContract.Columns.TASK_SORTORDER)

        if (cursor != null) {
            Log.d("MainActivity", "onCreate: number of rows ${cursor.count}")
            while (cursor.moveToNext()) {
                for (i in 0 until cursor.columnCount) {
                    Log.d("MainActivity", "onCreate: ${cursor.getColumnName(i)}: ${cursor.getString(i)}")
                }
                Log.d("MainActivity", "onCreate: =================================")
            }

            cursor.close()
        } else {
            Log.d("MainActivity", "onCreate: cursor is null")
        }

//        val appDataBase = AppDatabase.getInstance(this)
//        val sqldb = appDataBase.readableDatabase


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
