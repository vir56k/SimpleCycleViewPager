# SimpleCycleViewPager
循环播放的 viewPager，支持 拖动手势拖动过程中 不滚动

#介绍
名称：SimpleCycleViewPager
作者：张云飞
日期：2015-12-04 15:24:12
描述: 循环播放的 viewPager
适用: 某些需要 循环播放 广告，主题内容，活动，新闻内容时。
支持： 拖动手势拖动过程中 不滚动

#使用代码：
在layout里写：
  <zhangyf.vir56k.autoviewpager.SimpleCycleViewPager
    android:id="@+id/viewpager"
    android:layout_width="wrap_content"
    android:layout_height="100dp"
    android:layout_margin="0dp"
    android:background="#FF0000"
    android:padding="0dp"></zhangyf.vir56k.autoviewpager.SimpleCycleViewPager>

在代码里应用：
      //设置数据源
      viewPager.setDatasource(datasource);
      //设置间隔
      viewPager.setInterval(1500);
      //启动自动滚动
      viewPager.resumeScroll();
