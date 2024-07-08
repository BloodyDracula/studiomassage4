package com.example.studiomassage.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.studiomassage.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView aromaterap, bank, face, footmas, maslo, spina, rocks, handmas, vibromas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        init();

        aromaterap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "aromaterap");
                startActivity(intent);
            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "bank");
                startActivity(intent);
            }
        });
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "face");
                startActivity(intent);
            }
        });
        footmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "footmas");
                startActivity(intent);
            }
        });
        maslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "maslo");
                startActivity(intent);
            }
        });
        spina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "spina");
                startActivity(intent);
            }
        });
        rocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "rocks");
                startActivity(intent);
            }
        });
        handmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "handmas");
                startActivity(intent);
            }
        });
        vibromas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewServeActivity.class);
                intent.putExtra("category", "vibromas");
                startActivity(intent);
            }
        });
    }

    private void init() {
        aromaterap = findViewById(R.id.aromaterap);
        bank = findViewById(R.id.bank);
        face = findViewById(R.id.face);
        footmas = findViewById(R.id.footmas);
        maslo = findViewById(R.id.maslo);
        spina = findViewById(R.id.spina);
        rocks = findViewById(R.id.rocks);
        handmas = findViewById(R.id.handmas);
        vibromas = findViewById(R.id.vibromas);
    }
}