package com.whf.testkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById(R.id.bt) as Button
        var editText = findViewById(R.id.edt) as EditText

        button.setOnClickListener({
//            Log.e("--------------", editText.text.toString())
//            Log.e("--------------", Person(16,"小明").name)
//            //空字符串只能加在数字之前
//            Log.e("--------------", ""+Person(16,"小明").sum(3,5))
//            Person(16,"小明").showAge(5)
            Person(16,"小米").showName()
        })
    }
}
