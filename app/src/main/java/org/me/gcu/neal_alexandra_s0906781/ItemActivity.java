//Student Name: Alexandra Neal
//Student ID: S0906781

package org.me.gcu.neal_alexandra_s0906781;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {
    TextView view_title;
    TextView view_description;
    TextView view_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Removes the Top Bar from the App to make it look cleaner
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        view_title = findViewById(R.id.roadworkLocation);
        view_description = findViewById(R.id.moreInfo);
        view_duration = findViewById(R.id.roadworkDuration);

        String title = intent.getExtras().getString("title");
        String dur = intent.getExtras().getString("dur");
        String description = intent.getExtras().getString("desc0");

        view_title.setText(title);
        view_description.setText(Html.fromHtml(description));
        view_duration.setText(dur);
    }

        public void onClick (View view){
        }
    }



