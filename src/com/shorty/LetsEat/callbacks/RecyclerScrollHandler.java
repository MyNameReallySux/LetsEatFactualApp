package com.shorty.LetsEat.callbacks;

import android.support.v7.widget.RecyclerView;
import com.shorty.LetsEat.listeners.RecyclerScrollListener;

/**
 * Created by Shorty on 3/19/2015.
 */
public interface RecyclerScrollHandler {
    public void onScrolled(RecyclerView view, RecyclerScrollListener listener, int dx, int dy);
}
