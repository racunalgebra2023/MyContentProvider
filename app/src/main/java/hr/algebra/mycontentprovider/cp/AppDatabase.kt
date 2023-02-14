package hr.algebra.mycontentprovider.cp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME =   "TasksDatabase"
const val DATABASE_VERSION = 1

const val TABLE_NAME = "TASK"
const val COLUMN_ID = "_ID"
const val COLUMN_NAME = "Name"
const val COLUMN_DESCRIPTION = "Description"

class AppDatabase private constructor( context : Context ) :
    SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION ) {

    override fun onCreate( db : SQLiteDatabase? ) {
        val SQL_CREATE = """CREATE TABLE "$TABLE_NAME" (
                            "$COLUMN_ID" INTEGER,
                            "$COLUMN_NAME" TEXT,
                            "$COLUMN_DESCRIPTION" TEXT,
                            PRIMARY KEY("$COLUMN_ID" AUTOINCREMENT)
                        );"""
        db?.execSQL( SQL_CREATE )

    }

    override fun onUpgrade( db: SQLiteDatabase?, oldVersion : Int, newVersion: Int ) {
        db?.execSQL( "DROP TABLE IF EXISTS $TABLE_NAME;" )
        onCreate( db )
    }

    companion object : SingletonHolder< AppDatabase, Context >( ::AppDatabase )
}