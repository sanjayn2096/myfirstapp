package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ImageView sleep = (ImageView)findViewById(R.id.imageSleep);
        sleep.setClickable(true);
        ImageView Act = (ImageView)findViewById(R.id.imageAct);
        Act.setClickable(true);
        ImageView sentiments = (ImageView)findViewById(R.id.imageSentiments);
        sentiments.setClickable(true);
        ImageView driving = (ImageView)findViewById(R.id.imageDriving);
        driving.setClickable(true);
        
        sleep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openActivitySleep();
            }
        });

        sentiments.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openActivitySentiments();
            }
        });
        driving.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openActivityDriving();
            }
        });
        Act.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openActivityAct();
            }
        });
        
    }

    private void openActivityAct() {
        Intent intent = new Intent(this, ActActivity.class);
        startActivity(intent);
    }

    private void openActivityDriving() {

        Intent intent = new Intent(this, drivingActivity.class);
        startActivity(intent);
    }

    private void openActivitySentiments() {
        Intent intent = new Intent(this, sentimentsActivity.class);
        startActivity(intent);
    }

    public void openActivitySleep()
    {
        Intent intent = new Intent(this, sleepActivity.class);
        startActivity(intent);
    }


//    public void openActivity2() {
////        EditText editText1 = (EditText) findViewById(R.id.edittext1);
////        String text = editText1.getText().toString();
////
////        EditText editText2 = (EditText) findViewById(R.id.edittext2);
////        int number = Integer.parseInt(editText2.getText().toString());
//
//        Intent intent = new Intent(this, ReportActivity.class);
////        intent.putExtra(EXTRA_TEXT, text);
////        intent.putExtra(EXTRA_NUMBER, number);
//        startActivity(intent);
//    }
}
