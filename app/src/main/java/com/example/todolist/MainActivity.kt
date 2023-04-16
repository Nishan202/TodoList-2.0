package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<TodoModel>
    private val user = Firebase.auth.currentUser
    private lateinit var dbRef: DatabaseReference
    private var selectedTimeSpan: String = "All Time" //default is all time
    var dateStart: Long = 0
    var dateEnd: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUserData()

        val accountBtn: ImageButton = findViewById(R.id.btnAccount)
        accountBtn.setOnClickListener {
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
        }
    }

//    private fun getRangeDate(rangeType: Int) {
//        val currentDate = Date()
//        val cal: Calendar = Calendar.getInstance(TimeZone.getDefault())
//        cal.time = currentDate
//
//        val startDay = cal.getActualMinimum(rangeType) //get the first date of the month
//        cal.set(rangeType, startDay)
//        val startDate = cal.time
//        dateStart = startDate.time //convert to millis
//
//        val endDay = cal.getActualMaximum(rangeType) //get the last date of the month
//        cal.set(rangeType, endDay)
//        val endDate = cal.time
//        dateEnd= endDate.time //convert to millis
//    }

    private fun getUserData(){
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<TodoModel>()
        recyclerView.visibility = View.GONE

        val uid = user?.uid //get user id from database
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid)
        }
        //dbRef = FirebaseDatabase.getInstance().getReference("Todo")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                newArrayList.clear()
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val item = userSnapshot.getValue(TodoModel::class.java)
                        newArrayList.add(item!!)
//                        if (selectedTimeSpan == "All Time"){
//                            newArrayList.add(item!!)
//                        }else{
//                            if (item!!.date!! > dateStart-86400000 &&
//                                item.date!!<= dateEnd){
//                                newArrayList.add(item)
//                            }
//                        }
                    }
                    recyclerView.adapter = TodoAdapter(newArrayList)
                }
                val mAdapter = TodoAdapter(newArrayList)
                recyclerView.adapter = mAdapter
                mAdapter.setOnItemClickListener(object : TodoAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@MainActivity, TodoDetails::class.java)

                        // put extras
                        intent.putExtra("todoId", newArrayList[position].todoId)
                        intent.putExtra("title", newArrayList[position].titleTodo)
                        intent.putExtra("body", newArrayList[position].bodyTodo)
//                        intent.putExtra("date", newArrayList[position].date)
                        startActivity(intent)
                    }
                })
                recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                print("Listener was cancelled")
            }

        })
    }

    fun floating_button(view: View) {
        val intent = Intent(this, Insertion::class.java)
        startActivity(intent)
    }
}