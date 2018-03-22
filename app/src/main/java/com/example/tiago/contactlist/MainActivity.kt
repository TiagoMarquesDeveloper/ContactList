package com.example.tiago.contactlist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import android.R.attr.name
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.R.drawable.ic_delete
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuListView

class MainActivity : AppCompatActivity() {

    lateinit var buttonadd : FloatingActionButton
    lateinit var listview : SwipeMenuListView
    lateinit var ref : DatabaseReference

    lateinit var contactlist : MutableList<Contact>
    lateinit var KeyList : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonadd = findViewById(R.id.fab)
        listview = findViewById(R.id.lista)

        contactlist = mutableListOf()
        KeyList = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("contact")

        val adapter = HeroAdapter(this@MainActivity,R.layout.list,contactlist)
        listview.adapter = adapter

        ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        contactlist.clear()
                        for (h in dataSnapshot.children){
                            val contact = h.getValue(Contact::class.java)
                            contactlist.add(contact!!)
                        }
                        val adapter = HeroAdapter(this@MainActivity,R.layout.list,contactlist)
                        listview.adapter = adapter
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "error:" + databaseError.toException().toString(),Toast.LENGTH_LONG).show()
                    }
                })

        reloaddata()

        val creator = SwipeMenuCreator { menu ->
            val openItem = SwipeMenuItem(
                    applicationContext)
            openItem.background = ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE))
            openItem.width = 170
            openItem.title = "Edit"
            openItem.titleSize = 18
            openItem.titleColor = Color.WHITE
            menu.addMenuItem(openItem)

            val deleteItem = SwipeMenuItem(
                    applicationContext)
            deleteItem.background = ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25))
            deleteItem.width = 170
            deleteItem.setIcon(R.drawable.ic_action_delete)
            menu.addMenuItem(deleteItem)
        }

        listview.setMenuCreator(creator)

        buttonadd.setOnClickListener {
            openactivity("1","","","0")
        }

        listview.setOnMenuItemClickListener(object : SwipeMenuListView.OnMenuItemClickListener {
            override fun onMenuItemClick(position: Int, menu: SwipeMenu, index: Int): Boolean {
                when (index) {
                    0 -> {
                        openactivity("2",contactlist[position].id,contactlist[position].name,contactlist[position].phonenumber.toString())
                    }
                    1 -> {
                        val alert = AlertDialog.Builder(this@MainActivity)
                        alert.setTitle("Are you Sure?")
                        alert.setMessage("Do you want to delete this contact?")
                        alert.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int ->
                            ref.child(KeyList[position]).removeValue()
                            reloaddata()
                        })
                        alert.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int ->

                        })
                        alert.show()
                    }
                }// open
                // delete
                // false : close the menu; true : not close the menu
                return false
            }
        })

    }

    fun openactivity(type : String, id : String,name : String, phone: String){
        val intent = Intent(this, AddActivity::class.java)
        intent.putExtra("typesend",type)
        intent.putExtra("idsend",id)
        intent.putExtra("namesend",name)
        intent.putExtra("phonesend",phone)
        startActivity(intent)
    }

    fun reloaddata(){
        ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        contactlist.clear()
                        KeyList.clear()
                        for (h in dataSnapshot.children){
                            val contact = h.getValue(Contact::class.java)
                            contactlist.add(contact!!)
                            KeyList.add(h.key)
                        }
                        val adapter = HeroAdapter(this@MainActivity,R.layout.list,contactlist)
                        listview.adapter = adapter
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "error:" + databaseError.toException().toString(),Toast.LENGTH_LONG).show()
                    }
                })
    }

    override protected fun onRestart() {
        super.onRestart();
        reloaddata()
    }
}
