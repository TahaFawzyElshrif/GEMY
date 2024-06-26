package gemy.android.gemy_v1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Menu extends AppCompatActivity {
    ImageButton info,help,message,log,settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        info=findViewById(R.id.about);
        help=findViewById(R.id.help);
        message=findViewById(R.id.ask);
        log=findViewById(R.id.logs);
        settings=findViewById(R.id.settings);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(Menu.this,message.class);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.goToActivity(Menu.this,settings.class);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Utils.goToActivity(Menu.this,Log.class);}
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Utils.goToActivity(Menu.this,Faq.class);}
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Utils.goToActivity(Menu.this,about.class);}
        });


    }


}