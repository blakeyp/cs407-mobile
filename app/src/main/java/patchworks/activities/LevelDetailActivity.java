package patchworks.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import patchworks.R;

public class LevelDetailActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {   // override back button to go back to levels tab
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Lots of Sweets!");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            class RemoveListener implements View.OnClickListener {
                public void onClick(View v) {
                    fab.show();
                }
            }

            @Override
            public void onClick(View view) {
                fab.hide();
                Snackbar.make(view, "Added to Queue!", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new RemoveListener()).show();
            }
        });

    }

}
