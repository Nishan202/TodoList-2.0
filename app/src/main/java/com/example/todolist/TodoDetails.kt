package com.example.todolist

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class TodoDetails : AppCompatActivity() {
    private lateinit var tvTitleDetails: TextView
    private lateinit var tvNoteDetails: TextView
    private lateinit var tvDateDetails: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        //---back button---
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }

        //--update data--
//        val updateData: Button = findViewById(R.id.updateButton)
//        updateData.setOnClickListener {
//            openUpdateDialog(
//                intent.getStringExtra("title").toString()
//            )
//        }

        initView() //call method for initialized each ui item
        deleteData()
        openUpdateDialog()
    }

    private fun deleteData() {
        val deleteData: ImageButton = findViewById(R.id.deleteData)
        val alertBox = AlertDialog.Builder(this)
        deleteData.setOnClickListener {
            alertBox.setTitle("Are you sure?")
            alertBox.setMessage("Do you want to delete this todo list?")
            alertBox.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                deleteRecord(
                    intent.getStringExtra("todoId").toString()
                )
            }
            alertBox.setNegativeButton("No") { _: DialogInterface, _: Int -> }
            alertBox.show()
        }
    }

    private fun deleteRecord(todoId: String) {
        // --Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid).child(todoId) //initialize database with uid as the parent
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Transaction Data Deleted", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initView() { //initialized ui item id

        tvTitleDetails = findViewById(R.id.tvTitleDetails)
        tvNoteDetails = findViewById(R.id.tvNoteDetails)
//        tvDateDetails = findViewById(R.id.tvDateDetails)
    }

    private fun openUpdateDialog() {

        //---Initialize item---
        val etTitle = findViewById<EditText>(R.id.tvTitleDetails)
        val etNote = findViewById<EditText>(R.id.tvNoteDetails)
        val updateData: Button = findViewById(R.id.updateButton)
        //val etDate = findViewById<EditText>(R.id.dateUpdate)
        val todoId = intent.getStringExtra("todoId") //store todo id

        etTitle.setText(intent.getStringExtra("title").toString())
        etNote.setText(intent.getStringExtra("body").toString())

        //---set text to date edit text and date picker:---
        /*val date: Long = intent.getLongExtra("date", 0)
        val cal = Calendar.getInstance()
        val getDate = Date(date) //convert millis to date format
        cal.time = getDate

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val resultParse = simpleDateFormat.format(getDate)
        etDate.setText(resultParse)

        var dateUpdate: Long = intent.getLongExtra("date", 0) //initialized current date value on db
        var invertedDate: Long = dateUpdate * -1
        etDate.setOnClickListener {
            val year = cal.get(Calendar.YEAR) //set default year in datePickerDialog similar with database data
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    etDate.text = null
                    etDate.hint = selectedDate

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val theDate = sdf.parse(selectedDate)
                    dateUpdate = theDate!!.time //convert date to millisecond
                    invertedDate = dateUpdate * -1
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        */
        updateData.setOnClickListener {

            updateTodoData(
                todoId.toString(),
                etTitle.text.toString(),
                //dateUpdate,
                etNote.text.toString(),
                //invertedDate
            )
            Toast.makeText(applicationContext, "Transaction Data Updated", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateTodoData(
        todoId: String,
        title: String,
        //date: Long,
        note: String,
        //invertedDate: Long
    ){
        // --Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
            val todoInfo = TodoModel(todoId, /*date,*/ title, note/*,invertedDate*/)
            dbRef.child(todoId).setValue(todoInfo)
        }
    }
}