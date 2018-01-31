package router.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import router.Router;
import router.annotations.RouterConfig;
import router.annotations.RouterUri;

@RouterUri("main")
@RouterConfig(module = "app", scheme = "test://app/")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_main)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Router.openUriForResult(MainActivity.this,"app/second",3,null);
                    }
                });
    }
}
