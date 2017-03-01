package com.maxi.updatatest;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.maxi.updatatest.fragment.OneFragment;
import com.maxi.updatatest.fragment.ThreeFragment;
import com.maxi.updatatest.fragment.TwoFragment;


/**
 * ViewPager+Fragment
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	Button buttonOne;
	Button buttonTwo;
	Button buttonThree;

	ViewPager mViewPager;

	List<Fragment> fragmentList;

	OneFragment oneFragment;
	TwoFragment twoFragment;
	ThreeFragment threeFragment;

	ImageView imageviewOvertab;

	int screenWidth;
	int currenttab = -1;

	private AnimatorSet anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonOne = (Button) findViewById(R.id.btn_one);
		buttonTwo = (Button) findViewById(R.id.btn_two);
		buttonThree = (Button) findViewById(R.id.btn_three);

		buttonOne.setOnClickListener(this);
		buttonTwo.setOnClickListener(this);
		buttonThree.setOnClickListener(this);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		fragmentList = new ArrayList<>();

		oneFragment = new OneFragment();
		twoFragment = new TwoFragment();
		threeFragment = new ThreeFragment();

		fragmentList.add(oneFragment);
		fragmentList.add(twoFragment);
		fragmentList.add(threeFragment);

		screenWidth = getResources().getDisplayMetrics().widthPixels;
		buttonTwo.measure(0, 0);

		imageviewOvertab = (ImageView) findViewById(R.id.imgv_overtab);

		mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
	}




	/**
	 * 定义自己的ViewPager适配器。 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
	 */
	class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

		public MyFrageStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		/**
		 * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
		 */
		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);// 这句话要放在最前面，否则会报错

			// 获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
			int currentItem = mViewPager.getCurrentItem();

			if (currentItem == currenttab) {
				return;
			}
			imageMove(mViewPager.getCurrentItem());
			currenttab = mViewPager.getCurrentItem();
		}

	}

	/**
	 * 移动覆盖层图片 初始化动画
     * 目标Tab，也就是要移动到的导航选项按钮的位置 第一个导航按钮对应0，第二个对应1，以此类推
	 */
	private void imageMove(int moveToTab) {
		switch (moveToTab) {
			case 0:
				initAnimator(buttonOne);
				break;
			case 1:
				initAnimator(buttonTwo);
				break;
			case 2:
				initAnimator(buttonThree);
				break;
			default:
				break;
		}

		anim.start();
		int startPosition = 0;
		int movetoPosition = 0;

		startPosition = (currenttab * (screenWidth / 3)) + (((screenWidth / 3) - dip2px(this,80)) / 2) - dip2px(this,10);
		movetoPosition = moveToTab * (screenWidth / 3) + (((screenWidth / 3) - dip2px(this,80)) / 2) - dip2px(this,10);

		// 平移动画
		TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);


		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(200);

		imageviewOvertab.startAnimation(translateAnimation);
	}


	public static int dip2px(Context context,float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_one:
				changeView(0);
				break;
			case R.id.btn_two:
				changeView(1);
				break;
			case R.id.btn_three:
				changeView(2);
				break;
			default:
				break;
		}
	}

	// 手动设置ViewPager要显示的视图
	private void changeView(int desTab) {
		mViewPager.setCurrentItem(desTab, true);
	}



	private void initAnimator(View view) {
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.3f);
		ObjectAnimator anim3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.3f);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1f);
		ObjectAnimator anim4 = ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1f);

		ObjectAnimator anim11 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f);
		ObjectAnimator anim22 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
		ObjectAnimator anim33 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f);
		ObjectAnimator anim44 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);

		anim = new AnimatorSet();

		anim.play(anim1).with(anim3);
		anim.play(anim2).with(anim4);
		anim.play(anim2).after(anim1);
		anim.play(anim11).with(anim33);
		anim.play(anim22).with(anim44);
		anim.play(anim22).after(anim11);
		anim.play(anim11).after(anim2);

		anim.setDuration(100);
	}
}
