package zhangyf.vir56k.autoviewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：张云飞
 * 日期：2015-12-04 15:24:12
 * name: 循环播放的 viewPager
 * 适用: 某些需要 循环播放 广告，主题内容，活动，新闻内容时。
 * 支持： 拖动手势拖动过程中 不滚动
 * Created by zhangyunfei on 15/12/4.
 */
public class SimpleCycleViewPager extends ViewPager {
    private static final String TAG = SimpleCycleViewPager.class.getSimpleName();
    private long interval = 1500;

    private int currentPosition;

    private MyHandler myHandler;
    private MyPagerAdapter pagerAdapter;
    private DateLogger mDateLogger = new DateLogger();
    private List<Drawable> mDatasource;

    public SimpleCycleViewPager(Context context) {
        super(context);
        init();
    }

    public SimpleCycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOffscreenPageLimit(3);
        addOnPageChangeListener(mOnPageChangeListener);

        myHandler = new MyHandler(this);
    }

    public long getInterval() {
        return interval;
    }

    /**
     * 自动滚动的间隔
     *
     * @param interval
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setDatasource(List<Drawable> data) {
        mDatasource = data;
        if (mDatasource == null || mDatasource.size() == 1) {
            setEnabled(false);
            setClickable(false);
            setFocusable(false);
            setFocusableInTouchMode(false);
        }
        pagerAdapter = new MyPagerAdapter(data);
        setAdapter(pagerAdapter);

        currentPosition = 1;
        setCurrentItem(1);
    }

    public void showNextView() {
        if (mDatasource == null || mDatasource.size() == 1)
            return;
        setCurrentItem(currentPosition + 1, true);
    }

    /**
     * 子页面变化状态监听
     */
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int positon, float baifen, int offset) {
        }

        @Override
        public void onPageSelected(int postion) {
            Log.i(TAG, "## onPageSelected :: onPageSelected:" + postion);
            if (pagerAdapter.getCount() == 0)
                return;
            currentPosition = postion;
            if (postion == 0) {
                int last = currentPosition;
                currentPosition = pagerAdapter.getCount() - 2;
                Log.i(TAG, String.format("#### onPageSelected :: change currentPosition :: from %s to %s ", last, currentPosition));
            } else if (postion == pagerAdapter.getCount() - 1) {
                int last = currentPosition;
                currentPosition = 1;
                Log.i(TAG, String.format("#### onPageSelected :: change currentPosition :: from %s to %s ", last, currentPosition));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i(TAG, "## onPageScrollStateChanged :: state=" + state);
            if (pagerAdapter.getCount() == 0)
                return;
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                pauseScroll();
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                Log.i(TAG, "## onPageScrollStateChanged :: viewPager.getCurrentItem: " + getCurrentItem());
                Log.i(TAG, "## onPageScrollStateChanged :: curent position: " + currentPosition);
                setCurrentItem(currentPosition, false);
                mDateLogger.logDate();
                resumeScroll();
            }
        }
    };

    /**
     * 恢复自动滚动
     */
    public void resumeScroll() {
        if (mDatasource == null || mDatasource.size() == 1)
            return;
        myHandler.sendMessageDelayed(myHandler.obtainMessage(MyHandler.MESSAGE_CHECK), interval);
    }

    /**
     * 暂停自动滚动
     */
    public void pauseScroll() {
        myHandler.removeMessages(MyHandler.MESSAGE_CHECK);
    }


    private class MyPagerAdapter extends PagerAdapter {

        private final ArrayList<ImageView> viewList;

        public MyPagerAdapter(List<Drawable> data) {
            if (data == null) {
                viewList = new ArrayList<>();// 将要分页显示的View装入数组中
                return;
            }
            viewList = new ArrayList<>();// 将要分页显示的View装入数组中

            ImageView imageView;

            imageView = new ImageView(getContext());
            imageView.setImageDrawable(data.get(data.size() - 1));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewList.add(imageView);

            for (int i = 0; i < data.size(); i++) {
                imageView = new ImageView(getContext());
                imageView.setImageDrawable(data.get(i));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewList.add(imageView);
            }

            imageView = new ImageView(getContext());
            imageView.setImageDrawable(data.get(0));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewList.add(imageView);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title" + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(viewList.get(position), lp);
            return viewList.get(position);
        }

    }

    private static class MyHandler extends Handler {
        public static final int MESSAGE_CHECK = 9001;
        private static final String TAG = MyHandler.class.getSimpleName();
        private WeakReference<SimpleCycleViewPager> innerObject;

        public MyHandler(SimpleCycleViewPager context) {
            this.innerObject = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (MESSAGE_CHECK == msg.what) {
                SimpleCycleViewPager simpleCycleViewPager = innerObject.get();
                if (simpleCycleViewPager == null)
                    return;
                if (simpleCycleViewPager.getContext() instanceof Activity) {
                    Activity activity = (Activity) simpleCycleViewPager.getContext();
                    if (activity.isFinishing())
                        return;
                }
                simpleCycleViewPager.showNextView();

                removeMessages(MESSAGE_CHECK);
                sendMessageDelayed(obtainMessage(MESSAGE_CHECK), simpleCycleViewPager.interval);
                return;
            }
            super.handleMessage(msg);
        }

    }

    /**
     * 记录时间间隔使用的 计时器
     */
    private class DateLogger {
        private long lastDate;

        public void logDate() {
            long now = new Date().getTime();
            if (lastDate == 0) {
                lastDate = now;
                return;
            }
            long jiange = now - lastDate;
            Log.d(TAG, "时间间隔:" + jiange);
            lastDate = now;
        }
    }
}
