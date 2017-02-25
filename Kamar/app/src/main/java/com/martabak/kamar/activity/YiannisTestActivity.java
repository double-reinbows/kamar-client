package com.martabak.kamar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.martabak.kamar.R;


public class YiannisTestActivity extends AbstractCustomFontActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiannis_test);

        final Button doSomething1Button = (Button) findViewById(R.id.doSomething1);

        doSomething1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = null;
                // force a NPE to test uncaught exception handler
                s.toUpperCase();

//                EmailSender.getInstance(getApplicationContext())
//                        .sendEmail("Test", "Bleh", "y.phillipou@gmail.com");
            }
        });
    }

}
