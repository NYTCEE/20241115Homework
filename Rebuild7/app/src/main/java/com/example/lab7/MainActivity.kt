package com.example.rebuild7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 宣告元件
        val spinner: Spinner = findViewById(R.id.spinner)
        val listView: ListView = findViewById(R.id.listView)
        val gridView: GridView = findViewById(R.id.gridView)

        // 生成水果資訊
        val items = generateItems()

        // 建立 ArrayAdapter 物件，並傳入購買數量資訊
        val counts = items.mapIndexed { index, _ -> "${index + 1}個" }
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            counts
        )

        // 設定橫向顯示列數
        gridView.numColumns = 3

        // 建立 MyAdapter 並設置於 GridView 和 ListView
        val verticalAdapter = MyAdapter(this, items, R.layout.adapter_vertical)
        val horizontalAdapter = MyAdapter(this, items, R.layout.adapter_horizontal)

        gridView.adapter = verticalAdapter
        listView.adapter = horizontalAdapter
    }

    /**
     * 生成水果資訊的函式
     */
    private fun generateItems(): List<Item> {
        val priceRange = 10..100 // 價格範圍
        val itemList = mutableListOf<Item>()

        val images = resources.obtainTypedArray(R.array.image_list)
        for (index in 0 until images.length()) {
            val photo = images.getResourceId(index, 0)
            val name = "水果${index + 1}"
            val price = priceRange.random()

            itemList.add(Item(photo, name, price))
        }
        images.recycle() // 釋放資源

        return itemList
    }
}

/**
 * 資料類別：Item
 */
data class Item(
    val photo: Int,   // 圖片資源 ID
    val name: String, // 名稱
    val price: Int    // 價格
)
