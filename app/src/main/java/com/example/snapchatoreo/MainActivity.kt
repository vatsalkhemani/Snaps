package com.example.snapchatoreo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {


    var emailEditText: EditText ?= null
    var passwordEditText: EditText ?= null
    private val mAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailEditText=findViewById(R.id.emailEditText)
        passwordEditText=findViewById(R.id.passwordEditText)


        if(mAuth.currentUser!=null){
            logIn()
        }



    }

    fun logIn(){
        val intent= Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
    fun goClicked(view: View){
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user?.uid!!).child("email").setValue(emailEditText?.text.toString())
                    logIn()
                } else {
                    // If sign in fails, display a message to the user.
                   mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this){task ->
                       if(task.isSuccessful){
                           FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user?.uid!!).child("email").setValue(emailEditText?.text.toString())
                           logIn()
                       }else{
                           Toast.makeText(this,"Login Failed! Try Again",Toast.LENGTH_SHORT).show()

                       }
                   }
                }

                // ...
            }

    }
}