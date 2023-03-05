package com.example.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;
public class NewAppWidget extends AppWidgetProvider {
    private final static String mSharedPrefFile = "com.example.android.appwidget"; //menyimpan data diplikasi dengan package ini agar tidak hilang
    private final static String COUNT_KEY = "count"; //inisialisasi untuk count yang akan bertambah

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        SharedPreferences prefs = context.getSharedPreferences(mSharedPrefFile, 0); //menggunakan shared pref dgn parameter data dan value count
        int count = prefs.getInt(COUNT_KEY+appWidgetId, 0); //variabel count yang akan bertambah saat jumlah widget bertambah (default 0)
        count++;

        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()); //menampilkan waktu

        //membuat object (widget) yang akan ditampilkan
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.appwidget_update, dateString);
        views.setTextViewText(R.id.appwidget_count, String.valueOf(count));


        Intent intent = new Intent(context, NewAppWidget.class); //mengarahkan intent ke class NewAppWidget saat widget diklik
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[] {appWidgetId}; //set action untuk menambah value id di widget
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        //get broadcast disini fungsinya untuk mengupdate perubahan (mengubah isi widget saat tombol update di klik)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.button_update, pendingIntent);

        //untuk menyimpan informasi suatu aplikasi
        SharedPreferences.Editor prefEditor = prefs.edit(); //mengedit shared pref
        prefEditor.putInt(COUNT_KEY+appWidgetId, count); //menambah count untuk widget
        prefEditor.apply(); //menyimpan count yang sudah diincrement



        // update widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //mengupdate widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}