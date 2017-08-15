package com.madao.appbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        OnActionExpandListener expandListener = new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                if (getActionBar() != null) {
                    getActionBar().setDisplayHomeAsUpEnabled(false);
                }

                return true;     //Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if (getActionBar() != null) {
                    getActionBar().setDisplayHomeAsUpEnabled(false);
                }

                return true;     //Return true to collapse action view
            }
        };

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnActionExpandListener(expandListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_favorite:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
