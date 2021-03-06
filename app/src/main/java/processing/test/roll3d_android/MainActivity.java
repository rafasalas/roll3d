package processing.test.roll3d_android;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
    private Button vamos;
    private RadioGroup mFunction;
    private RadioButton typeFunction;
    public boolean isPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        isPlayer=true;

        addlistenerOnButton();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }
        //


    }

    public void addlistenerOnButton(){


        vamos = (Button) findViewById(R.id.goBoton);

        vamos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final global dataglobal = (global) getApplicationContext();
                dataglobal.setPlayer(isPlayer);

                    WallpaperManager wallcachas = WallpaperManager.getInstance(getApplicationContext());
                    try {


                        Intent intent = new Intent(wallcachas.ACTION_CHANGE_LIVE_WALLPAPER);

                        intent.putExtra(wallcachas.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                new ComponentName(MainActivity.this, MainService.class));
                        //stopService(intent);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {

                        Intent intent = new Intent();
                        intent.setAction(wallcachas.ACTION_LIVE_WALLPAPER_CHOOSER);
                        startActivity(intent);
                        finish();
                    }



            }

        });
    }
}
