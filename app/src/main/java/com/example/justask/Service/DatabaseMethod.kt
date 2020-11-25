package com.example.juskask2.Service

import com.google.firebase.firestore.FirebaseFirestore


class DatabaseMethod {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun setNewUser(email: String, user: HashMap<String, Any>) {
        db.collection("users").document(email).set(user)
    }

    fun setNewQuestion(question: String, questionInfo: HashMap<String, Any>) {
        db.collection("questions").document(question).set(questionInfo)
    }

    fun setNewAnswerToquestion(
        question: String,
        answerer: String,
        answerInfo: HashMap<String, Any>
    ) {
        db.collection("questions").document(question).collection("answers").document(answerer)
            .set(answerInfo)
    }

    fun setNewAnswerToUser(email: String, timestamp: String, answerInfo: HashMap<String, Any>) {
        db.collection("users").document(email).collection("answers").document(timestamp)
            .set(answerInfo)
    }

    fun setNewFavUser(
        email: String,
        question: String,
        user: String,
        username: HashMap<String, Any>
    ) {
        db.collection("users").document(email).collection("fav_user")
            .document(question + "of" + user).set(username)
    }

    fun removeFavUser(email: String, question: String, user: String) {
        db.collection("users").document(email).collection("fav_user")
            .document(question + "of" + user).delete()
    }

    fun updateIsLike(question: String, answerer: String, answerInfo: HashMap<String, Any>) {
        db.collection("questions").document(question).collection("answers").document(answerer)
            .update(answerInfo)
    }

    fun newinvitation(question: String, invitationInfo: HashMap<String, Any>) {
        db.collection("invitation").document(question).set(invitationInfo)
    }

    fun dismissinvitation(question: String){
        db.collection("invitation").document(question).delete()
    }
}