package com.shorty.LetsEat.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shorty.LetsEat.R;
import com.shorty.LetsEat.callbacks.ViewHolderHandler;
import com.shorty.LetsEat.objects.ResponseList;
import com.shorty.LetsEat.utilities.KEYS;
import com.shorty.LetsEat.viewholders.SearchResultsViewHolder;

import java.text.DecimalFormat;
import java.util.Map;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {
    ResponseList items;
    ViewHolderHandler viewHolderHandler;
    Activity context;

    int itemLayoutId;

    public SearchResultsAdapter(Activity context, ResponseList items, int itemLayoutId){
        this.context = context;
        this.items = items;
        this.itemLayoutId = itemLayoutId;
    }

    public SearchResultsAdapter(Activity context, ResponseList items, int itemLayoutId, ViewHolderHandler viewHolderHandler){
        this(context, items, itemLayoutId);
        this.viewHolderHandler = viewHolderHandler;
    }

    public void setItemLayoutId(int itemLayoutId){
        this.itemLayoutId = itemLayoutId;
    }

    public void populateView(SearchResultsViewHolder viewHolder, Map<String, Object> item){
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

    @Override
    public SearchResultsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.search_list_item, viewGroup, false);
        return new SearchResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchResultsViewHolder viewHolder, int position) {
        Map<String, Object> item = getItemByPosition(position);
        populateView(viewHolder, item);
    }

    public Map<String, Object> getItemByPosition(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
