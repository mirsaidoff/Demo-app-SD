package uz.mirsaidoff.testapp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "post_table")
data class Post(
    @ColumnInfo(name = "post_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {
    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "author")
    var author: String? = null

    @ColumnInfo(name = "published_at")
    var publishedAt: String? = null
}