package uz.mirsaidoff.testapp.model

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import uz.mirsaidoff.testapp.App
import uz.mirsaidoff.testapp.ui.posts.IProgressCtrl
import uz.mirsaidoff.testapp.ui.posts.PostsViewModel
import java.text.SimpleDateFormat
import java.util.*

class PostRepo private constructor(private val postDao: PostDao, private val newPostDao: NewPostDao) {

    companion object {
        @Volatile
        private var INSTANCE: PostRepo? = null

        fun getInstance(postDao: PostDao, newPostDao: NewPostDao): PostRepo =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PostRepo(postDao, newPostDao)
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
                if (result != null) {
                    vm.addPosts(result)
                    progressListener.onFinishLoading()
                } else {
                    vm.addPosts(listOf())
                    progressListener.onErrorLoading()
                }
            }
        }

        private class RemoveAllPostsAsync(private val postDao: PostDao, private val vm: PostsViewModel) :
            AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                postDao.deleteAllPosts()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                vm.setPosts()
            }
        }

        private class LoadEarlierPostsAsync(private val postDao: PostDao, private val vm: PostsViewModel) :
            AsyncTask<Long, Unit, List<Post>>() {
            override fun doInBackground(vararg params: Long?): List<Post> {
                return postDao.loadNextTenPostsFromGiven(params[0]!!)
            }

            override fun onPostExecute(result: List<Post>?) {
                super.onPostExecute(result)
                //todo add progress
                vm.addPosts(result!!)
            }
        }

        private class LoadAllNewPostsAsync(
            private val postDao: PostDao,
            private val newPostDao: NewPostDao,
            private val vm: PostsViewModel
        ) :
            AsyncTask<Unit, Unit, List<Post>>() {
            override fun doInBackground(vararg params: Unit?): List<Post> {
                val result = postDao.loadAllNewPosts()
                if (result.isNotEmpty())
                    newPostDao.removeAllNewPostIds(result[0].id)
                return result
            }

            override fun onPostExecute(result: List<Post>) {
                super.onPostExecute(result)
                vm.addPosts(result)
            }
        }
    }

    fun loadTenEarlierPosts(vm: PostsViewModel, id: Long) = LoadEarlierPostsAsync(postDao, vm).execute(id)

    fun loadLastTenPosts(vm: PostsViewModel, progressListener: IProgressCtrl) {
        progressListener.onStartLoading()
        AddPostAsync(postDao, vm, progressListener).execute()
    }

    fun loadAllNewPosts(vm: PostsViewModel) = LoadAllNewPostsAsync(postDao, newPostDao, vm).execute()

    @SuppressLint("SimpleDateFormat")
    fun insertPost() {
        val i = App.nextSequence()

        val post = Post()
        post.title = "Title $i"
        post.author = "Author $i"

        val format = SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss")
        post.publishedAt = format.format(Date())
        val id = postDao.insertPost(post)
        newPostDao.insertNewPostId(NewPost(id))

        Log.d("Repo", "Inserted post with id = $id")
    }

    fun removeAllPosts(vm: PostsViewModel) {
        RemoveAllPostsAsync(postDao, vm).execute()
    }
}