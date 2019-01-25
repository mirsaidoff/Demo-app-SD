package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PostDao {

    @Query("""
        SELECT * FROM post_table t
        ORDER BY t.post_id DESC
        LIMIT 10""")
    fun loadLastTenPosts(): List<Post>

    @Query("""
        SELECT * FROM post_table t
        WHERE t.post_id<:id
        ORDER BY t.post_id DESC
        LIMIT 10""")
    fun loadNextTenPostsFromGiven(id: Long): List<Post>

    @Insert
    fun insertPost(posts: Post)

    @Query("DELETE FROM post_table")
    fun deleteAllPosts()
}