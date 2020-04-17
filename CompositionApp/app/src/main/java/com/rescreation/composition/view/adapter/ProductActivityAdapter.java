package com.rescreation.composition.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.model.Product;
import com.rescreation.composition.view.ui.ProductActivity;
import com.rescreation.composition.view.ui.WebViewActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductActivityAdapter extends RecyclerView.Adapter<ProductActivityAdapter.ProductActivityViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    ArrayList<Product> arraylist;
    ArrayList<Product> totalDatalist;
    String catName="";

    public ProductActivityAdapter(Context context, ArrayList<Product> productArrayList,String catName) {
        this.context = context;
        this.catName = catName;
        this.productArrayList = productArrayList;
        this.arraylist = new ArrayList<Product>();
        this.arraylist.addAll(productArrayList);

    }


    @NonNull
    @Override
    public ProductActivityAdapter.ProductActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_layout, parent, false);
        return new ProductActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductActivityAdapter.ProductActivityViewHolder holder, int position) {
        holder.productName.setText(productArrayList.get(position).getProduct_name());
        holder.productDescription.setText(productArrayList.get(position).getProduct_description());
        // Picasso.get().load(categoryArrayList.get(position).getMenu_image()).into(holder.cardImageView);

//        final ProgressBar progressView = holder.progressBar;
//        Picasso.get().load(productArrayList.get(position).getMenu_image())
//                //    .transform(new RoundedTransformation(15, 0))
//                .fit()
//                .into(holder.cardImageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        progressView.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//
//                });

        holder.rowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked "+productArrayList.get(position).getProduct_name(), Toast.LENGTH_SHORT).show();
                Intent productIntent=new Intent(context, WebViewActivity.class);
                productIntent.putExtra("product_name",productArrayList.get(position).getProduct_name().toString());
                productIntent.putExtra("catName",catName);
                context.startActivity(productIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductActivityViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        TextView productDescription;
        ImageView cardImageView;
        ProgressBar progressBar;
        CardView rowList;

        public ProductActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
//            cardImageView = itemView.findViewById(R.id.cardImageView);
//            progressBar = itemView.findViewById(R.id.bar);
            rowList = itemView.findViewById(R.id.rowList);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        if(arraylist.size()<1){
            arraylist.clear();
            arraylist.addAll(productArrayList);
        }

        productArrayList.clear();

        if (charText.length() == 0) {
            productArrayList.addAll(arraylist);
        } else {
            for (Product wp : arraylist) {
                if (wp.getProduct_description().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productArrayList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}
