package com.example.sayac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class SettingActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Button kaydetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        checkBox = findViewById(R.id.checkBox);
        kaydetButton = findViewById(R.id.kaydetButton);

        SharedPref shared = SharedPref.getInstance(getApplicationContext());
        checkBox.setChecked(shared.objeyiÇek());

        if (checkBox.isChecked()) {
            checkBox.setText("Sayacın alt ve üst limitlerine ulaşılması durumunda ses/titreşim açık");
        }
        else {
            checkBox.setText("Sayacın alt ve üst limitlerine ulaşılması durumunda ses/titreşim açık değil");
        }

        kaydetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.kaydet(checkBox.isChecked());
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}