package com.shorty.LetsEat.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;
import com.shorty.LetsEat.callbacks.AsyncTaskHandler;

import java.util.List;



public class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
    Factual factual;
    Context context;
    AsyncTaskHandler asyncTaskContext;

    public static final String FACTUAL_KEY = "YlaDxChIdtHE84qNqVDb4j7epo7286KmHEndh8kb";
    public static final String FACTUAL_SECRET = "qRjtNEIlqMy2TZCePcw3AXZd9JvTRtmDg7JAtQ5k";

    public FactualRetrievalTask(Context context, AsyncTaskHandler asyncTaskContext){
        super();
        this.context = context;
        this.factual = new Factual(FACTUAL_KEY, FACTUAL_SECRET);
        this.asyncTaskContext = asyncTaskContext;
    }

    @Override
    protected List<ReadResponse> doInBackground(Query... params) {
        List<ReadResponse> results = Lists.newArrayList();
        for (Query q : params) {
            results.add(factual.fetch("products-cpg-nutrition", q));
        }
        return results;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        asyncTaskContext.updateProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(List<ReadResponse> responses) {
        asyncTaskContext.onTaskComplete(responses);
    }
}
