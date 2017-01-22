package app.racdeveloper.com.bencolconnect.pushNotification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import app.racdeveloper.com.bencolconnect.R;

public class NotificationViewActivity extends AppCompatActivity {
    String title;
    String text;
    TextView txttitle;
    TextView txtmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        // Retrive the data from MainActivity.java
        Intent i = getIntent();

        title = i.getStringExtra("title");
        text = i.getStringExtra("content");
        Log.d("debugtest",text);

        // Locate the TextView
        txttitle = (TextView) findViewById(R.id.notificationTitle);
        txtmessage = (TextView) findViewById(R.id.notificationMessage);

        // Set the data into TextView
        txttitle.setText(title);
        txtmessage.setText(text);

    }
}
