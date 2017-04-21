package com.shorty.LetsEat.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.shorty.LetsEat.R;

public class SearchResultsViewHolder extends RecyclerView.ViewHolder{
    public TextView productName;
    public TextView brand;
    public TextView category;
    public TextView upc;
    public TextView avgPrice;

    public SearchResultsViewHolder(View view) {
        super(view);
        productName = (TextView)view.findViewById(R.id.productName);
        brand = (TextView)view.findViewById(R.id.brand);
        category = (TextView)view.findViewById(R.id.category);
        upc = (TextView)view.findViewById(R.id.upc);
        avgPrice = (TextView)view.findViewById(R.id.avgPrice);
    }
}