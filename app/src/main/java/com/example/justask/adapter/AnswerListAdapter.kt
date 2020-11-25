package com.example.juskask2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.juskask2.Model.AnswerList
import com.example.juskask2.Service.DatabaseMethod
import com.example.juskask2.Service.SharedPreference
import com.example.justask.R
import com.google.firebase.firestore.FirebaseFirestore
import xyz.hanks.library.bang.SmallBangView

class AnswerListAdapter(var ctx: Context, var resources: Int, var items: List<AnswerList>) :
    ArrayAdapter<AnswerList>(
        ctx,
        resources,
        items
    ) {
    var databaseMethod: DatabaseMethod = DatabaseMethod()
    val sharedPreference: SharedPreference = SharedPreference(context)
    val db = FirebaseFirestore.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
//        val view: View = layoutInflater.inflate(resources, null)

        val email: String = sharedPreference.getValueString("email").toString()
        val question: String = sharedPreference.getValueString("question").toString()
        val currentUser: String = sharedPreference.getValueString("username").toString()

//        val user: TextView = view.findViewById(R.id.al_user)
//        val answer: TextView = view.findViewById(R.id.al_answer)
//        val like: SmallBangView = view.findViewById(R.id.imageViewAnimation)

        val currentitem: AnswerList = items[position]
        var holder: ViewHolder
        var retView: View
        if (convertView == null) {
            holder = ViewHolder()
            retView = LayoutInflater.from(context).inflate(R.layout.answer_list, parent, false)

            holder.user = retView.findViewById(R.id.al_user)
            holder.answer = retView.findViewById(R.id.al_answer)
            holder.like = retView.findViewById(R.id.imageViewAnimation)

            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.user.text = currentitem.user
        holder.answer.text = currentitem.answer
        holder.like.isSelected = currentitem.like
        val targetUser = holder.user.text.toString()
        val fav_answer = holder.answer.text.toString()
        val answerInfo = HashMap<String, Any>()
        var islike: Boolean = false

        val fadeanimation: Animation = AnimationUtils.loadAnimation(context,R.anim.fade_in)
        val slideanimation:Animation = AnimationUtils.loadAnimation(context,R.anim.slide_in)
        retView.startAnimation(fadeanimation)
        retView.startAnimation(slideanimation)

        holder.like.setOnClickListener(
            View.OnClickListener {
                if (holder.like.isSelected()) {
                    holder.like.setSelected(false)
                    databaseMethod.removeFavUser(email, question, targetUser)

                    islike = false
                    answerInfo["is liked by" + currentUser] = islike
                    databaseMethod.updateIsLike(question, targetUser, answerInfo)


                } else {
                    // if not selected only
                    // then show animation.
                    holder.like.setSelected(true)
                    holder.like.likeAnimation()
                    val favUser = HashMap<String, Any>()
                    favUser["Fav_user"] = targetUser
                    favUser["Fav_answer"] = fav_answer
                    databaseMethod.setNewFavUser(email, question, targetUser, favUser)
                    islike = true
                    answerInfo["is liked by" + currentUser] = islike
                    Log.d("islikeby", islike.toString())
                    databaseMethod.updateIsLike(question, targetUser, answerInfo)
                }
            })

        return retView
    }

    class ViewHolder {
        lateinit var user: TextView
        lateinit var answer: TextView
        lateinit var like: SmallBangView
    }
}