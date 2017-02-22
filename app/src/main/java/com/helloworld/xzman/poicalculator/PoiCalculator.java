package com.helloworld.xzman.poicalculator;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(23)
public class PoiCalculator extends AppCompatActivity {
    private static final String[] keyboard = {
            "AC", "(", ")", "^",
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            ".", "0", "=", "/"
    };

    private static TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_calculator);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result = (TextView)findViewById(R.id.result);

        final TextUpdater updater = new TextUpdater(result);

        final GridLayout root = (GridLayout)findViewById(R.id.root);
        for (int i = 0; i < keyboard.length; i++) {
            final Button button = new Button(this);
            button.setText(keyboard[i]);
            //button.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            button.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            View.OnClickListener onClick = new View.OnClickListener() {
                private final TextUpdater update = updater;
                @Override
                public void onClick(View v) {
                    try {
                        update.updateText(button.getText());
                    }
                    catch (Exception e) {
                        result.setText("Exception: " + e.getMessage());
                    }
                }
            };
            button.setOnClickListener(onClick);
            root.addView(button);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi_calculator, menu);
        return true;
    }

    private void AlertPoi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Poi!")
                .setTitle("Poi?")
                .setCancelable(true)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about:
                // initializeResult();
                Toast.makeText(PoiCalculator.this, R.string.aboutText, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setting:
                startActivity(new Intent(this, PoiSetting.class));
                return true;
            case R.id.action_poi:
                AlertPoi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
