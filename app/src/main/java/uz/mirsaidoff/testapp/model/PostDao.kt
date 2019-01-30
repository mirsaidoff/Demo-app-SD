package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface PostDao {

    @Query(
        """
        SELECT * FROM post_table t
        ORDER BY t.post_id DESC
        LIMIT 10"""
    )
    fun loadLastTenPosts(): List<Post>

    @Query(
        """
        SELECT * FROM post_table t
        WHERE t.post_id<:id
        ORDER BY t.post_id DESC
        LIMIT 10"""
    )
    fun loadNextTenPostsFromGiven(id: Long): List<Post>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPost(posts: Post): Long

    @Query("DELETE FROM post_table")
    fun deleteAllPosts()

//    @Query(
//        """
//        SELECT pt.* FROM post_table pt
//        INNER JOIN new_added_posts_table npt
//        ON pt.post_id = npt.post_id
//        ORDER BY pt.post_id DESC"""
//    )

    @Query(
        """
        SELECT * FROM post_table t
        WHERE t.post_id IN(
            SELECT nt.post_id FROM new_added_posts_table nt)
        ORDER BY t.post_id DESC"""
    )
    fun loadAllNewPosts(): List<Post>
}