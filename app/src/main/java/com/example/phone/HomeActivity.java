package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  HomeActivity extends AppCompatActivity {

    Users users;

    private RecyclerView recyclerView;

    private List<Products> productsList;

    HomeProductsAdapter homeProductsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        users = new Users(this);
        users.checkLogin();

        HashMap<String, String> user = users.userDetails();
        String uEmail = user.get(users.EMAIL);
        String uPhone = user.get(users.PHONE);
        String uFullname = user.get(users.NAME);

        TextView nameTv = findViewById(R.id.FullNameTv);
        TextView emailTv = findViewById(R.id.EmailTv);
        TextView phoneTv = findViewById(R.id.PhoneTv);


        nameTv.setText(uFullname);
        emailTv.setText(uEmail);
        phoneTv.setText(uPhone);

        if(uFullname.equals("admin")){
            Intent i = new Intent(HomeActivity.this, ViewProductsActivity.class);
            startActivity(i);
            finish();
        }

        recyclerView = findViewById(R.id.HomeRecyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsList = new ArrayList<>();


        LoadAllProducts();
    }

    private void LoadAllProducts() {
        JsonArrayRequest request = new JsonArrayRequest(Urls.SHOW_ALL_PRODUCTS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject object = array.getJSONObject(i);
                        int id = object.getInt("id");
                        String name =object.getString("name").trim();
                        String price = object.getString("price").trim();
                        String category = object.getString("category").trim();
                        String description = object.getString("description").trim();
                        String status = object.getString("status").trim();
                        String image = object.getString("image").trim();

                        String s = String.valueOf(id);
                        Products products = new Products();
                        products.setId(s);
                        products.setName(name);
                        products.setPrice(price);
                        products.setCategory(category);
                        products.setDescription(description);
                        products.setStatus(status);
                        products.setImage(Urls.ROOT_URl + "images/" + image);
                        productsList.add(products);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                homeProductsAdapter = new HomeProductsAdapter(HomeActivity.this, productsList);
                recyclerView.setAdapter(homeProductsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(HomeActivity.this,
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
        requestQueue.start();
        }

    public void logout(View view) {
        users.logout();
    }

}