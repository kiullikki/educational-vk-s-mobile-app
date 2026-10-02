package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TEXT = "extra_text"
    }

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val padLeft = binding.second.paddingLeft
        val padTop = binding.second.paddingTop
        val padRight = binding.second.paddingRight
        val padBottom = binding.second.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.second) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                padLeft + systemBars.left,
                padTop + systemBars.top,
                padRight + systemBars.right,
                padBottom + systemBars.bottom
            )
            insets
        }

        setSupportActionBar(binding.toolbarSecond)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.textViewResult.text = intent.getStringExtra(EXTRA_TEXT) ?: ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
