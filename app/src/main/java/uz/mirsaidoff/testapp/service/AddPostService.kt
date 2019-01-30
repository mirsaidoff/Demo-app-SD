package uz.mirsaidoff.testapp.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import uz.mirsaidoff.testapp.common.DELAY_TIME
import uz.mirsaidoff.testapp.model.NewPostDao
import uz.mirsaidoff.testapp.model.PostDao
import uz.mirsaidoff.testapp.model.PostRepo
import uz.mirsaidoff.testapp.model.PostsDb

class AddPostService : IntentService("Service") {

    private lateinit var handler: Handler
    private lateinit var postDao: PostDao
    private lateinit var newPostDao: NewPostDao

    override fun onCreate() {
        super.onCreate()
        val thread = HandlerThread("")
        thread.start()
        handler = Handler(thread.looper)

        postDao = PostsDb.getPostDao(this)
        newPostDao = PostsDb.getNewPostDao(this)
        Log.d(TAG, "onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        handler.postDelayed(InsertNewItemRunnable(postDao, newPostDao), DELAY_TIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

    }

    companion object {
        const val TAG = "AddPostService"
    }

    private inner class InsertNewItemRunnable(val postDao: PostDao, val newPostDao: NewPostDao) : Runnable {
        override fun run() {
            PostRepo.getInstance(postDao, newPostDao).insertPost()
            handler.postDelayed(this, DELAY_TIME)
        }
    }
}