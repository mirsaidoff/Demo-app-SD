package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.posts_fragment.*
import uz.mirsaidoff.testapp.R

class PostsFragment : Fragment() {

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

        recycler_posts.layoutManager = LinearLayoutManager(context)
        recycler_posts.adapter = PostAdapter(arrayListOf(), context!!)

        btn_reload.setOnClickListener { listener?.onLoadPosts() }
        viewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        viewModel.getPosts().observe(this, Observer {
            val postAdapter = recycler_posts.adapter as PostAdapter
            postAdapter.setItems(it ?: arrayListOf())
        })
    }

}
