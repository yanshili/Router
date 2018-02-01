package mrouter.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mrouter.Mrouter;
import mrouter.annotations.MrouterConfig;
import mrouter.annotations.MrouterUri;

@MrouterUri("main")
@MrouterConfig(module = "app")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mrouter.getInstance().init(this);
        findViewById(R.id.tv_main)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mrouter.getInstance().open("router://appName/app/second");
                    }
                });
    }
}
