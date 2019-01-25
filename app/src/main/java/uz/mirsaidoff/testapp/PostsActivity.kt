package uz.mirsaidoff.testapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uz.mirsaidoff.testapp.model.PostDao
import uz.mirsaidoff.testapp.model.PostRepo
import uz.mirsaidoff.testapp.model.PostsDb
import uz.mirsaidoff.testapp.ui.posts.PostsFragment
import java.lang.ref.WeakReference

class PostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.posts_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PostsFragment.newInstance())
                .commitNow()
        }

        val db = PostsDb.getInstance(this)
        val postDao = db.getPostDao()

        //async task for populating the db
        PopulateAsync(postDao, this).execute()
    }

    fun startPostExecService() = startService(Intent(this, AddPostService::class.java))

    // --------------------------------------------------------------------------
    companion object {
        private class PopulateAsync(
            val postDao: PostDao,
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
                    PostRepo.insertPost(postDao)
                }
            }
        }
    }


}
