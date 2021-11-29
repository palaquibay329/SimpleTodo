package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from list
                listOfTasks.removeAt(position)

                //2. Notifiy Adapter something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.i("Edgar", "User clicked on button")

        }

        loadItems()
        // Lookup the recyclerview in activity layout
        val recyclerView  = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField= findViewById<EditText>(R.id.addTaskField)
        // setup a button and input field so that the user can enter task
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. grab text user has inputted to edit text field, @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            //Notify adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size -1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()

        }
    }
    // Save the data the user has inputted
    // Save data by writing and reading from a file
    // get the data file we need
    fun getDataFile(): File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems(){
        try {
        listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
    } catch (ioException: IOException){
        ioException.printStackTrace()
        }
    }
    // Save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}