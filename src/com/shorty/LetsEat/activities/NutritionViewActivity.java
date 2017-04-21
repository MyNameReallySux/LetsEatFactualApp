package com.shorty.LetsEat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.shorty.LetsEat.R;
import com.shorty.LetsEat.callbacks.ViewHolderHandler;
import com.shorty.LetsEat.listeners.RecyclerItemClickListener;
import com.shorty.LetsEat.objects.ResponseList;
import com.shorty.LetsEat.utilities.KEYS;
import com.shorty.LetsEat.viewholders.SearchResultsViewHolder;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

public class NutritionViewActivity extends Activity
        implements ViewHolderHandler<SearchResultsViewHolder> {

    ResponseList responseList;
    SearchResultsViewHolder viewHolder;

    int position;

    CardView cardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        handleIntent();
    }

    public void initialize(){
        setContentView(R.layout.nutrition_view);

        cardView = (CardView)findViewById(R.id.cardView);
        viewHolder = new SearchResultsViewHolder(cardView);
    }

    public void handleIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.d("...", bundle.toString());
        /*
        position = bundle.getInt(KEYS.INDEX);
        responseList = bundle.getParcelable(KEYS.QUERY_LIST);
        if((responseList == null || responseList.size() <= 0)){
            Log.d("...", "ResponseList = Null or size 0");
        } else if(position <= 0){
            Log.d("...", "Passed Position = 0");
        } else {
            Map<String, Object> item = responseList.get(position);
            populateView(item, viewHolder);
        }
        */
    }

    @Override
    public void populateView(Map<String, Object> item, SearchResultsViewHolder viewHolder) {
        viewHolder.productName.setText((String) item.get(KEYS.PRODUCT_NAME));
        viewHolder.brand.setText((String)item.get(KEYS.BRAND));
        viewHolder.category.setText((String)item.get(KEYS.CATEGORY));

        StringBuilder upcStr = new StringBuilder((String)item.get(KEYS.UPC));
        upcStr.insert(11, '-');
        upcStr.insert(6, '-');
        upcStr.insert(1, '-');
        viewHolder.upc.setText((upcStr.toString()));

        String priceStr = "N/A";
        Object obj = item.get(KEYS.AVG_PRICE);
        float num = 0.0f;
        if(obj != null){
            if(obj instanceof Integer){
                num = ((Integer)obj).floatValue();
            } else if(obj instanceof Double){
                num = ((Double) obj).floatValue();
            } else if (obj instanceof Float){
                num = (Float) obj;
            }
            priceStr = new DecimalFormat("$###.00")
                    .format(num);
        }
        viewHolder.avgPrice.setText(priceStr);
    }
}