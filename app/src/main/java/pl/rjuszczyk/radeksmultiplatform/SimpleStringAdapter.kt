package pl.rjuszczyk.radeksmultiplatform

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleStringAdapter : RecyclerView.Adapter<SimpleStringAdapter.SimpleStringViewHolder>() {

    private var data: List<String>? = null

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleStringViewHolder {
        return SimpleStringViewHolder(TextView(parent.context))
    }

    override fun getItemCount(): Int {
        return data.orEmpty().size
    }

    override fun onBindViewHolder(holder: SimpleStringViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    class SimpleStringViewHolder(val view: TextView): RecyclerView.ViewHolder(view) {

        fun bind(item: String) {
            view.text = item
        }
    }
}