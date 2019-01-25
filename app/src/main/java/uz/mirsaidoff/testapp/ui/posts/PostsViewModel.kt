package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import uz.mirsaidoff.testapp.model.Post

class PostsViewModel : ViewModel() {

    private val postsLiveData: MutableLiveData<ArrayList<Post>> = MutableLiveData()

    fun setPosts(newPosts: List<Post>) {
        var existingPosts = postsLiveData.value
        if (existingPosts == null) {
            existingPosts = arrayListOf()
        }
        existingPosts.addAll(newPosts)
        postsLiveData.postValue(existingPosts)
    }

    fun getPosts(): LiveData<ArrayList<Post>> = postsLiveData
}
