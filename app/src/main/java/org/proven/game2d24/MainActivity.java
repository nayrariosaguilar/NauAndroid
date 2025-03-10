package org.proven.game2d24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PilotesView pilotesView = (PilotesView) findViewById(R.id.gameview);

//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pilotesView.postInvalidate(); // onDraw
//                pilotesView.move();
//            }
//        });
        ThreadGame thread = new ThreadGame(pilotesView);
        thread.start();
    }
}