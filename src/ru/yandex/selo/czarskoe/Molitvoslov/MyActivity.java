package ru.yandex.selo.czarskoe.Molitvoslov;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyActivity extends Activity {
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/HirmosPonomar.ttf");

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);

        mButton1.setTypeface(myTypeface);
        mButton1.setText(Html.fromHtml("<font color='red'><big><big><big>М</big></big></big></font>ѡли́твы ѹ҆́треннїѧ"));
        mButton2.setTypeface(myTypeface);
        mButton2.setText(Html.fromHtml("<font color='red'><big><big><big>М</big></big></big></font>ѡли́твы на со́нъ грѧдꙋ́щымъ"));
        mButton3.setText(Html.fromHtml("<font color='red'><big><big><big>М</big></big></big></font>олитвы утренние"));
        mButton4.setText(Html.fromHtml("<font color='red'><big><big><big>М</big></big></big></font>олитвы на сон грядущим"));

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ActivityText.class);
                Bundle b = new Bundle();
                b.putInt("key", 0);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        mButton1.setOnClickListener(listener1);

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ActivityText.class);
                Bundle b = new Bundle();
                b.putInt("key", 1);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        mButton2.setOnClickListener(listener2);

        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ActivityText.class);
                Bundle b = new Bundle();
                b.putInt("key", 2);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        mButton3.setOnClickListener(listener3);

        View.OnClickListener listener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ActivityText.class);
                Bundle b = new Bundle();
                b.putInt("key", 3);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        mButton4.setOnClickListener(listener4);

    }
}
