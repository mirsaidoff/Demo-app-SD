package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import uz.mirsaidoff.testapp.model.NewPostDao
import uz.mirsaidoff.testapp.model.Post

class PostsViewModel : ViewModel() {

    private val postsLiveData: MutableLiveData<MutableList<Post>> = MutableLiveData()

    fun setPosts() {
        val newList = arrayListOf<Post>()
        postsLiveData.postValue(newList)
    }

    fun loadNewPostCount(newPostDao: NewPostDao): LiveData<Long> {
        return newPostDao.loadNewPostCount()
    }

    fun addPosts(newPosts: List<Post>) {
        var existingPosts = postsLiveData.value
        if (existingPosts == null) {
            existingPosts = arrayListOf()
        }

        if (newPosts.isNotEmpty()) {
            existingPosts.addAll(newPosts)
            existingPosts.sort()
            postsLiveData.postValue(existingPosts)
        }
    }

    fun getPosts(): LiveData<MutableList<Post>> = postsLiveData
}
