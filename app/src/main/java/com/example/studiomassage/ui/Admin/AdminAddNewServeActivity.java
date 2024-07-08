package com.example.studiomassage.ui.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studiomassage.R;
import com.example.studiomassage.ui.LoginActivity;
import com.example.studiomassage.ui.RegisterActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewServeActivity extends AppCompatActivity {

    private String categoryName, Description, Price, SName, saveCurrentDate, saveCurrentTime, serviceRandomKey;
    private ImageView servicesImage;
    private EditText serviceName, serviceDescribe, servicePrice;
    private Button addNewServiceBtn;
    private static final int GALLERYPICK = 1;
    private StorageReference ServiceImageRef;
    private Uri ImageUri;
    private String downloadedImageUri;
    private DatabaseReference ServicesRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_serve);

        init();

        servicesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        addNewServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateServiceData();
            }
        });
    }

    private void ValidateServiceData() {
        Description = serviceDescribe.getText().toString();
        Price = servicePrice.getText().toString();
        SName = serviceName.getText().toString();
        
        if(ImageUri == null){
            Toast.makeText(this, "Добавьте изображение услуги", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Добавьте описание услуги", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Добавьте стоимость услуги", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Добавьте название услуги", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreServiceInformation();
        }
    }

    private void StoreServiceInformation() {

        loadingBar.setTitle("Загрузка данных");
        loadingBar.setMessage("Пожалуйста,подождите");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        serviceRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = ServiceImageRef.child(ImageUri.getLastPathSegment() + serviceRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewServeActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewServeActivity.this, "Изображение успешно загружено", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadedImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminAddNewServeActivity.this, "Фото сохранено", Toast.LENGTH_SHORT).show();

                            SaveServiceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveServiceInfoToDatabase() {
        HashMap<String, Object> serviceMap = new HashMap<>();

        serviceMap.put("serviceID", serviceRandomKey);
        serviceMap.put("date", saveCurrentDate);
        serviceMap.put("time", saveCurrentTime);
        serviceMap.put("description", Description);
        serviceMap.put("image", downloadedImageUri);
        serviceMap.put("category", categoryName);
        serviceMap.put("price", Price);
        serviceMap.put("serviceName", SName);

        ServicesRef.child(serviceRandomKey).updateChildren(serviceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewServeActivity.this, "Услуга добавлена", Toast.LENGTH_SHORT).show();

                    Intent logintIntent = new Intent(AdminAddNewServeActivity.this, AdminCategoryActivity.class);
                    startActivity(logintIntent);
                }
                else{
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewServeActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            servicesImage.setImageURI(ImageUri);
        }
    }

    private void init() {
        categoryName = getIntent().getExtras().get("category").toString();
        servicesImage = findViewById(R.id.select_img_service);
        serviceName = findViewById(R.id.name_service);
        serviceDescribe = findViewById(R.id.describe_service);
        servicePrice = findViewById(R.id.price_service);
        addNewServiceBtn = findViewById(R.id.add_new_services_btn);
        ServiceImageRef = FirebaseStorage.getInstance().getReference().child("Service Image");
        ServicesRef = FirebaseDatabase.getInstance().getReference().child("Services");
        loadingBar = new ProgressDialog(this);

    }
}