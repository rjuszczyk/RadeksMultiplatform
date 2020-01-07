package pl.rjuszczyk.radeksmultiplatform

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.rjuszczyk.radeksmultiplatform.network.Todo

class TodosAdapter : RecyclerView.Adapter<TodosAdapter.TodosViewHolder>() {

    private var data: List<Todo>? = null

    fun setData(data: List<Todo>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        return TodosViewHolder(TextView(parent.context))
    }

    override fun getItemCount(): Int {
        return data.orEmpty().size
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    class TodosViewHolder(val view: TextView): RecyclerView.ViewHolder(view) {

        fun bind(todo: Todo) {
            view.text = todo.title
        }
    }
}