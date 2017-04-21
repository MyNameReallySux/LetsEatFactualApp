package com.shorty.LetsEat.callbacks;

import com.shorty.LetsEat.viewholders.SearchResultsViewHolder;

import java.util.Map;

public interface ViewHolderHandler<T> {
    public void populateView(Map<String, Object> item, T viewHolder);
}
