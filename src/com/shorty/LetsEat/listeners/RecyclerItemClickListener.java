package com.shorty.LetsEat.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.shorty.LetsEat.callbacks.OnItemClickHandler;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickHandler listener;
    private GestureDetector gestureDetector;
    private Context context;

    public RecyclerItemClickListener(Context context, OnItemClickHandler listener) {
        this.context = context;
        this.listener = listener;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && listener != null && gestureDetector.onTouchEvent(motionEvent)) {
            listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }
}