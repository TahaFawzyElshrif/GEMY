package gemy.android.gemy_v1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class settings extends AppCompatActivity {
    AutoCompleteTextView lang;
    String[] langs = {"en-US","ar","fr"};
    Switch hello,speaker;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        send=findViewById(R.id.send);
        lang = findViewById(R.id.lang);
        hello=findViewById(R.id.hello);
        speaker=findViewById(R.id.speaker);
        setlang();
        setSpeakerEnable();
        setSend();


    }

    private void setSpeakerEnable() {
        // make default ,same state as in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 =  database.getReference(Utils.user_name).child("helloCond");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hello.setChecked(dataSnapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
        myRef2 =  database.getReference(Utils.user_name).child("speakerCond");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               speaker.setChecked(dataSnapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });



        if (!hello.isChecked()){// on first open may not work so code to handle it
            speaker.setEnabled(false);
            speaker.setChecked(false);//just to prevent other parts of code to give unexpected results

        }

        //as logic stop speaker when no hello
        hello.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enabled) {
                    speaker.setEnabled(enabled);
                    if(!enabled){
                        speaker.setChecked(false);
                    }
            }
        });

    }
    private void setSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //hello
                DatabaseReference helloRef = database.getReference(Utils.user_name).child("helloCond");
                helloRef.setValue(hello.isChecked());

                //speaker Condition
                DatabaseReference speakerRef = database.getReference(Utils.user_name).child("speakerCond");
                speakerRef.setValue(speaker.isChecked());

                //language
                String selected_lng=lang.getText().toString();
                if (selected_lng.equals("ar") || selected_lng.equals("en-US") || selected_lng.equals("fr")) {
                    //---->save settings in app current memory
                    Utils.spokenLanguage=selected_lng;

                    //------>shared prefrences
                    SharedPreferences sharedPreferences = getSharedPreferences("gemy", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("spokenLanguage", selected_lng);

                    //-----> save in firebase
                    DatabaseReference langRef = database.getReference(Utils.user_name).child("language");
                    langRef.setValue(lang.getText().toString());


                }

            }
        });
    }

    private void setlang() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, langs);
        lang.setAdapter(adapter);
        lang.setThreshold(1);


    }
}