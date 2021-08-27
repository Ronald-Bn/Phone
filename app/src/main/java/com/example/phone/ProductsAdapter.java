package com.example.phone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

            String id = products.getId();
            String name = products.getName();
            String price = products.getPrice();
            String category = products.getCategory();
            String description = products.getDescription();
            String status = products.getStatus();
            String image = products.getImage();

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
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductsHolders extends RecyclerView.ViewHolder{
        TextView Id,Name,Price,Category,Description,Status;
        ImageView Image;
        Button EditProducts;
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
        }
    }
}
