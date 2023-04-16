package com.example.todolist

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.todolist.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class Insertion : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var etDate: EditText
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isSubmitted: Boolean = false
    private var date: Long = 0
    private var invertedDate: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_insertion)
        etTitle = findViewById(R.id.title)
        etBody = findViewById(R.id.details)

        //---back button---
        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            finish()
        }

//        // --Initialize Firebase Auth and firebase database--
//        val user = Firebase.auth.currentUser
//        val uid = user?.uid
//        if (uid != null) {
//            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
//        }
//        auth = Firebase.auth

        //---date picker---
        /*
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis())) //take current date
        date = currentDate!!.time //initialized date value to current date as the default value
        etDate.setOnClickListener {
            clickDatePicker()
        }
        */
        //----

        val btnSaveData: Button = findViewById(R.id.saveButton)
        btnSaveData.setOnClickListener {
            if (!isSubmitted){
                saveTodoData()
            }else{
                Snackbar.make(findViewById(android.R.id.content), "You have saved todo data", Snackbar.LENGTH_LONG).show()
            }

        }

    }

    /*private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                etDate.text = null
                etDate.hint = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                date = theDate!!.time //convert date to millisecond

            },
            year,
            month,
            day
        )
        dpd.show()
    }
*/
    private fun saveTodoData(){
        val title = etTitle.text.toString()
        val body = etBody.text.toString()

        //dbRef = FirebaseDatabase.getInstance().getReference("Todo")

        // --Initialize Firebase Auth and firebase database--
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(uid) //initialize database with uid as the parent
        }
        auth = Firebase.auth


        if(title.isEmpty()){
            etTitle.error = "Please enter Amount"
        }else if(body.isEmpty()) {
            etBody.error = "Please enter Title"
        }else{
            val todoId = dbRef.push().key!!
            //invertedDate = date * -1 //convert millis value to negative, so it can be sort as descending order
            val item = TodoModel(todoId,/*date,*/ title, body/*, invertedDate*/)
            dbRef.child(todoId).setValue(item)
                .addOnCompleteListener{
                    etTitle.text.clear()
                    etBody.text.clear()
                    Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
            isSubmitted = true
        }
    }

}