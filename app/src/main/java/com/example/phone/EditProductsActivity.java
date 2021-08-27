package com.example.phone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProductsActivity extends AppCompatActivity {

    private String id;
    private EditText nameEdit, priceEdit, categoryEdit, descriptionEdit;
    private ImageView Image;
    String encodeImageString;
    Bitmap bitmap;
    AutoCompleteTextView statusEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);

        //Initialize id;
        nameEdit = findViewById(R.id.etNameEdit);
        priceEdit = findViewById(R.id.etPriceEdit);
        categoryEdit = findViewById(R.id.etCategoryEdit);
        descriptionEdit = findViewById(R.id.etDescriptionEdit);
        statusEdit = findViewById(R.id.actvStatusEdit);
        Image = findViewById(R.id.imgEdit);
        Button btnBrowseEdit = findViewById(R.id.btnbrowseEdit);
        Button btnEdit = findViewById(R.id.btnEdit);

        //Get string from adapter;
        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");
        String status = getIntent().getStringExtra("status");
        String image = getIntent().getStringExtra("image");

        //Transfer String into Edittext;
        nameEdit.setText(name);
        priceEdit.setText(price);
        categoryEdit.setText(category);
        descriptionEdit.setText(description);
        statusEdit.setText(status);
        Glide.with(this)
                .load(image)
                .into(Image);

        //Status adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.avail, R.layout.dropdown_item);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        statusEdit.setAdapter(adapter);

        //Update products in the Database
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductsDb();
            }
        });

        //Browse photo in the gallery
        btnBrowseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(EditProductsActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Browse"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });



    }

    //Data to Bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Image.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Bitmap to EncodedString
    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString= android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    //Update products in the Database
    private void EditProductsDb() {
        final String Name =  nameEdit.getText().toString().trim();
        final String Price = priceEdit.getText().toString().trim();
        final String Category = categoryEdit.getText().toString().trim();
        final String Description = descriptionEdit.getText().toString().trim();
        final String Status = statusEdit.getText().toString().trim();
        final String NullPicture = "Null";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditProductsActivity.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProductsActivity.this,ViewProductsActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProductsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                    map.put("id",id);
                    map.put("name", Name);
                    map.put("price", Price);
                    map.put("category", Category);
                    map.put("description", Description);
                    map.put("status", Status);
                    if(encodeImageString == null){
                        map.put("upload", NullPicture);
                    }else {
                        map.put("upload", encodeImageString);
                    }
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
}