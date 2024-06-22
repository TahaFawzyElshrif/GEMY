package gemy.android.gemy_v1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }

}