package uz.mirsaidoff.testapp.model

import android.annotation.SuppressLint
import android.util.Log
import uz.mirsaidoff.testapp.App
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
    }

    fun loadLastTenPosts(){

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
}