package gemy.android.gemy_v1;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Faq extends AppCompatActivity {
    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main = findViewById(R.id.layMin);
        addSomeQuestion();
    }

    private void addSomeQuestion() {
        main.addView(Utils.madeBlock(Faq.this,
                "What is valid chars?",
                "This version support input from just the 4*4 keypad which make limit on input ,for this use chars from A-C capital (inclusive" +
                        "),or * ,or # or 1-9 ,where D is enter" ));
        main.addView(Utils.madeBlock(Faq.this,
                "Do not make account:",
                "try use large password ,of 6 chars at least" ));
        main.addView(Utils.madeBlock(Faq.this,
                "Some slow in response:",
                "if not take long ,Normal please wait " ));
        main.addView(Utils.madeBlock(Faq.this,
                "not read my  language?",
                "you must go to settings and select language you need ,currently support ar ,en-US ,fr  " ));
        main.addView(Utils.madeBlock(Faq.this,
                "Touch/sensors delay?",
                "try to be with it for seconds ,for touch try to hold the metal wire (ring)" ));
        main.addView(Utils.madeBlock(Faq.this,
                "Settings not apply?",
                "For this version ,some settings won't apply ,and for ARABIC may not display correctly" ));
        main.addView(Utils.madeBlock(Faq.this,
                "Removed logged in user in the robot?",
                "For this version ,You can not do this." ));


    }
}