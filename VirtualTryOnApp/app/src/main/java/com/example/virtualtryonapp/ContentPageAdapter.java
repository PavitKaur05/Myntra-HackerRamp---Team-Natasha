package com.example.virtualtryonapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContentPageAdapter extends RecyclerView.Adapter<ContentPageAdapter.CustomViewHolder>{
    Context mContext;
    ArrayList<String> productId,productName,productImageLink,productBrand,productPrice;


    public ContentPageAdapter(Context mContext, ArrayList<String> productId, ArrayList<String> productName, ArrayList<String> productImageLink,ArrayList<String> productBrand,ArrayList<String> productPrice) {
        this.mContext = mContext;
        this.productId=productId;
        this.productName=productName;
        this.productImageLink=productImageLink;
        this.productBrand=productBrand;
        this.productPrice=productPrice;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tbrand.setText(productBrand.get(position));
        holder.tname.setText(productName.get(position));
        holder.tprice.setText(productPrice.get(position));
        Picasso.get().load(productImageLink.get(position)).into(holder.timageView);

    }

    @Override
    public int getItemCount() {
        return productId.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView timageView;
        TextView tbrand,tname,tprice;
        ConstraintLayout tlayout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tlayout = itemView.findViewById(R.id.myparent);
            tbrand = itemView.findViewById(R.id.brand);
            tname = itemView.findViewById(R.id.type);
            tprice = itemView.findViewById(R.id.price);
            timageView = itemView.findViewById(R.id.imageView2);
            tlayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            //Toast.makeText(mContext,"You clcick",Toast.LENGTH_LONG).show();
            Intent intent= new Intent(mContext,DetailsPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id",productId.get(position));
            intent.putExtra("brand",productBrand.get(position));
            intent.putExtra("name",productName.get(position));
            intent.putExtra("price",productPrice.get(position));
            intent.putExtra("imageLink",productImageLink.get(position));
            mContext.startActivity(intent);

        }
    }
}
