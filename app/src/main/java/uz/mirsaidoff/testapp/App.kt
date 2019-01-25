package uz.mirsaidoff.testapp

import android.app.Application
import android.content.Context
import uz.mirsaidoff.testapp.common.PREF_NAME
import uz.mirsaidoff.testapp.common.SEQUENCE_KEY

class App : Application() {

    companion object {
        private var sequence = 0L
        fun nextSequence() = sequence++
    }

    override fun onCreate() {
        super.onCreate()
        val sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastSequence = sp.getLong(SEQUENCE_KEY, 0L)
        sequence = lastSequence
    }

}