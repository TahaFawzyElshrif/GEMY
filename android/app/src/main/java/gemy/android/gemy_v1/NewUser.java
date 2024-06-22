package gemy.android.gemy_v1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUser extends AppCompatActivity {
    EditText name, password;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        create = findViewById(R.id.create);
        name = findViewById(R.id.username);
        password = findViewById(R.id.password);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String VALID_INPUT_PATTERN = "[0-9A-C*#]+";
                if (!(name.getText().toString().matches(VALID_INPUT_PATTERN)) || (!password.getText().toString().matches(VALID_INPUT_PATTERN))) {
                    Utils.showDialog(NewUser.this, "Not Valid values ,use from 0-p A-C capital , * #");
                } else {
                    String email = name.getText().toString() + Utils.email_part;
                    FirebaseApp.initializeApp(NewUser.this);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password.getText().toString())//------------>Must be valid to be saven
                            .addOnCompleteListener(NewUser.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Utils.showDialog(NewUser.this, "User Created");
                                        Utils.setEmailPassword(NewUser.this,name.getText().toString(),password.getText().toString(),email);
                                        Utils.goToActivity(NewUser.this,Finish_Setup.class);
                                    } else {
                                        Utils.showDialog(NewUser.this, "Can not create ,try again ,you may use weak password ?");

                                    }
                                }
                            });

                }
            }
        });
    }
}