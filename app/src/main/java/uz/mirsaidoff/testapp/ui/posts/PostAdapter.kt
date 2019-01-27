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

class PostAdapter(private val items: MutableList<Post>,
                  context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val diffUtilCallback = PostsDiffUtilCallback(arrayListOf(), arrayListOf())
    private var lastItemReachedListener: ILastItemReachedListener? = null

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

        //if last item is reached while scrolling, do what callback says
        if (pos == itemCount - 1) {
            lastItemReachedListener?.onLastItemReached(post)
        }
    }

    fun setOnLastItemReachedListener(lastItemCallback: ILastItemReachedListener) {
        this.lastItemReachedListener = lastItemCallback
    }

    fun setItems(newItems: MutableList<Post>) {
        diffUtilCallback.setOldItems(items)
        diffUtilCallback.setNewItems(newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tv_title
        val tvBody: TextView = itemView.tv_body
        val tvDate: TextView = itemView.tv_date
    }

    inner class PostsDiffUtilCallback(private var newItems: MutableList<Post>,
                                      private var oldItems: MutableList<Post>) : DiffUtil.Callback() {

        fun setNewItems(newItems: MutableList<Post>) {
            this.newItems = newItems
        }

        fun setOldItems(oldItems: MutableList<Post>) {
            this.oldItems = oldItems
        }

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = oldItems[oldPos].id == newItems[newPos].id

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        //todo generate {@Object.equals}
        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = oldItems[oldPos] == newItems[newPos]

    }

    interface ILastItemReachedListener {
        fun onLastItemReached(post: Post)
    }
}