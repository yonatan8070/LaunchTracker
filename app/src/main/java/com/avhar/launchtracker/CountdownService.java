package com.avhar.launchtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.avhar.launchtracker.data.Launch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CountdownService extends Service {
  private static final String CHANNEL_ID = "Countdown notifications";
  private boolean enabled;

  public CountdownService() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    enabled = true;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Launch launch = (Launch) intent.getSerializableExtra("LaunchData");
    assert launch != null;

    createNotificationChannel();
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setContentTitle(launch.getName())
                                        .setContentText("")
                                        .setSmallIcon(R.drawable.ic_loading_120)
                                        .setContentIntent(pendingIntent)
                                        .build();
    startForeground(1, notification);

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    SimpleDateFormat countdownFormat = new SimpleDateFormat("'T-' DD : HH : mm : ss", Locale.getDefault());

    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        long now = new Date().getTime();
        long timeUntilLaunch = launch.getNet().getTime() - now;

        Notification notificationUpdate = new NotificationCompat.Builder(CountdownService.this, CHANNEL_ID)
                                                  .setContentTitle(launch.getName())
                                                  .setContentText(countdownFormat.format(new Date(timeUntilLaunch + 1000)))
                                                  .setSmallIcon(R.drawable.ic_loading_120)
                                                  .setContentIntent(pendingIntent)
                                                  .build();

        notificationManager.notify(1, notificationUpdate);
        if (enabled) handler.postDelayed(this, 1000 - (now % 1000));
      }
    }, 0);
    //do heavy work on a background thread
    //stopSelf();
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    enabled = false;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void createNotificationChannel() {
    NotificationChannel serviceChannel = new NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
    );
    NotificationManager manager = getSystemService(NotificationManager.class);
    manager.createNotificationChannel(serviceChannel);
  }
}