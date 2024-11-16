package com.example.Rebuild8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edPhone: EditText
    private lateinit var btnSend: Button

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_PHONE = "phone"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setContentView(R.layout.activity_sec)

        initViews()
        setupEdgeToEdgeInsets()
        setupClickListeners()
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
    }

    private fun initViews() {
        edName = findViewById(R.id.edName)
        edPhone = findViewById(R.id.edPhone)
        btnSend = findViewById(R.id.btnSend)
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupClickListeners() {
        btnSend.setOnClickListener {
            validateAndSendData()
        }
    }

    private fun validateAndSendData() {
        val name = edName.text.toString().trim()
        val phone = edPhone.text.toString().trim()

        when {
            name.isEmpty() -> {
                showToast("請輸入姓名")
                edName.requestFocus()
            }
            phone.isEmpty() -> {
                showToast("請輸入電話")
                edPhone.requestFocus()
            }
            else -> {
                sendResult(name, phone)
            }
        }
    }

    private fun sendResult(name: String, phone: String) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_NAME, name)
            putExtra(EXTRA_PHONE, phone)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}