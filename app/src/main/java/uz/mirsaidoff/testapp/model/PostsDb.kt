package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Post::class], version = 1)
abstract class PostsDb : RoomDatabase() {

    abstract fun getPostDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: PostsDb? = null

        fun getInstance(context: Context): PostsDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context, PostsDb::class.java, "posts_db.sqlite")
            .build()

    }
}