package uz.mirsaidoff.testapp.ui.posts

interface IProgressCtrl {

    fun onStartLoading()

    fun onFinishLoading()

    fun onErrorLoading()
}