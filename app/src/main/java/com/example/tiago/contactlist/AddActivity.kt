package com.example.tiago.contactlist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    lateinit var txtname : EditText
    lateinit var txtphone : EditText
    lateinit var buttonadd : Button
    lateinit var titleactivity : TextView

    lateinit var ref : DatabaseReference

    var typeget = ""
    var contactget = Contact("","",0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        txtname = findViewById(R.id.edittextname)
        txtphone = findViewById(R.id.edittextphone)
        buttonadd = findViewById(R.id.bttadd)
        titleactivity = findViewById(R.id.titletext)
        typeget =  intent.getStringExtra("typesend").toString()
        contactget = Contact(intent.getStringExtra("idsend").toString(),intent.getStringExtra("namesend").toString(),intent.getStringExtra("phonesend").toInt())
        txtname.requestFocus()

        ref = FirebaseDatabase.getInstance().getReference("contact")

        if (typeget.toInt() == 1){
            titleactivity.setText("Add Contact")
            buttonadd.setText("ADD")
        }else if (typeget.toInt() == 2){
            titleactivity.setText("Edit Contact")
            buttonadd.setText("SAVE")
            txtname.setText(contactget.name)
            txtphone.setText(contactget.phonenumber.toString())
        }

        buttonadd.setOnClickListener {
            val name = txtname.text.toString()
            val phone = txtphone.text.toString().toInt()
            if (name.isEmpty()){
                txtname.error = "please enter a name!"
                return@setOnClickListener
            }

            if (txtphone.text.toString().isEmpty()){
                txtphone.error = "please enter a phone number!"
                return@setOnClickListener
            }

            if (typeget.toInt() == 1){
                val Id = ref.push().key
                val contact = Contact(Id,name,phone)
                ref.child(Id).setValue(contact).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Contact Added Successfully!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }else if (typeget.toInt() == 2){
                val contact = Contact(contactget.id,name,phone)
                ref.child(contactget.id).setValue(contact).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Contact Edited Successfully!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        }
    }
}