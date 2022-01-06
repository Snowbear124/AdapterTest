package com.snowbear.sqlitetest

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val main_listView by lazy { findViewById<ListView>(R.id.main_listView) }
    private val recycler_list by lazy { findViewById<RecyclerView>(R.id.recycler_list) }
    private val but_input by lazy { findViewById<Button>(R.id.but_input) }
    private val but_clear by lazy { findViewById<Button>(R.id.but_clear) }
    private val edit_msg by lazy { findViewById<EditText>(R.id.edit_msg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //設定Adapter的陣列表
        //simple_list_item_1是系統內建的名稱
        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)

        //加入項目至ArrayAdapter, addAll指一次可以加入很多項目
        myAdapter.addAll("測試項目1", "測試項目2")
        myAdapter.add("測試項目3")

        //將ArrayAdapter的資料放入ListView框框中
        main_listView.adapter = myAdapter

        //將ArrayAdapter的資料放進Recycler當中
//        recycler_list.adapter = myAdapter

        but_input.setOnClickListener {
            val messenger = edit_msg.text.toString()
//            myAdapter.add(messenger)  //加入資料, 從最尾端開始增加
            if(messenger != "") {
                myAdapter.insert(messenger, 0)  //加入資料, 自訂加入位置, 0表示第一位
            }
        }


        but_clear.setOnClickListener {
            val listener = object : OnClickListener {
                // dialog 為使用此監聽器的 AlertDialog
                // which　為 AlertDialog 上的按鈕代號
                // 例如：BUTTON_POSITIVE、BUTTON_NEGATIVE、BUTTON_NEUTRAL
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        BUTTON_POSITIVE -> myAdapter.clear()
                        BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }
            }
            AlertDialog
                .Builder(this)
                .setTitle("是否要全部清除")
                .setPositiveButton("yes", listener)
                .setNegativeButton("no", listener)
                .show()
        }

        // 當ListView中某個項目被點擊時, i為項目
        main_listView.setOnItemClickListener { adapterView, view, i, l ->
            val items = myAdapter.getItem(i)
            val listener = object : OnClickListener {
                // dialog 為使用此監聽器的 AlertDialog
                // which　為 AlertDialog 上的按鈕代號
                // 例如：BUTTON_POSITIVE、BUTTON_NEGATIVE、BUTTON_NEUTRAL
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        BUTTON_POSITIVE -> myAdapter.remove(items)
                        BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }
            }

            AlertDialog
                .Builder(this)
                .setMessage("要刪除${items}嗎?")
                .setPositiveButton("yes", listener)
                .setNegativeButton("no", listener)
                .show()
        }
    }
}

