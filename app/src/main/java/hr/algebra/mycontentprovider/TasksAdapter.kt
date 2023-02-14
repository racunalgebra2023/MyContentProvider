package hr.algebra.mycontentprovider

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import hr.algebra.mycontentprovider.cp.TasksContract

class TasksAdapter( context : Context, cursor : Cursor, flags: Int )
                    : CursorAdapter( context, cursor, flags ) {

    private val layoutInflater : LayoutInflater =
                    context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ) as LayoutInflater

    override fun newView( context: Context?, cursor: Cursor?, parent: ViewGroup? ) : View {
        return layoutInflater.inflate( R.layout.task, parent, false )
    }

    override fun bindView( view: View?, context: Context?, cursor: Cursor? ) {
        view?.let {
            val tvName = it.findViewById< TextView >( R.id.tvTaskName )
            val tvDesc = view.findViewById< TextView >( R.id.tvTaskDescription )

            val taskName = cursor
                            ?.getString(
                                cursor.getColumnIndexOrThrow( TasksContract.Columns.TASK_NAME )
                            )
            val taskDesc = cursor
                ?.getString(
                    cursor.getColumnIndexOrThrow( TasksContract.Columns.TASK_DESCRIPTION )
                )

            tvName.text = taskName
            tvDesc.text = taskDesc
        }

    }
}