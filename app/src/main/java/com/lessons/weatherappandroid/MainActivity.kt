package com.lessons.weatherappandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.lessons.weatherappandroid.ui.main.MainFragmentJava
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.activityScope
import org.koin.core.KoinExperimentalAPI


fun View.snackBarError(string: String) {
    var sb = Snackbar.make(
        this, "${resources.getString(R.string.error)} : $string",
        Snackbar.LENGTH_LONG
    )
    sb.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
    sb.show()
}

class MainActivity : AppCompatActivity() {

    @KoinExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory(activityScope())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace<MainFragmentJava>(R.id.container)
                .commitNow()
        }
    }
}