package com.rescreation.composition.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rescreation.composition.R;
import com.rescreation.composition.model.Category;
import com.rescreation.composition.view.ui.MainActivity;
import com.rescreation.composition.view.ui.ProductActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivityAdapter   extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {

    Context context;
    ArrayList<Category> categoryArrayList;

    public MainActivityAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public MainActivityAdapter.MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_menu_layout, parent, false);
        return new  MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.MainActivityViewHolder holder, int position) {
        holder.categoryName.setText(categoryArrayList.get(position).getMenu_name());
        holder.categoryDescription.setText(categoryArrayList.get(position).getMenu_description());
       // Picasso.get().load(categoryArrayList.get(position).getMenu_image()).into(holder.cardImageView);

        final ProgressBar progressView = holder.progressBar;
        Picasso.get().load(categoryArrayList.get(position).getMenu_image())
                //    .transform(new RoundedTransformation(15, 0))
                .fit()
                .into(holder.cardImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });

        holder.rowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked "+categoryArrayList.get(position).getMenu_name(), Toast.LENGTH_SHORT).show();
                Intent productIntent=new Intent(context, ProductActivity.class);
                productIntent.putExtra("cat_id",categoryArrayList.get(position).getId().toString());
                context.startActivity(productIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class MainActivityViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        TextView categoryDescription;
        ImageView cardImageView;
        ProgressBar progressBar;
        RelativeLayout rowList;

        public MainActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
            cardImageView = itemView.findViewById(R.id.cardImageView);
            progressBar = itemView.findViewById(R.id.bar);
            rowList = itemView.findViewById(R.id.rowList);

        }
    }
}
