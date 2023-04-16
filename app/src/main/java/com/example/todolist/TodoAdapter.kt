package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter(private val todoList: ArrayList<TodoModel>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.title.text = currentItem.titleTodo // get the current title

        holder.body.text = currentItem.bodyTodo

//        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//        val result = Date(currentItem.date!!)
//        holder.tvDate.text = simpleDateFormat.format(result)

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.todoTitle)
        val body: TextView = itemView.findViewById(R.id.todoBody)
//        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}