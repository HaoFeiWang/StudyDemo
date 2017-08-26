package com.whf.testkotlin

import android.util.Log

/**
 * Created by root on 2017/8/26.
 * @author whf
 */
class Person(age: Int, name: String) {

    //可变的成员变量
    var age: Int = 0
    var name: String = "123"
    //不可变得成员变量，相当于final
    val isPerson: Boolean = true

    init {
        this.age = age
        this.name = name
    }

    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    fun showAge(c: Int) {
        //可以不直接赋值，也可以不声明变量类型，不赋值时不能省略类型
        val a: Int = 12
        var b: Int
        b = c
        Log.e("+++++++++++", (a + b).toString())
    }

    fun showName() {
        val a : String = "Hello"
        var b = 2

        var c = "a is $a"
        //套用字符串，并没有真正地改变其字符串
        val d = "${a.replace("Hello","Hello Word")},but now is $a"

        Log.e("++++++++++++++++",c)
        Log.e("++++++++++++++++",d)

    }

}