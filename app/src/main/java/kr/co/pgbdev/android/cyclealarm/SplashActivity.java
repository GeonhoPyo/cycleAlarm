package kr.co.pgbdev.android.cyclealarm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try{
            new Handler().postDelayed(new SplashHandler(),1500); // splash 1500 ms
        }catch (Exception e){ // Lopper exception
            e.printStackTrace();

            startActivity(new Intent(getApplication(),MainActivity.class));
            SplashActivity.this.finish();
        }
    }

    private class SplashHandler implements Runnable {
        @Override
        public void run() {
            try{
                startActivity(new Intent(getApplication(),MainActivity.class));
                SplashActivity.this.finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Back key 무시
    }
}
