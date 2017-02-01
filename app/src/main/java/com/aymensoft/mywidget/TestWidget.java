package com.aymensoft.mywidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TestWidgetConfigureActivity TestWidgetConfigureActivity}
 */
public class TestWidget extends AppWidgetProvider {

    //SharedPreferences
    private static SharedPreferences sharedpreferences;
    public static final String WIDGETPREFERENCES = "MyWigetsID" ;
    public static final String WIDGETID = "WIDGETID" ;

    private static final String MyPlayClick = "MyPlayClickTag";
    private static final String MyPauseClick = "MyPauseClickTag";
    private static final String MyStopClick = "MyStopClick";

    public static final String MP3SOUND = "https://www.freesound.org/data/previews/151/151337_1677-lq.mp3";
    public static MediaPlayer mediaPlayer ;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Toast.makeText(context, "updateAppWidget", Toast.LENGTH_SHORT).show();

        CharSequence widgetText = TestWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_widget);

        //open activity from widget
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.btn_open, pendingIntent);

        //get actions from widget
        views.setOnClickPendingIntent(R.id.btn_play, getPendingSelfIntent(context, MyPlayClick));
        views.setOnClickPendingIntent(R.id.btn_pause, getPendingSelfIntent(context, MyPauseClick));
        views.setOnClickPendingIntent(R.id.btn_stop, getPendingSelfIntent(context, MyStopClick));



        //changet widget text
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        //save widget id to update it from activity
        sharedpreferences =context.getSharedPreferences(WIDGETPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(WIDGETID, String.valueOf(appWidgetId));
        editor.apply();
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            //prepare media player
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(MP3SOUND);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MyPlayClick.equals(intent.getAction())){
            Toast.makeText(context, "MyPlayClick", Toast.LENGTH_SHORT).show();
            PlayMusic();
        }else if (MyPauseClick.equals(intent.getAction())){
            Toast.makeText(context, "MyPauseClick", Toast.LENGTH_SHORT).show();
            PauseMusic();
        }else if (MyStopClick.equals(intent.getAction())){
            Toast.makeText(context, "MyStopClick", Toast.LENGTH_SHORT).show();
            StopMusic();
        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, TestWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TestWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }

    public void PlayMusic(){
        mediaPlayer.start();
    }

    public void PauseMusic(){
        mediaPlayer.pause();
    }

    public void StopMusic(){
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(MP3SOUND);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

