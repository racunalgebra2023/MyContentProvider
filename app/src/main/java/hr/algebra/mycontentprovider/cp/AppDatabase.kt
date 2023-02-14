package hr.algebra.mycontentprovider.cp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME    =   "TasksDatabase"
const val DATABASE_VERSION = 2
const val TABLE_NAME_OLD   = "TASK"

class AppDatabase private constructor( context : Context ) :
    SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION ) {

    override fun onCreate( db : SQLiteDatabase? ) {
        val SQL_CREATE = """CREATE TABLE "${TasksContract.TABLE_NAME}" (
                            "${TasksContract.Columns.ID}" INTEGER,
                            "${TasksContract.Columns.TASK_NAME}" TEXT,
                            "${TasksContract.Columns.TASK_DESCRIPTION}" TEXT,
                            PRIMARY KEY("${TasksContract.Columns.ID}" AUTOINCREMENT)
                        );"""
        db?.execSQL( SQL_CREATE )

    }

    override fun onUpgrade( db: SQLiteDatabase?, oldVersion : Int, newVersion: Int ) {
        if( newVersion==2 && oldVersion==1 )
            db?.execSQL( "DROP TABLE IF EXISTS $TABLE_NAME_OLD;" )
        onCreate( db )
    }

    companion object : SingletonHolder< AppDatabase, Context >( ::AppDatabase )
}