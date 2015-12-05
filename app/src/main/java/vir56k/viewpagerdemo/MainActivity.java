package vir56k.viewpagerdemo;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import zhangyf.vir56k.autoviewpager.SimpleCycleViewPager;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Drawable> datasource;

    private SimpleCycleViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (SimpleCycleViewPager) findViewById(R.id.viewpager);

        datasource = new ArrayList<>();
        datasource.add(getResources().getDrawable(R.drawable.image1));
        datasource.add(getResources().getDrawable(R.drawable.image2));
        datasource.add(getResources().getDrawable(R.drawable.image3));
        datasource.add(getResources().getDrawable(R.drawable.image4));

        //设置数据源
        viewPager.setDatasource(datasource);
        //设置间隔
        viewPager.setInterval(1500);
        //启动自动滚动
        viewPager.resumeScroll();

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.pauseScroll();
                viewPager.showNextView();
                viewPager.resumeScroll();
            }
        });
    }


}
