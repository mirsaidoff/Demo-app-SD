package uz.mirsaidoff.testapp.model

import java.util.*

class PostRepo {

    companion object {
        //in order to keep rows' uniqueness
        private var SEQUENCE = 0L

        private fun nextSequence(): Long = ++SEQUENCE

        fun insertPost(postDao: PostDao) {
            val sequence = nextSequence()
            val post = Post()
            post.title = "Title $sequence"
            post.author = "Author $sequence"
            post.publishedAt = Date()
            postDao.insertPost(post)
        }
    }
}