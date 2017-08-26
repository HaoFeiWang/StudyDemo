package com.whf.decorationitem;

/**
 * Created by WHF on 2017/3/6.
 */

public class Person {
    private String mName;
    private String mNumber;

    public Person() {
    }

    public Person(String name, String number) {
        this.mName = name;
        this.mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }
}
