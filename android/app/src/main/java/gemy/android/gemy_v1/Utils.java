package gemy.android.gemy_v1;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class Utils {
    public static final String serverUri ="tcp://test.mosquitto.org:1883";
    public static final String clientId = MqttClient.generateClientId();
    public static final String responsesTopic = "gemy/user/responses";
    public static final String messagesTopic = "gemy/user/messages";

    public static boolean is_first_time=true;
    public static String user_name="";
    public static String user_password="";
    public static String email_part="@gemy.com";
    public static String email="";
    public static String spokenLanguage="en-US";//"ar","fr"
    public static void goToActivity(AppCompatActivity from, Class dir) {
        if (from == null || dir == null) {
            Log.e("error", "null page");
            return;
        } else {
            Intent go = new Intent(from, dir);
            from.startActivity(go);
        }
    }
    public static void registerNewUser(AppCompatActivity activ, String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( activ, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showDialog(activ,"User Created");
                        } else {
                            showDialog(activ,"Can not create");

                        }
                    }
                });


    }

    public static void showDialog(AppCompatActivity currentActivity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void setEmailPassword(AppCompatActivity currentActivity,String user,String pass,String mail) {
        email=mail;
        user_name=user;
        user_password=pass;
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("gemy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("user_name", user_name);
        editor.putString("user_password", user_password);

    }


    public static void endFirstTime(AppCompatActivity currentActivity) {
        is_first_time=false;
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("gemy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_first_time", false);
    }

    public static void loadSharedPrefrence(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("gemy", MODE_PRIVATE);
        user_name = sharedPreferences.getString("user_name", "");
        user_password = sharedPreferences.getString("user_password", "");
        email = sharedPreferences.getString("email", "");
        is_first_time = sharedPreferences.getBoolean("is_first_time", true);

    }
}
