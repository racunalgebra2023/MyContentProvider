package hr.algebra.mycontentprovider.cp

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns


object TasksContract {

    val CONTENT_AUTHORITY = "hr.algebra.mycontentprovider"
    val CONTENT_AUTHORITY_URI = Uri.parse( "content://$CONTENT_AUTHORITY" )
    val TABLE_NAME = "Tasks"

    /*
     * Uri za pristup tablici Tasks
     */
    val CONTENT_URI = Uri.withAppendedPath(
        CONTENT_AUTHORITY_URI,
        TABLE_NAME
    )

    val CONTENT_TYPE =      "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object Columns {
        val ID               = BaseColumns._ID
        val TASK_NAME        = "Name"
        val TASK_DESCRIPTION = "Description"
    }

    fun getId( uri : Uri ) : Long {
        return ContentUris.parseId( uri )
    }

    fun buildUriFromId( id : Long ) : Uri {
        return ContentUris.withAppendedId( CONTENT_URI, id )
    }
}