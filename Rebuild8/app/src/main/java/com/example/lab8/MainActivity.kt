package com.example.Rebuild8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button

    private val contacts = ArrayList<Contact>()

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val name = intent.getStringExtra("name").orEmpty()
                val phone = intent.getStringExtra("phone").orEmpty()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    contacts.add(Contact(name, phone))
                    myAdapter.notifyItemInserted(contacts.size - 1)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupEdgeToEdgeInserts()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)
    }

    private fun setupEdgeToEdgeInserts() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            myAdapter = MyAdapter(contacts)
            adapter = myAdapter
        }
    }

    private fun setupClickListeners() {
        btnAdd.setOnClickListener {
            val intent = Intent(this, SecActivity::class.java)
            startForResult.launch(intent)
        }
    }
}

data class Contact(
    val name: String,    // 姓名
    val phone: String    // 電話
)