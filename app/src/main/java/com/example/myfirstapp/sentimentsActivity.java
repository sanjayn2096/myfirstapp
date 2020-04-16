package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class sentimentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiments);
        ImageView sleep = (ImageView) findViewById(R.id.imageSentimentsPie);

        sleep.setClickable(true);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySentimentsGraph();
            }
        });
        //mImageView=(ImageView)findViewById(R.id.imageView);

    }

    public void openActivitySentimentsGraph() {
        Intent intent = new Intent(this, SentimentsGraphsActivity.class);
        startActivity(intent);
    }
}

