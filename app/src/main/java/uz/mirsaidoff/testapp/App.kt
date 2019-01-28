package uz.mirsaidoff.testapp

import android.app.Application
import android.content.Context
import uz.mirsaidoff.testapp.common.PREF_NAME
import uz.mirsaidoff.testapp.common.SEQUENCE_KEY

class App : Application() {

    companion object {
        @Volatile
        private var sequence: Long = 0L
            @Synchronized set(value) {
                field = value
            }

        fun nextSequence() = sequence++
        fun clearSequence() {
            sequence = 0L
        }
    }

    override fun onCreate() {
        super.onCreate()
        val sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastSequence = sp.getLong(SEQUENCE_KEY, 0L)
        sequence = lastSequence
    }

}