package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @ColumnInfo(name = "post_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) : Comparable<Post> {
    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "author")
    var author: String? = null

    @ColumnInfo(name = "published_at")
    var publishedAt: String? = null

    override fun compareTo(other: Post): Int {
        val compareInt = this.id.compareTo(other.id)
        return when {
            compareInt > 0 -> -1
            compareInt < 0 -> 1
            else -> compareInt
        }
    }

}

@Entity(tableName = "new_added_posts_table")
data class NewPost(
    @ColumnInfo(name = "post_id")
    @ForeignKey(
        entity = Post::class,
        parentColumns = ["post_id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.SET_DEFAULT
    ) @PrimaryKey(autoGenerate = false) val postId: Long
)