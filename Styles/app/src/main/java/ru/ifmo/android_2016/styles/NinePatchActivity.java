package ru.ifmo.android_2016.styles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NinePatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_patch);
    }

    public void onButtonClick(View view) {
        Button b = (Button) view;
        CharSequence t = b.getText();
        b.setText(t + " " + t);
    }
}
