package com.snowbear.sqlitetest

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.*
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    private val main_listView by lazy { findViewById<ListView>(R.id.main_listView) }
    private val recycler_list by lazy { findViewById<RecyclerView>(R.id.recycler_list) }
    private val but_input by lazy { findViewById<Button>(R.id.but_input) }
    private val but_clear by lazy { findViewById<Button>(R.id.but_clear) }
    private val edit_msg by lazy { findViewById<EditText>(R.id.edit_msg) }
    val list = mutableListOf<String>("aa", "bb", "cc", "dd", "ff")

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

        //=======================================================================

        //設定list資料
        val listData = ArrayList<String>()
        for (x in 0..10) {
            listData.add(x.toString())
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_list.layoutManager = layoutManager

        //呼叫recycler
        initReView()


        but_input.setOnClickListener {
            inputClick(myAdapter)
            list.add(edit_msg.text.toString())
            renewReView()
        }


        but_clear.setOnClickListener {
            clearAction(myAdapter)
            list.clear()
            renewReView()
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

    private fun clearAction(myAdapter: ArrayAdapter<String>) {
        val listener = object : OnClickListener {
            // dialog 為使用此監聽器的 AlertDialog
            // which　為 AlertDialog 上的按鈕代號
            // 例如：BUTTON_POSITIVE、BUTTON_NEGATIVE、BUTTON_NEUTRAL
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
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

    private fun inputClick(myAdapter: ArrayAdapter<String>) {
        val messenger = edit_msg.text.toString()
        //            myAdapter.add(messenger)  //加入資料, 從最尾端開始增加
        if (messenger != "") {
            myAdapter.insert(messenger, 0)  //加入資料, 自訂加入位置, 0表示第一位
        }
    }

    //將Adapter初始化, 指定給Recycler
    private fun initReView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_list)   //指定Recycler要顯示的地方

        // 線性排列，第2項為控制垂直或水平，第3項為設定元件排列順序是否顛倒 ，默認為垂直
        val linearLayoutManager = LinearLayoutManager(this)
        // 網格排列，第2項為控制一列顯示幾項
        val gridLayoutManager = GridLayoutManager(this, 2)
        // 瀑布排列，排列方式與網格相似，但格子尺寸可以隨元件大小變動
        // 第1項為控制一列顯示幾項，第2項為控制垂直或水平
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)

        val testAdapter = RecyclerAdapter(this, list)

        //recyclerView的顯示方式，可以自訂
        recyclerView.layoutManager = linearLayoutManager
//        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.layoutManager = staggeredGridLayoutManager

        //將Adapter內的資料，放進Recycler
        recyclerView.adapter = testAdapter
    }

    //更新Recycler資料
    private fun renewReView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_list)   //指定Recycler要顯示的地方
        val testAdapter = RecyclerAdapter(this, list)
        recyclerView.adapter = testAdapter
    }
}

// 設定recycler的adapter, 需要繼承RecyclerView.Adapter, 然後資料型態是自訂的ViewHolder
class RecyclerAdapter(
    private val context: Context,
    private val data: MutableList<String>
    ): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // ViewHolder為一個變數集合包 (還未指定物件)
    // ViewHolder類別要定義在recycler的類別之下才能使用
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var itemText: TextView
        lateinit var imageText: ImageView
    }

    //dp轉手機螢幕的px, xml用dp表示, 手機系統用px表示
    fun dpTopx(context: Context, dpValue: Float): Int {
        val salce = context.resources.displayMetrics.density
        return (dpValue * salce + 0.5f).toInt()
    }

    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt() //全局靜態
    fun Int.toPx_2(): Int = (this * context.resources.displayMetrics.density).toInt() //動態




    // 為ViewHolder中設定的變數指定要顯示的元件
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val viewHolder = ViewHolder(view)

        // 這定每個放進來的項目的高度, 不設定, 系統會直接一項佔整一個版面
//        view.layoutParams.height = dpTopx(parent.context,100f)
//        view.layoutParams.height = 80.toPx()
        view.layoutParams.height = 80.toPx_2()

        viewHolder.imageText = view.findViewById(R.id.img_flower)
        viewHolder.itemText = view.findViewById(R.id.tV_item)

        return viewHolder
    }

    // 為ViewHolder已指定的物件設定功能
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.itemText.text = model
        holder.itemText.setOnClickListener {    //設定item觸發功能
            Toast
                .makeText(it.context, model, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun getItemCount(): Int {  // 設定Recycler內item的數量
        return data.size
    }
}

