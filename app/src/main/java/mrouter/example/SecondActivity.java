package mrouter.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mrouter.annotations.MrouterUri;

/**
 * 作者： mooney
 * 日期： 2018/2/1
 * 邮箱： shili_yan@sina.com
 * 描述：
 */
@MrouterUri("second")
public class SecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
