package com.example.childapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import com.example.childapp.managers.LocationPermissionManager;

public class MainActivity extends AppCompatActivity {
    private LocationPermissionManager locationPermissionManager;
    private static final String CHANNEL_ID = "test_notification_channel";
private Button accessGeolocBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        locationPermissionManager = new LocationPermissionManager(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        showTestNotification("Test Notification", "This is a test notification.");
accessGeolocBtn=findViewById(R.id.acessGeolocBtn);

accessGeolocBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        locationPermissionManager.requestLocationPermissionAndStartService();
    }
});
    }//end of onCreate
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionManager.handlePermissionResult(requestCode, permissions, grantResults);
    }





    private void showTestNotification(String title, String text) {
        // Создаем NotificationChannel (только для Android 8.0 и выше)
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Test Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        // Создаем и отправляем уведомление
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // или любой другой значок
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);  // Отправка уведомления с уникальным ID
        }
    }







}// END OF CLASS






//            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });