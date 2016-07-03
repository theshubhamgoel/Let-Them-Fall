package games.shubham.com.letthemfall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends Activity {
    Button easybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        easybtn = (Button) findViewById(R.id.easybtn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //getting highScore
        SharedPreferences prefs = this.getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        int easyHighScore = prefs.getInt("easyHighScore", 0); //0 is the default value
        easybtn.setText("EASY\nBest : " + easyHighScore);

    }

    public void startEasyLevel(View v) {
        Intent i = new Intent(this, EasyLevel.class);
        startActivity(i);
    }
}
