package hr.algebra.mycontentprovider.cp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

private const val TASKS    = 100
private const val TASKS_ID = 101

class AppProvider : ContentProvider( ) {

    private val TAG = "AppProvider"
    private val uriMatcher by lazy { buildUriMatcher( ) }

    private fun buildUriMatcher( ) : UriMatcher {

        val matcher = UriMatcher( UriMatcher.NO_MATCH )

        matcher.addURI(
            TasksContract.CONTENT_AUTHORITY,
            TasksContract.TABLE_NAME,
            TASKS
        )

        matcher.addURI(
            TasksContract.CONTENT_AUTHORITY,
            "${TasksContract.TABLE_NAME}/#",
            TASKS_ID
        )

        return matcher

    }

    override fun onCreate( ): Boolean {
        Log.i( TAG, "onCreate(): starts" )
        return true
    }

    override fun getType( uri: Uri ): String? {
        return when( uriMatcher.match( uri ) ) {
            TASKS    -> TasksContract.CONTENT_TYPE
            TASKS_ID -> TasksContract.CONTENT_ITEM_TYPE
            else -> throw java.lang.IllegalArgumentException( "Unknown Uri: $uri" )
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        val match = uriMatcher.match( uri )
        val queryBuilder = SQLiteQueryBuilder( )

        when( match ) {
            TASKS -> queryBuilder.tables = TasksContract.TABLE_NAME
            TASKS_ID -> {
                val taskId = TasksContract.getId( uri )
                queryBuilder.tables = TasksContract.TABLE_NAME
                queryBuilder.appendWhere( "${TasksContract.Columns.ID} = " )
                queryBuilder.appendWhereEscapeString( "$taskId" )
            }
            else -> throw IllegalArgumentException( "Unknown Uri: $uri" )
        }
        val db = context?.let{ AppDatabase.getInstance( it ).readableDatabase }
        val cursor = queryBuilder.query(
                                    db,
                                    projection,
                                    selection,
                                    selectionArgs,
                                    null, null, sortOrder  )

        return cursor

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = uriMatcher.match( uri )

        when( match ) {
            TASKS -> {
                val db = context?.let{ AppDatabase.getInstance( it ).writableDatabase }
                val recordId = db?.insert( TasksContract.TABLE_NAME, null, values ) ?: 0L
                if( recordId==-1L )
                    throw SQLException( "Failed to insert..." )
                return TasksContract.buildUriFromId( recordId!! )
            }
            else -> throw IllegalArgumentException( "Unknown Uri: $uri" )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        val match = uriMatcher.match( uri )
        val queryBuilder = SQLiteQueryBuilder( )
        var count : Int? = -1

        when( match ) {
            TASKS -> {
                val db = context?.let{ AppDatabase.getInstance( it ).writableDatabase }
                count = db?.delete( TasksContract.TABLE_NAME, selection, selectionArgs )
            }
            TASKS_ID -> {
                val db = context?.let{ AppDatabase.getInstance( it ).writableDatabase }
                val taskId = TasksContract.getId( uri )
                var selectionCriteria = " ${TasksContract.Columns.ID}=$taskId"
                if( selection!=null && selection.isNotEmpty() ) {
                    selectionCriteria += " AND ($selection)"
                }
                count = db?.delete( TasksContract.TABLE_NAME, selectionCriteria, selectionArgs )
            }
            else -> throw IllegalArgumentException( "Unknown Uri: $uri" )
        }
        return count ?: 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}