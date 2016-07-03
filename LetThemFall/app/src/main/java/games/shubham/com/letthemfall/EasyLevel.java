package games.shubham.com.letthemfall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EasyLevel extends Activity {
    ImageView[] listOfimg = new ImageView[6];
    ImageView player;
    TextView scoretxtview, gameOvertxtView;
    RelativeLayout.LayoutParams playerLayoutparam;
    boolean gameRunnng, playerMoving;
    int speed, playerMargin;
    int score;
    int playerPosition;
    int screenWidthDp;
    GameLoop gameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_level);

        listOfimg[0] = (ImageView) findViewById(R.id.hurdle1);
        listOfimg[1] = (ImageView) findViewById(R.id.hurdle2);
        listOfimg[2] = (ImageView) findViewById(R.id.hurdle3);
        listOfimg[3] = (ImageView) findViewById(R.id.hurdle4);
        listOfimg[4] = (ImageView) findViewById(R.id.hurdle5);
        listOfimg[5] = (ImageView) findViewById(R.id.hurdle6);

        scoretxtview = (TextView) findViewById(R.id.scoreId);
        gameOvertxtView = (TextView) findViewById(R.id.gameOver);
        player = (ImageView) findViewById(R.id.easyPlayer);

        Configuration configuration = this.getResources().getConfiguration();
        screenWidthDp = configuration.screenWidthDp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameLoop = new GameLoop();

        score = 0;
        scoretxtview.setText("Score  : 0");
        gameRunnng = true;

        playerMoving = false;
        playerMargin = 0;
        playerPosition = 0;
        player.setImageResource(R.drawable.playerleft);
        playerLayoutparam = (RelativeLayout.LayoutParams) player.getLayoutParams();
        playerLayoutparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        playerLayoutparam.setMargins(0, 150, 0, 0);
        player.setLayoutParams(playerLayoutparam);
        speed = 8;

        gameOvertxtView.setVisibility(View.INVISIBLE);
        gameLoop.execute("");
    }

    @Override
    protected void onPause() {
        gameRunnng = false;
        super.onPause();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.ACTION_UP == event.getActionMasked() && playerMargin == 0 && gameRunnng) {
            playerMargin = 800;
            playerMoving = true;
        }
        return true;
    }

    private boolean iscollision() {

        Rect R1 = new Rect(player.getLeft(), player.getTop() + 15, player.getRight(), player.getBottom() - 15);
        for (int i = 0; i < listOfimg.length; i++) {
            Rect R2 = new Rect(listOfimg[i].getLeft(), listOfimg[i].getTop(), listOfimg[i].getRight(), listOfimg[i].getBottom());

            if (R1.intersect(R2)) {
                gameRunnng = false;
                gameOvertxtView.setVisibility(View.VISIBLE);
                updateHighScore();
                gameLoop.cancel(true);
                return true;
            }
        }
        return false;
    }

    private void updateHighScore() {
        //getting preferences
        SharedPreferences prefs = this.getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        int highscore = prefs.getInt("easyHighScore", 0); //0 is the default value

        //setting highscore
        if (highscore < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("easyHighScore", score);
            editor.commit();
        }
    }

    private void updatehurdleStatus() {
        for (int i = 0; i < listOfimg.length; i++) {
            ImageView tmp = listOfimg[i];
            RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) tmp.getLayoutParams();
            int marginTop = parms.topMargin;
            if (marginTop > speed) {
                marginTop = marginTop - speed;
            } else {
                marginTop = 1800;
                score += 10;
                if (score < 200)
                    speed += 1;

                if (score > 550 && score <= 600)
                    speed += 1;

                if (score > 950 && score <= 1000)
                    speed += 1;
            }
            parms.setMargins(0, marginTop, 0, 0);
            tmp.setLayoutParams(parms);

        }
    }

    private void updatePlayerPosition() {
        if (playerMoving) {
            if (playerPosition == 0) {
                player.setImageResource(R.drawable.platerright);
                playerPosition = 1;
                playerLayoutparam.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                playerLayoutparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                player.setLayoutParams(playerLayoutparam);
            } else {
                player.setImageResource(R.drawable.playerleft);
                playerPosition = 0;
                playerLayoutparam.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                playerLayoutparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                player.setLayoutParams(playerLayoutparam);
            }
            playerMoving = false;
        }

        if (playerMargin > 0) {
            playerMargin -= 200;
            if (playerPosition == 0) {
                playerLayoutparam.setMargins(playerMargin, 150, 0, 0);
            } else {
                playerLayoutparam.setMargins(0, 150, playerMargin, 0);
            }
        }
    }

    private void updateScore() {
        scoretxtview.setText("Score : " + score);
    }

    private class GameLoop extends AsyncTask<String, Integer, String> {

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array

            // Do something that takes a long time, for example:
            while (true) {
                if (!gameRunnng) {
                    break;
                }

                // Do things
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Call this to update your progress
                publishProgress(0);
            }

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (!iscollision()) {
                updatehurdleStatus();
                updatePlayerPosition();
                updateScore();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
}
