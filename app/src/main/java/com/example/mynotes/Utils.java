package com.example.mynotes;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Utils {


    public static boolean inputValidation(TextInputLayout input) {
        String username = input.getEditText().getText().toString().trim();
        return username.length() != 0;
    }

}
