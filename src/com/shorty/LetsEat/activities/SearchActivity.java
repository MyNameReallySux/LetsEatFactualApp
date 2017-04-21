package com.shorty.LetsEat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.shorty.LetsEat.R;
import com.shorty.LetsEat.utilities.KEYS;

public class SearchActivity extends Activity {
    EditText searchBox;
    LinearLayout rootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    public void initialize(){
        setContentView(R.layout.search);

        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
        rootLayout = (LinearLayout)findViewById(R.id.rootLayout);
        searchBox = (EditText)findViewById(R.id.searchBoxEditText);
        searchBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    onSubmitSearchResults(view);
                }
                return false;
            }
        });
    }

    public void onSubmitSearchResults(View view){
        String query = searchBox.getText().toString();

        if(query != null){
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(KEYS.QUERY, query);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            Toast.makeText(this,
                    "The search box is blank! Please enter a valid query!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}