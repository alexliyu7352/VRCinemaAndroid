package com.bestv.vrcinema;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bestv.vrcinema.model.RecommendInfoSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunyang on 17/1/4.
 */

public class ContentFragment extends android.support.v4.app.Fragment {
    private static final int commonFragmentCount = 10;
    private ViewPager viewPager;
    private ArrayList<ImageView> dotImageViews = new ArrayList<>();  // 翻页索引


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.content_fragment, container, false);

        dynamicAddImageview();

        // 填充ViewPager
        fillViewPager(view);


        return view;
    }

    /**
     * 填充ViewPager
     */
    private void fillViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // 1. viewPager添加parallax效果，使用PageTransformer就足够了
        viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));


        viewPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            private PageFragment currentPrimaryPage = null;

            @Override
            public Fragment getItem(int position) {
                int cur = viewPager.getCurrentItem();
                boolean showInfo = (cur == position);
                PageFragment fragment = PageFragment.newInstance(RecommendInfoSingleton.getInstance().getMovieInfo(position), showInfo);
                return fragment;
            }

            @Override
            public int getCount() {
                return RecommendInfoSingleton.getInstance().getItemsCount();
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                PageFragment pageFragment = (PageFragment) object;
                if (pageFragment != currentPrimaryPage) {
                    if (currentPrimaryPage != null) {
                        currentPrimaryPage.disappearMovieInfo();
                    }
                    if (pageFragment != null) {
                        pageFragment.appearMovieInfo();
                    }
                    currentPrimaryPage = pageFragment;
                }
            }
        });

        // 3. viewPager滑动时，调整指示器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicator();
                updatePage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicator();
        updatePage();
    }

    /**
     * 更新指示器
     */
    private void updateIndicator() {
        int totalNum = viewPager.getAdapter().getCount();
        if(totalNum > 0){
            int currentItem = viewPager.getCurrentItem();
            dotImageViews.get(currentItem).setImageDrawable(this.getResources().getDrawable((R.drawable.dot_current)));
            if(currentItem-1 >= 0){
                dotImageViews.get(currentItem-1).setImageDrawable(this.getResources().getDrawable((R.drawable.dot_normal)));
            }
            if(currentItem+1<totalNum){
                dotImageViews.get(currentItem+1).setImageDrawable(this.getResources().getDrawable((R.drawable.dot_normal)));
            }
        }
    }

    private void updatePage(){

    }

    private void dynamicAddImageview(){
        int count = RecommendInfoSingleton.getInstance().getItemsCount();
        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.dot_area);
        for(int i = 0; i < count; i++){
            ImageView imageView = new ImageView(getActivity());
            linearLayout.addView(imageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)imageView.getLayoutParams();
            params.width = dp2px(getActivity(), 6);
            params.height = dp2px(getActivity(), 6);;
            params.leftMargin = dp2px(getActivity(), 7);;
            params.rightMargin = dp2px(getActivity(), 7);;
            params.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(params);
            if(i==0) {
                imageView.setImageDrawable(this.getResources().getDrawable((R.drawable.dot_current)));
            }else{
                imageView.setImageDrawable(this.getResources().getDrawable((R.drawable.dot_normal)));
            }

            dotImageViews.add(imageView);
        }
    }

    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }
}
