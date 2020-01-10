package com.example.yhao.floatwindow.base;

import android.os.Bundle;

import com.example.yhao.fixedfloatwindow.R;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
    }
}
