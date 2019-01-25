package uz.mirsaidoff.testapp.ui.posts

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.post_row.view.*
import uz.mirsaidoff.testapp.R
import uz.mirsaidoff.testapp.model.Post

class PostAdapter(val items: ArrayList<Post>,
                  context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(vg: ViewGroup, pos: Int): ViewHolder {
        val view = inflater.inflate(R.layout.post_row, vg, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
        val post = items[pos]
        with(vh) {
            tvTitle.text = post.title
            tvBody.text = post.author
            tvDate.text = post.publishedAt
        }
    }

    fun setItems(newItems: ArrayList<Post>) {
        val callback = PostsDiffUtilCallback(newItems, items)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tv_title
        val tvBody: TextView = itemView.tv_body
        val tvDate: TextView = itemView.tv_date
    }

    inner class PostsDiffUtilCallback(private val newItems: ArrayList<Post>,
                                      private val oldItems: ArrayList<Post>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = oldItems[oldPos].id == newItems[newPos].id

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        //todo generate {@Object.equals}
        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = oldItems[oldPos] == newItems[newPos]

    }
}