package com.shorty.LetsEat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.widget.*;
import com.factual.driver.Query;

import java.text.DecimalFormat;
import java.util.*;

import android.support.v7.widget.RecyclerView;
import com.factual.driver.ReadResponse;
import com.shorty.LetsEat.R;
import com.shorty.LetsEat.adapters.SearchResultsAdapter;
import com.shorty.LetsEat.callbacks.AsyncTaskHandler;
import com.shorty.LetsEat.callbacks.OnItemClickHandler;
import com.shorty.LetsEat.callbacks.RecyclerScrollHandler;
import com.shorty.LetsEat.callbacks.ViewHolderHandler;
import com.shorty.LetsEat.listeners.RecyclerItemClickListener;
import com.shorty.LetsEat.listeners.RecyclerScrollListener;
import com.shorty.LetsEat.objects.ResponseList;
import com.shorty.LetsEat.tasks.FactualRetrievalTask;
import com.shorty.LetsEat.utilities.KEYS;
import com.shorty.LetsEat.viewholders.SearchResultsViewHolder;

/*

:::::NEXT:::::
TODO(Chris): Create individual item page
TODO(Chris): Fix loading of search results
TODO(Chris): Add "Load More" button to search results
TODO(Chris): Download and cache images

:::::SOON:::::
TODO(Chris): Implement spell checker, like google "Did you mean Chicken?"
TODO(Chris): Animate view items, sequentially
TODO(Chris): Cache entire query results
TODO(Chris): Create advanced search page w/ options "by brand, category, ect"
TODO(Chris): Convert Factual Driver into an "API Driver"
TODO(Chris): Integrate with Nutritionix

:::::LATER::::
TODO(Chris): Integrate with Barcode Scanner (Possibly Java Based, or ZXing)
TODO(Chris): Run multiple concurrent database calls and combine results.
TODO(Chris): Create social aspects, allow login
TODO(Chris): Implement a "Likes, Favorites, Comments" system
TODO(Chris): Implement an inventory system
TODO(Chris): Implement a recipe system, searchable by ingredients, or nutrition
*/

public class SearchResultsActivity extends Activity
        implements AsyncTaskHandler, ViewHolderHandler<SearchResultsViewHolder>, OnItemClickHandler,
        RecyclerScrollHandler {
    private ResponseList responseList;
    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;
    private ProgressBar progressBar;

    private String query;
    private boolean loading;
    private int offset = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        initProgressBar();
        initRecyclerView();
        handleSavedInstanceState(savedInstanceState);

    }
    private void initialize(){
        setContentView(R.layout.search_results);
        loading = true;
    }
    private void initProgressBar(){
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 24));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_horizontal_no_back));
        progressBar.setProgress(0);

        final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(progressBar);

        ViewTreeObserver observer = progressBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View contentView = decorView.findViewById(android.R.id.content);
                progressBar.setY(contentView.getY() - 10);

                ViewTreeObserver observer = progressBar.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(this);
            }
        });
    }
    private void initRecyclerView(){
        if(recyclerView == null){
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            int orientation = getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
            recyclerView.setOnScrollListener(new RecyclerScrollListener(this, this, layoutManager));
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
            recyclerView.setLayoutManager(layoutManager);
        }
        if(adapter != null){
            recyclerView.setAdapter(adapter);
        }

    }

    private void initAdapter(){
        if(adapter == null){
            adapter = new SearchResultsAdapter(this, responseList, R.layout.search_list_item);
            initRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    private void handleSavedInstanceState(Bundle savedInstanceState){
        if(savedInstanceState != null && savedInstanceState.containsKey(KEYS.QUERY_LIST)){
            responseList = savedInstanceState.getParcelable(KEYS.QUERY_LIST);
            initAdapter();
        } else {
            responseList = new ResponseList();
            handleIntent();
        }
    }
    private void handleIntent(){
        Intent intent = getIntent();
        query = intent.getStringExtra(KEYS.QUERY);

        if(query != null){
            executeQuery(query);
        } else {
            Intent returnIntent = new Intent(this, SearchActivity.class);
            startActivity(returnIntent);
        }
    }
    private void executeQuery(String query){
        FactualRetrievalTask task = new FactualRetrievalTask(this, this);
        Query q = new Query().search(query)
                .limit(20)
                .offset(offset);
        task.execute(q);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEYS.QUERY_LIST, responseList);
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), NutritionViewActivity.class);
        intent.putExtra(KEYS.QUERY_LIST, (android.os.Parcelable) responseList);
        intent.putExtra(KEYS.INDEX, position);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_left);
    }

    @Override
    public void updateProgress(int progress){
        progressBar.setProgress(progress);
        if(progressBar.getProgress() >= 100){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setProgress(0);

                }
            }, 800);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskComplete(List<ReadResponse> responses) {
        int size = responses.size();
        for (ReadResponse response : responses){
            int rows = response.size();
            int processed = 0;
            for (Map<String, Object> result : response.getData()) {
                responseList.add(result);
                processed++;
                offset++;
                updateProgress((processed * 100) / (rows * size));
            }
        }
        initAdapter();
    }

    @Override
    public void onScrolled(RecyclerView view, RecyclerScrollListener listener, int dx, int dy) {
        listener.visibleItemCount = listener.layoutManager.getChildCount();
        listener.totalItemCount = listener.layoutManager.getItemCount();
        listener.pastVisibleItems = listener.layoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if ((listener.visibleItemCount + listener.pastVisibleItems) >= listener.totalItemCount) {
                loading = false;
                if (listener.totalItemCount < 100) {
                    executeQuery(query);
                }
            }
        }
    }
}

