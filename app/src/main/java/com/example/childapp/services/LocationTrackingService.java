package com.example.childapp.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.childapp.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationTrackingService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startForeground(1, buildForegroundNotification());
            startLocationUpdates(); // Запуск обновлений местоположения
        } else {
            Log.e("LocationTrackingService", "Location permission not granted.");
            // При необходимости инициируйте запрос разрешений через Activity, запустившую сервис
        }

        return START_STICKY;
    }

    private Notification buildForegroundNotification() {
        // Создание уведомления
        String channelId = "location_channel_id";
        NotificationChannel channel = new NotificationChannel(channelId, "Location Tracking", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Tracking Location")
                .setContentText("Tracking your location in the background")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .build();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    Log.d("LocationTrackingService", "Location: " + location.getLatitude() + ", " + location.getLongitude());
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("LocationTrackingService", "Permission not granted for location updates.", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Останавливаем обновления местоположения при завершении работы сервиса
        fusedLocationClient.removeLocationUpdates(locationCallback);
        stopForeground(true);  // Останавливаем foreground-сервис
    }


}



