package mrouter.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mrouter.Router;
import mrouter.annotations.RouterConfig;
import mrouter.annotations.RouterUri;

@RouterUri("main")
@RouterConfig(module = "app")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Router.getInstance().init(this);
        findViewById(R.id.tv_main)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Router.getInstance().open("router://appName/app/second");
                    }
                });
    }
}
