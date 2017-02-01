package com.aymensoft.mywidget;

import android.appwidget.AppWidgetManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    EditText etNom;
    Button config;

    private MainActivity context;
    private int widgetID;
    AppWidgetManager appWidgetManager;
    RemoteViews views;
    //SharedPreferences
    private static SharedPreferences sharedpreferences;
    String mywidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNom=(EditText)findViewById(R.id.et_nom);
        config=(Button)findViewById(R.id.btn_config);
        setResult(RESULT_CANCELED);
        //get WIDGETID
        sharedpreferences = getSharedPreferences("MyWigetsID", MODE_PRIVATE);
        mywidget=sharedpreferences.getString("WIDGETID", null);

        try {
            widgetID=Integer.valueOf(mywidget);
        }catch (NumberFormatException ignored){}

        context=this;

        appWidgetManager=AppWidgetManager.getInstance(context);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.test_widget);

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                views.setTextViewText(R.id.appwidget_text, etNom.getText().toString());
                appWidgetManager.updateAppWidget(widgetID, views);
            }
        });

    }
}
