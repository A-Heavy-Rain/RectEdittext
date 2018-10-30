package com.zzb.rectedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RectEditText rectEditText=findViewById(R.id.rect_input);
        rectEditText.setOnCompleteListener(new RectEditText.OnCompleteListener() {
            @Override
            public void OnComplete(String text) {
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
