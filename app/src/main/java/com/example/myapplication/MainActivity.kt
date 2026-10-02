package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val padLeft = binding.main.paddingLeft
        val padTop = binding.main.paddingTop
        val padRight = binding.main.paddingRight
        val padBottom = binding.main.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                padLeft + systemBars.left,
                padTop + systemBars.top,
                padRight + systemBars.right,
                padBottom + systemBars.bottom
            )
            insets
        }

        binding.editText.doOnTextChanged { _, _, _, _ ->
            binding.textField.error = null
        }

        binding.buttonOpenSecond.setOnClickListener {
            val text = inputText() ?: return@setOnClickListener
            val intent = Intent(this, SecondActivity::class.java)
                .putExtra(SecondActivity.EXTRA_TEXT, text)
            startActivity(intent)
        }

        binding.buttonCall.setOnClickListener {
            val number = inputText() ?: return@setOnClickListener
            if (!number.matches(Regex("^\\+?[0-9()\\-\\s]{3,}$"))) {
                showInputError("Введите корректный номер телефона")
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(intent)
        }

        binding.buttonShare.setOnClickListener {
            val text = inputText() ?: return@setOnClickListener
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(sendIntent, "Поделиться через…"))
        }
    }

    private fun inputText(): String? {
        val text = binding.editText.text.toString().trim()
        if (text.isEmpty()) {
            showInputError("Поле не должно быть пустым")
            return null
        }
        return text
    }

    private fun showInputError(message: String) {
        binding.textField.error = message
        binding.editText.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT)
    }
}
