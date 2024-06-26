package gemy.android.gemy_v1;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class Utils {
    public static final String serverUri ="tcp://test.mosquitto.org:1883";
    public static final String clientId = MqttClient.generateClientId();
    public static final String responsesTopic = "gemy/user/responses";
    public static final String messagesTopic = "gemy/user/messages";

    public static boolean is_first_time=true;
    public static String user_name;//="r";
    public static String user_password;//="rr112233";
    public static String email_part="@gemy.com";
    public static String email;//="r@gemy.com";
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
        editor.apply();

    }


    public static void endFirstTime(AppCompatActivity currentActivity) {
        is_first_time=false;
        SharedPreferences sharedPreferences = currentActivity.getSharedPreferences("gemy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_first_time", false);
        editor.apply();
    }

    public static void loadSharedPrefrence(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("gemy", MODE_PRIVATE);
        user_name = sharedPreferences.getString("user_name", "");
        user_password = sharedPreferences.getString("user_password", "");
        email = sharedPreferences.getString("email", "");
        is_first_time = sharedPreferences.getBoolean("is_first_time", true);
        spokenLanguage=sharedPreferences.getString("spokenLanguage","en-US");
    }
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static CardView madeBlock(AppCompatActivity activ, String title , String message){
        CardView blockCard=new CardView(activ);
        LinearLayout.LayoutParams paramsCard = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        paramsCard.setMargins(20,10,20,10);
        blockCard.setLayoutParams(paramsCard);
        blockCard.setRadius(dpToPx(activ, 19));
        blockCard.setCardElevation(dpToPx(activ, 19));


        LinearLayout block=new LinearLayout(activ);
        block.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        block.setLayoutParams(params);


        //Title
        TextView titleTxt=new TextView(activ);
        LinearLayout.LayoutParams textparams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleTxt.setLayoutParams(textparams);
        titleTxt.setText("\t"+title); //" " just nice look start
        titleTxt.setTextSize(16);
        titleTxt.setTextColor(Color.parseColor("#94D8DE"));
        titleTxt.setBackgroundResource(R.drawable.grad3);
        block.addView(titleTxt);

        //message
        TextView messageTxt=new TextView(activ);
        LinearLayout.LayoutParams messageparams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        messageTxt.setLayoutParams(messageparams);
        messageTxt.setText("\t"+message);
        messageTxt.setTextSize(16);
        messageTxt.setTextColor(Color.parseColor("#94D8DE"));
        messageTxt.setBackgroundResource(R.drawable.grad_ans);
        block.addView(messageTxt);

        blockCard.addView(block);
        return blockCard;
    }

    public static void createDefaultKeys() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference(Utils.user_name).child("error");
        Ref.setValue("None");
        Ref = database.getReference(Utils.user_name).child("helloCond");
        Ref.setValue(true);
        Ref = database.getReference(Utils.user_name).child("language");
        Ref.setValue("en-US");
        Ref = database.getReference(Utils.user_name).child("record");
        Ref.setValue(true);
        Ref = database.getReference(Utils.user_name).child("speakerCond");
        Ref.setValue(true);
        //Ref = database.getReference(Utils.user_name).child("clear_pass");
        //Ref.setValue(true);
        //Ref = database.getReference(Utils.user_name).child("clear_session");
        //Ref.setValue(true);
    }


    public static void authenticateAccount(AppCompatActivity context,String email,String password) throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //try {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Toast.makeText(context, "Authentication Successful.", Toast.LENGTH_SHORT).show();

                            }catch(Exception ex){
                                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
