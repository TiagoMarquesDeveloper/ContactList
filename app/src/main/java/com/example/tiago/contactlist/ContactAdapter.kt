package com.example.tiago.contactlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by Tiago on 20/03/2018.
 */

class HeroAdapter(val mCtx: Context, val layoutResId: Int, val ContactList: List<Contact>) : ArrayAdapter<Contact>(mCtx,layoutResId, ContactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutinflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutinflater.inflate(layoutResId,null)
        val nametext = view.findViewById<TextView>(R.id.txtname)
        val phonetext = view.findViewById<TextView>(R.id.txtphone)
        val hero = ContactList[position]
        nametext.text = hero.name
        phonetext.text = "phone number: " + hero.phonenumber
        return view
    }

}