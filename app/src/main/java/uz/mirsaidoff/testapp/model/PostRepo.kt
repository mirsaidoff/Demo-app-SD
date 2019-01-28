package uz.mirsaidoff.testapp.model

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import uz.mirsaidoff.testapp.App
import uz.mirsaidoff.testapp.ui.posts.IProgressCtrl
import uz.mirsaidoff.testapp.ui.posts.PostsViewModel
import java.text.SimpleDateFormat
import java.util.*

class PostRepo private constructor(private val postDao: PostDao) {

    companion object {
        @Volatile
        private var INSTANCE: PostRepo? = null

        fun getInstance(postDao: PostDao): PostRepo =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: PostRepo(postDao)
                }

        private class AddPostAsync(
                private val postDao: PostDao,
                private val vm: PostsViewModel,
                private val progressListener: IProgressCtrl
        ) : AsyncTask<Unit, Unit, List<Post>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                vm.setPosts()
            }

            override fun doInBackground(vararg params: Unit?): List<Post>? {
                return postDao.loadLastTenPosts()
            }

            override fun onPostExecute(result: List<Post>?) {
                super.onPostExecute(result)
                vm.addPosts(result ?: listOf())
                progressListener.onFinishLoading()
            }
        }

        private class RemoveAllPostsAsync(private val postDao: PostDao, private val vm: PostsViewModel) : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                postDao.deleteAllPosts()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                vm.setPosts()
            }
        }

        private class LoadEarlierPostsAsync(private val postDao: PostDao, private val vm: PostsViewModel) : AsyncTask<Long, Unit, List<Post>>() {
            override fun doInBackground(vararg params: Long?): List<Post> {
                return postDao.loadNextTenPostsFromGiven(params[0]!!)
            }

            override fun onPostExecute(result: List<Post>?) {
                super.onPostExecute(result)
                vm.addPosts(result!!)
            }
        }
    }

    fun loadTenEarlierPosts(vm: PostsViewModel, id: Long) = LoadEarlierPostsAsync(postDao, vm).execute(id)

    fun loadLastTenPosts(vm: PostsViewModel, progressListener: IProgressCtrl) {
        progressListener.onStartLoading()
        AddPostAsync(postDao, vm, progressListener).execute()
    }

    @SuppressLint("SimpleDateFormat")
    fun insertPost() {
        val i = App.nextSequence()

        val post = Post()
        post.title = "Title $i"
        post.author = "Author $i"

        val format = SimpleDateFormat("MMMMM dd, yyyy HH:mm")
        post.publishedAt = format.format(Date())
        postDao.insertPost(post)

        Log.d("Repo", "Inserted $post")
    }

    fun removeAllPosts(vm: PostsViewModel) {
        RemoveAllPostsAsync(postDao, vm).execute()
    }
}