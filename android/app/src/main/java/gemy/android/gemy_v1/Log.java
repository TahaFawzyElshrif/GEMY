package gemy.android.gemy_v1;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Log extends AppCompatActivity {
    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main = findViewById(R.id.layMin);
        addLog();
        addThreadLog();

    }

    private void addThreadLog() {
        MqttHandler mqttManager=new MqttHandler();
        mqttManager.connect(Utils.serverUri , Utils.clientId);

        Thread  myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mqttManager.subscribe(Utils.messagesTopic);
                mqttManager.subscribe(Utils.responsesTopic);

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // Simulate a loop to check for new messages
                        Thread.sleep(1000); // Example: Sleep for 1 second

                        // Check if new messages are available
                        String newMessage = mqttManager.new_message;
                        if ((newMessage != null)&&(newMessage .equals("finished"))) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main.addView(Utils.madeBlock(Log.this, "server", "done ,Response"), 0);
                                    mqttManager.new_message=null;
                                }
                            });
                        }else if ((newMessage != null)&&(newMessage .equals("Robot connected to server."))){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main.addView(Utils.madeBlock(Log.this, "server", "Robot connected to server."), 0);
                                    mqttManager.new_message=null;
                                }
                            });
                        }else if ((newMessage != null)&&(newMessage .equals("Robot reconnected to server after crush."))){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main.addView(Utils.madeBlock(Log.this, "server", "Robot reconnected to server after crush."), 0);
                                    mqttManager.new_message=null;
                                }
                            });
                        }

                        else if ((newMessage != null)&&(newMessage.startsWith("MQTT_SERVER_GEMY_V1: "))){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main.addView(Utils.madeBlock(Log.this, "server", newMessage), 0);
                                    mqttManager.new_message=null;
                                }
                            });
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        myThread.start();



    }

    private void addLog() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 =  database.getReference(Utils.user_name).child("error");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(! dataSnapshot.getValue(String.class).equals("None")) {//prevent None log
                    main.addView(Utils.madeBlock(Log.this, "State:", dataSnapshot.getValue(String.class)), 0);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });



    }
}