package gemy.android.gemy_v1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class message extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    ImageButton record;
    Button send;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        record=findViewById(R.id.record);
        send=findViewById(R.id.send);
        send.setEnabled(true);
        text=findViewById(R.id.text);
        setMicToListen();
        setSendMessage();
        //setStopOnRecord();
    }

    private void setStopOnRecord() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 =  database.getReference(Utils.user_name).child("record");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean recording=dataSnapshot.getValue(Boolean.class);
                send.setEnabled(recording);//just send message when state is recording
                send.setText(recording?"Send the message":"wait until response");
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void setSendMessage() {

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MqttHandler mqttManager=new MqttHandler();
                mqttManager.connect(Utils.serverUri , Utils.clientId);
                mqttManager.subscribe(Utils.messagesTopic);
                mqttManager.publish(Utils.messagesTopic,text.getText().toString());
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference langRef = database.getReference(Utils.user_name).child("record");
                //langRef.setValue(false);

            }
        });
    }

    private void setMicToListen() {
        record.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Utils.spokenLanguage);//Locale.getDefault());

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(message.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                text.setText(Objects.requireNonNull(result).get(0));

            }
        }
    }

}