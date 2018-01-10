package com.example.wangmutian.slider;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int currentItem = 0; // 当前图片的位置
    private ViewPager slider;
    private int[] mImageId=new int[]{R.drawable.slider01,R.drawable.slider02,R.drawable.slider03,R.drawable.slider04,R.drawable.slider05,R.drawable.slider01,R.drawable.slider02};
    private ArrayList<ImageView> mimageViewList;
    private LinearLayout ll_layout;
    private int mPointDis;
    private int dis;
//    private ImageView redpoint;
    int DELAY = 3600;//切换的延迟时间为2400毫秒
    boolean isAuto = true;//是否值自动轮播，默认为true
    boolean isFromUser = false;//用来标志用户是否滑动屏幕
    private static int  cursorpoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slider= (ViewPager) findViewById(R.id.slider);
        ll_layout= (LinearLayout) findViewById(R.id.ll_layout);
//        redpoint= (ImageView) findViewById(R.id.redpoint);
        initData();
        slider.setAdapter(new sliderAdapter());
        slider.setCurrentItem(0);
        initdian(cursorpoint);
        //执行定时任务
        handler.sendEmptyMessageDelayed(1, DELAY);
        slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position >= (mImageId.length-2)){
                    position = position - mImageId.length + 2;
                }


                /*
                dis= (int) (mPointDis*positionOffset) + position*mPointDis;
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) redpoint.getLayoutParams();
                params.leftMargin=dis;
                redpoint.setLayoutParams(params);

                ImageView image= (ImageView) ll_layout.getChildAt(position);
                image.setImageResource(R.drawable.shape_point_red);
                */
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                cursorpoint=position;
                if(position >= (mImageId.length-2)){
                    position = position - mImageId.length + 2;
                }
                initdian(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        //手指离开ViewPager的时候调用，发送延迟消息，自动轮播

                        isAuto = true;
                        if (isFromUser) {
                            isFromUser = false;
                            handler.sendEmptyMessageDelayed(1, DELAY);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //手指拖动ViewPager进行手动切换的时候，停止自动轮播

                        isAuto = false;
                        handler.removeMessages(1);
                        isFromUser = true;
                        break;
                    default:
                        break;
                }

                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;

                // 当视图在第一个时，将页面号设置为图片的最后一张。
                if (currentPosition == 0) {
                    slider.setCurrentItem(mimageViewList.size() - 2, false);

                } else if (currentPosition == mimageViewList.size() - 1) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    slider.setCurrentItem(1, false);
                }
            }
        });

        //视图树
        /*
        redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                redpoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //layout 方法执行结束的回调

                mPointDis=ll_layout.getChildAt(1).getLeft()-ll_layout.getChildAt(0).getLeft();
                System.out.println("小圆点："+mPointDis);
            }
        });*/
    }



    //定义一个handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (isAuto) {
                    //自动轮播
//                    currPos = (currPos + 1) % size;

                    slider.setCurrentItem(cursorpoint, true);
                    cursorpoint++;
                    handler.sendEmptyMessageDelayed(1, DELAY);

                }
            }
        }
    };


    private void initdian(int position){
        ll_layout.removeAllViews();
        for(int i=0;i<mImageId.length - 2;i++){
            ImageView dian = new ImageView(this);
            if(position == i){
                dian.setImageResource(R.drawable.shape_point_red);
            }else{
                dian.setImageResource(R.drawable.shape_point_gray);
            }

            //布局参数初始化，宽高包裹内容，父控件是谁 就是谁声明的布局参数
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // 设置左边距
            if(i > 0){
                param.leftMargin=10;
            }
            dian.setLayoutParams(param); //设置布局参数
            ll_layout.addView(dian);
        }
    }


    private void initData(){
        mimageViewList=new  ArrayList<ImageView>();

        for(int i=0;i<mImageId.length;i++){
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageId[i]);
            mimageViewList.add(view);
        }

    }


    class sliderAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mimageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image=mimageViewList.get(position);
            container.addView(image);

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mimageViewList.get(position));
        }
    }




}
