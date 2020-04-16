package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ActActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);
        ImageView sleep = (ImageView)findViewById(R.id.imageLocationStep);
        //mScaleGestureDetector = new ScaleGestureDetector(this, new sleepActivity.ScaleListener());
        sleep.setClickable(true);
        sleep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openActivityAct();
            }
        });
        //mImageView=(ImageView)findViewById(R.id.imageView);


    }



    public void openActivityAct()
    {
        Intent intent = new Intent(this, ActActivityGraphs.class);
        startActivity(intent);
    }

}
