package gemy.android.gemy_v1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    EditText username,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        login=findViewById(R.id.login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        setLogIn();
    }

    private void setLogIn() {
    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String user=username.getText().toString();
            String pass=password.getText().toString();
            String email=user+Utils.email_part;
            try{
                Utils.authenticateAccount(Login.this,email,pass);
                Utils.setEmailPassword(Login.this, user, pass, email);
                Utils.goToActivity(Login.this, Finish_Setup.class);
            }catch (Exception ex){
                Utils.showDialog(Login.this,"Can not login ,check your data");
            }
        }
    });
    }
}