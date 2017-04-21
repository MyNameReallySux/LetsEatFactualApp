package com.shorty.LetsEat.callbacks;

import com.factual.driver.ReadResponse;

import java.util.List;

public interface AsyncTaskHandler {
    public void updateProgress(int progress);
    public void onTaskComplete(List<ReadResponse> responses);
}
