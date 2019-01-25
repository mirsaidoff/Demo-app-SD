package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PostDao {

    @Query("""SELECT * FROM post_table""")
    fun loadAllPosts()

    @Insert
    fun insertPost(posts: Post)

    @Query("DELETE FROM post_table")
    fun deleteAllPosts()
}