package com.example.phone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeProductsAdapter extends  RecyclerView.Adapter<HomeProductsAdapter.HomeProductsHolders> {
    Context context;
    List<Products> productsList;

    public HomeProductsAdapter(Context context, List<Products> homeProductList) {
        this.context = context;
        this.productsList = homeProductList;
    }



    @NonNull
    @NotNull
    @Override
    public HomeProductsHolders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View productsLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_products_list,parent,false);
        return new HomeProductsHolders(productsLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeProductsAdapter.HomeProductsHolders holder, int position) {
        Products products = productsList.get(position);
        holder.Id.setText(products.getId());
        holder.Name.setText(products.getName());
        holder.Price.setText(products.getPrice());
        holder.Category.setText(products.getCategory());
        holder.Description.setText(products.getDescription());
        holder.Status.setText(products.getStatus());
        Glide.with(context).load(products.getImage()).into(holder.ImageView);

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class HomeProductsHolders extends RecyclerView.ViewHolder{
        TextView Id,Name,Price,Category,Description,Status;
        ImageView ImageView;

        public HomeProductsHolders(@NonNull @NotNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.HomeIdTv);
            Name = itemView.findViewById(R.id.HomeProductNameTv);
            Price = itemView.findViewById(R.id.HomePriceTv);
            ImageView = itemView.findViewById(R.id.HomeImageIv);
            Category = itemView.findViewById(R.id.HomeCategoryTv);
            Description = itemView.findViewById(R.id.HomeDescriptionTv);
            Status = itemView.findViewById(R.id.HomeStatusTv);
        }
    }
}
