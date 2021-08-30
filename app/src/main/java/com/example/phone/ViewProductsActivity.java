package com.example.phone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;
    private List<Products> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_product_list);
        FloatingActionButton btnFloating = findViewById(R.id.fab_btn);
        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsList = new ArrayList<>();

        LoadAllProducts();

        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProductsActivity.this,ProductsActivity.class));
            }
        });

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
                productsAdapter = new ProductsAdapter(ViewProductsActivity.this,productsList);
                recyclerView.setAdapter(productsAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewProductsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ViewProductsActivity.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
        requestQueue.start();
    }
}