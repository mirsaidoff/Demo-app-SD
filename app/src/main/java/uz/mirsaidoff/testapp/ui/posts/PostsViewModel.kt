package uz.mirsaidoff.testapp.ui.posts

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import uz.mirsaidoff.testapp.model.Post

class PostsViewModel : ViewModel() {

    private val postsLiveData: MutableLiveData<MutableList<Post>> = MutableLiveData()
}
