package com.example.childapp.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.childapp.services.LocationTrackingService;

public class LocationPermissionManager {

    private final Activity activity;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public LocationPermissionManager(Activity activity) {
        this.activity = activity;
    }
 //сделать отдельный класс для уведомлений, для разрешений глянуть в каком потоке работает foreground service,
    public void requestLocationPermissionAndStartService() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Если разрешение уже есть, запустим сервис и получим местоположение
            takeGeolocation();
        }
    }

    // Метод для обработки результата запроса разрешений
    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeGeolocation();
            } else {
                Toast.makeText(activity, "Разрешение на доступ к геолокации отклонено", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void takeGeolocation() {
        Toast.makeText(activity, "Получаем геолокацию", Toast.LENGTH_LONG).show();

        // Запуск сервиса для отслеживания местоположения
        Intent serviceIntent = new Intent(activity, LocationTrackingService.class);
        ContextCompat.startForegroundService(activity, serviceIntent);
    }
}

