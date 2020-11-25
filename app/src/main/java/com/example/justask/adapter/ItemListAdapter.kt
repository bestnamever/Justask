package com.example.juskask2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.juskask2.Model.Item
import com.example.justask.R

class ItemListAdapter (var ctx: Context, var resources: Int, var items: List<Item>): ArrayAdapter<Item>(ctx, resources, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
//        val view: View = layoutInflater.inflate(resources, null)
//
//        val question: TextView = view.findViewById(R.id.question)
//        val question_des: TextView = view.findViewById(R.id.question_des)
//
        val currentitem: Item = items[position]
        var holder :ViewHolder
        var retView: View
        if(convertView == null){
            holder = ViewHolder()
            retView = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false)

            holder.question = retView.findViewById(R.id.question)
            holder.question_des=retView.findViewById(R.id.question_des)

            retView.tag = holder

        }
        else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.question.text = currentitem.question
        holder.question_des.text = currentitem.question_des

        val fadeanimation: Animation = AnimationUtils.loadAnimation(context,R.anim.fade_in)
        val slideanimation:Animation = AnimationUtils.loadAnimation(context,R.anim.slide_in)
        retView.startAnimation(fadeanimation)
        retView.startAnimation(slideanimation)

        return retView
    }
    class ViewHolder{
        lateinit var question: TextView
        lateinit var question_des :TextView
    }

}