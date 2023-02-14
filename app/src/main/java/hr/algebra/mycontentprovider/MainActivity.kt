package hr.algebra.mycontentprovider

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import hr.algebra.mycontentprovider.cp.TasksContract

class MainActivity : AppCompatActivity( ) {

    private lateinit var tvTaskID   : TextView
    private lateinit var etTaskName : EditText
    private lateinit var etTaskDesc : EditText
    private lateinit var bInsert    : Button
    private lateinit var bUpdate    : Button
    private lateinit var bDelete    : Button
    private lateinit var lvTasks    : ListView
    private lateinit var lvAdapter  : TasksAdapter

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )

        initWidgets( )
        populateData( )
        setupListeners( )
    }

    private fun setupListeners( ) {
        bInsert.setOnClickListener{
            val values = ContentValues( )
            values.put( TasksContract.Columns.TASK_NAME,        etTaskName.text.toString( ) )
            values.put( TasksContract.Columns.TASK_DESCRIPTION, etTaskDesc.text.toString( ) )
            contentResolver.insert( TasksContract.CONTENT_URI, values )
            refreshDataAndEmptyEditTexts( )
        }
        bUpdate.setOnClickListener{
            val taskId = tvTaskID.text.toString( ).toLong( )
            val values = ContentValues( )
            values.put( TasksContract.Columns.TASK_NAME,        etTaskName.text.toString( ) )
            values.put( TasksContract.Columns.TASK_DESCRIPTION, etTaskDesc.text.toString( ) )
            contentResolver.update( TasksContract.buildUriFromId( taskId ), values, null, null )
            refreshDataAndEmptyEditTexts( )
        }
        bDelete.setOnClickListener{
            val taskId = tvTaskID.text.toString( ).toLong( )
            contentResolver.delete( TasksContract.buildUriFromId( taskId ), null, null )
            refreshDataAndEmptyEditTexts( )
        }
        lvTasks.setOnItemClickListener { _, _, _, id ->
            val cursor = contentResolver.query( TasksContract.buildUriFromId( id.toLong( ) ), null, null, null, null )
            if( cursor?.moveToFirst( )==true ) {
                val taskId   = cursor?.getInt( cursor.getColumnIndexOrThrow( TasksContract.Columns.ID ) )
                val taskName = cursor?.getString( cursor.getColumnIndexOrThrow( TasksContract.Columns.TASK_NAME ) )
                val taskDesc = cursor?.getString( cursor.getColumnIndexOrThrow( TasksContract.Columns.TASK_DESCRIPTION ) )
                tvTaskID.text = "$taskId"
                etTaskName.setText( taskName )
                etTaskDesc.setText( taskDesc )
            }
            cursor?.close( )
        }
    }

    private fun populateData( ) {
        val cursor = contentResolver.query( TasksContract.CONTENT_URI, null, null, null, null )
        lvAdapter = cursor?.let { TasksAdapter( this, it, 0 ) }!!
        lvTasks.adapter = lvAdapter
/*
        cursor?.let {
            lvAdapter = TasksAdapter( this, it, 0 )
            lvTasks.adapter = lvAdapter
        }
*/
    }

    fun initWidgets( ) {
        tvTaskID   = findViewById( R.id.tvTaskID )
        etTaskName = findViewById( R.id.etTaskName )
        etTaskDesc = findViewById( R.id.etTaskDescription )
        bInsert    = findViewById( R.id.bSave )
        bUpdate    = findViewById( R.id.bEdit )
        bDelete    = findViewById( R.id.bDelete )
        lvTasks    = findViewById( R.id.lvTasks )
    }

    private fun refreshDataAndEmptyEditTexts( ) {
        val cursor = contentResolver.query( TasksContract.CONTENT_URI, null, null, null, null )
        lvAdapter.swapCursor( cursor )
        etTaskName.setText( "" )
        etTaskDesc.setText( "" )
    }
}