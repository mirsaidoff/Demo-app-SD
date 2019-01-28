package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.posts_fragment.*
import kotlinx.android.synthetic.main.posts_fragment.view.*
import uz.mirsaidoff.testapp.R
import uz.mirsaidoff.testapp.model.Post

class PostsFragment : Fragment(), IProgressCtrl {

    companion object {
        fun newInstance() = PostsFragment()
    }

    private lateinit var viewModel: PostsViewModel
    private var listener: IPostFragmentCtrl? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IPostFragmentCtrl) listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.posts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initViews()
    }

    private fun initViews() {
        recycler_posts.layoutManager = LinearLayoutManager(context)
        val postAdapter = PostAdapter(arrayListOf(), context!!)
        recycler_posts.adapter = postAdapter

        btn_reload.setOnClickListener { listener?.onLoadPosts(this) }
        viewModel = ViewModelProviders.of(activity!!).get(PostsViewModel::class.java)
        viewModel.getPosts().observe(this, Observer {
            postAdapter.setItems(it!!)
        })

        postAdapter.setOnLastItemReachedListener(object : PostAdapter.ILastItemReachedListener {
            override fun onLastItemReached(post: Post) {
                //if post id == 0 then the last post has been reached
                if (post.id != 0L) {
                    listener?.onLoadNextTenPosts(lastPostId = post.id)
                }
            }
        })

        swipe_container.setOnRefreshListener {
            listener?.onLoadPosts(this)
        }
    }

    private fun initToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar!!.setDisplayShowTitleEnabled(false)
        //todo change:delete
        toolbar.tv_action_title.setOnClickListener {
            listener?.onClearAllPosts()
        }

        toolbar.navigationContentDescription = "Clear"

    }

    override fun onStartLoading() {
        progress.visibility = View.VISIBLE
        if (empty_container.visibility == View.VISIBLE) empty_container.visibility = View.GONE
    }

    override fun onFinishLoading() {
        progress.visibility = View.INVISIBLE
        if (swipe_container.visibility == View.INVISIBLE) {
            swipe_container.visibility = View.VISIBLE
        } else {
            swipe_container.isRefreshing = false
        }
    }
}
