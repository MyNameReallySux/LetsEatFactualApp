package com.shorty.LetsEat.listeners;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.shorty.LetsEat.callbacks.RecyclerScrollHandler;

public class RecyclerScrollListener extends RecyclerView.OnScrollListener {
    public Context context;
    public LinearLayoutManager layoutManager;
    public RecyclerScrollHandler handler;

    public int pastVisibleItems,
        visibleItemCount,
        totalItemCount;

    public RecyclerScrollListener(Context context, RecyclerScrollHandler handler, LinearLayoutManager layoutManager){
        this.context = context;
        this.handler = handler;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        handler.onScrolled(recyclerView, this, dx, dy);
    }
}
