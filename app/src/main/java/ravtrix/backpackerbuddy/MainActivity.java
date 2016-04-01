package ravtrix.backpackerbuddy;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBackpacker, tvBuddy;
    private ImageButton imgbSignUp, imgbLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        tvBackpacker = (TextView) findViewById(R.id.tvBackpacker);
        tvBuddy = (TextView) findViewById(R.id.tvBuddy);
        imgbSignUp = (ImageButton) findViewById(R.id.imgbSignUp);
        imgbLogIn = (ImageButton) findViewById(R.id.imgbLogIn);

        Typeface monuFont = Typeface.createFromAsset(getAssets(), "Monu.otf");

        tvBackpacker.setTypeface(monuFont);
        tvBackpacker.setTextSize(85);
        tvBuddy.setTypeface(monuFont);
        tvBuddy.setTextSize(80);
        imgbSignUp.setOnClickListener(this);
        imgbLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbSignUp:
                startActivity(new Intent(this, SignUpPart1Activity.class));
                break;
            case R.id.imgbLogIn:
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
    }
}
