package com.example.phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolders>  {
    Context context;
    List<Products> productsList;

    public ProductsAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @NotNull
    @Override
    public ProductsHolders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View productsLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list,parent,false);
        return new ProductsHolders(productsLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsHolders holder, int position) {
        Products products = productsList.get(position);
        holder.Id.setText(products.getId());
        holder.Name.setText(products.getName());
        holder.Price.setText(products.getPrice());
        holder.Category.setText(products.getCategory());
        holder.Description.setText(products.getDescription());
        holder.Status.setText(products.getStatus());
        Glide.with(context).load(products.getImage()).into(holder.Image);
        holder.EditProducts.setOnClickListener(new View.OnClickListener() {
            final String id = products.getId();
            final String name = products.getName();
            final String price = products.getPrice();
            final String category = products.getCategory();
            final String description = products.getDescription();
            final String status = products.getStatus();
            final String image = products.getImage();

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context,EditProductsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name",name);
                intent.putExtra("price", price);
                intent.putExtra("category", category);
                intent.putExtra("description", description);
                intent.putExtra("status", status);
                intent.putExtra("image", image);
                context.startActivity(intent);
            }
        });
        holder.DeleteProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("");
                builder.setMessage("Do you want to delete this product ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest request = new StringRequest(Request.Method.POST, Urls.DELETE_PRODUCTS, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject object = new JSONObject(response);
                                    String check = object.getString("state");
                                    if(check.equals("delete")){
                                        Delete(position);
                                        Toast.makeText(context, "Products Delete Successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected @NotNull Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> deleteParams = new HashMap<>();
                                deleteParams.put("id", products.getId());
                                return deleteParams;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductsHolders extends RecyclerView.ViewHolder{
        TextView Id,Name,Price,Category,Description,Status;
        ImageView Image;
        Button EditProducts, DeleteProducts;

        public ProductsHolders(@NonNull @NotNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.idTv);
            Name = itemView.findViewById(R.id.nameTv);
            Price = itemView.findViewById(R.id.priceTv);
            Category = itemView.findViewById(R.id.categoryTv);
            Description = itemView.findViewById(R.id.descriptionTv);
            Status = itemView.findViewById(R.id.statusTv);
            Image = itemView.findViewById(R.id.imageTv);
            EditProducts = itemView.findViewById(R.id.btnProductEdit);
            DeleteProducts = itemView.findViewById(R.id.btnProductDelete);
        }
    }
    public void Delete(int item){
        productsList.remove(item);
        notifyItemRemoved(item);
    }
}
