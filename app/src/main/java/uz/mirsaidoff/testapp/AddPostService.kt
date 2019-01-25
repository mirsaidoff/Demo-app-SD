package uz.mirsaidoff.testapp

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.util.Log
import uz.mirsaidoff.testapp.model.PostDao
import uz.mirsaidoff.testapp.model.PostRepo

class AddPostService(private val postDao: PostDao) : IntentService(AddPostService::class.java.name) {

    private val handler = Handler()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        handler.postDelayed(InsertNewItemRunnable(postDao), DELAY_TIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")

    }

    companion object {
        private const val DELAY_TIME = 1_000L / 5
        const val TAG = "AddPostService"
    }

    private inner class InsertNewItemRunnable(val postDao: PostDao) : Runnable {
        override fun run() {
            PostRepo.insertPost(postDao)
            handler.postDelayed(this, DELAY_TIME)
        }
    }
}