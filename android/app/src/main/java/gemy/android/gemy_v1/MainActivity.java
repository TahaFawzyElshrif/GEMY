package gemy.android.gemy_v1;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import gemy.android.*;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.filament.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MqttHandler mqttManager;
    private final String serverUri ="tcp://test.mosquitto.org:1883";
    private final String clientId = MqttClient.generateClientId();
    private final String responsesTopic = "gemy/user/responses";
    private final String messagesTopic = "gemy/user/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });



        Utils.loadSharedPrefrence(MainActivity.this);
        animLogo();
        goNextActivity();


    }



    private void goNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.is_first_time) {
                    Utils.goToActivity(MainActivity.this,Guide1.class);
                }else
                {
                    Utils.goToActivity(MainActivity.this,Menu.class);
                }
                finish();
            }
        }, 2700);
    }

    private void animLogo(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.pic).startAnimation(animation);
    }




}

