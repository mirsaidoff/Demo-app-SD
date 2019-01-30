package uz.mirsaidoff.testapp.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface NewPostDao {

    @Insert
    fun insertNewPostId(newPostId: NewPost)

    @Query("SELECT COUNT(post_id) FROM new_added_posts_table")
    fun loadNewPostCount(): LiveData<Long>

    @Query(
        """DELETE FROM new_added_posts_table
                 WHERE post_id<=:lastId"""
    )
    fun removeAllNewPostIds(lastId: Long)
}