package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uz.mirsaidoff.testapp.App
import uz.mirsaidoff.testapp.R
import uz.mirsaidoff.testapp.common.PREF_NAME
import uz.mirsaidoff.testapp.common.SEQUENCE_KEY
import uz.mirsaidoff.testapp.model.NewPostDao
import uz.mirsaidoff.testapp.model.PostDao
import uz.mirsaidoff.testapp.model.PostRepo
import uz.mirsaidoff.testapp.model.PostsDb
import uz.mirsaidoff.testapp.service.AddPostService
import java.lang.ref.WeakReference

class PostsActivity : AppCompatActivity(), IPostFragmentCtrl {

    private lateinit var postDao: PostDao
    private lateinit var newPostDao: NewPostDao
    private lateinit var vm: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.posts_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostsFragment.newInstance())
                .commitNow()
        }

        postDao = PostsDb.getPostDao(this)
        newPostDao = PostsDb.getNewPostDao(this)
        vm = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        //async task for populating the db
        PopulateAsync(postDao, newPostDao, this).execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        //save last sequence before living
        val sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putLong(SEQUENCE_KEY, App.nextSequence())
            .apply()
    }

    fun startPostExecService() {
        startService(Intent(this, AddPostService::class.java))
    }

    override fun onLoadPosts(progressListener: IProgressCtrl) {
        PostRepo.getInstance(postDao, newPostDao).loadLastTenPosts(vm, progressListener)
    }

    //loads next ten posts before given id
    override fun onLoadNextTenPosts(lastPostId: Long) {
        PostRepo.getInstance(postDao, newPostDao).loadTenEarlierPosts(vm, id = lastPostId)
    }

    override fun onLoadNewPosts() {
        PostRepo.getInstance(postDao, newPostDao).loadAllNewPosts(vm)
    }

    override fun onClearAllPosts() {
        PostRepo.getInstance(postDao, newPostDao).removeAllPosts(vm)
        //sets the sequence to 0
        App.clearSequence()
    }

    // --------------------------------------------------------------------------
    companion object {
        private class PopulateAsync(
            val postDao: PostDao,
            val newPostDao: NewPostDao,
            postsActivity: PostsActivity
        ) : AsyncTask<Unit, Unit, Unit>() {
            private val weakActivity = WeakReference<PostsActivity>(postsActivity)

            override fun doInBackground(vararg params: Unit?) {
                populate(postDao)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                //after adding 100 dummy data to the db, start the service which keeps adding
                // 5 extra posts every second in the background
                weakActivity.get()?.startPostExecService()
            }

            //populates the database with 100 dummy data
            private fun populate(postDao: PostDao) {
                for (i in 1..100) {
                    PostRepo.getInstance(postDao, newPostDao).insertPost()
                }
            }
        }
    }


}
