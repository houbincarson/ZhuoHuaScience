package com.aeolus.ui;

import com.aeolus.zhuohuascience.R;
import com.aeolus.base.Base;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainFragment extends FragmentActivity {
	TabHost tabHost;
	LayoutInflater inflater;

	int tab_btn[] = new int[] 
			{
			R.drawable.tab_icon_home_layout, 
			R.drawable.tab_icon_message_layout,
			R.drawable.tab_icon_more_layout, 
			R.drawable.tab_icon_selfinfo_layout 
			};
	String tab_String[] = new String[] { "首页", "审批", "财务", "我们" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化组件
		InitiView();

		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				FragmentPage_home fragmentPage_home = (FragmentPage_home) fm
						.findFragmentByTag("Home");
				FragmentPage2 fragmentPage2 = (FragmentPage2) fm
						.findFragmentByTag("Page2");
				FragmentPage3 fragmentPage3 = (FragmentPage3) fm
						.findFragmentByTag("Page3");
				FragmentPage4 fragmentPage4 = (FragmentPage4) fm
						.findFragmentByTag("Page4");
				android.support.v4.app.FragmentTransaction ft = fm
						.beginTransaction();

				if (fragmentPage_home != null)
					ft.detach(fragmentPage_home);
				if (fragmentPage2 != null)
					ft.detach(fragmentPage2);
				if (fragmentPage3 != null)
					ft.detach(fragmentPage3);
				if (fragmentPage4 != null)
					ft.detach(fragmentPage4);
				// 当前选项卡
				if (tabId.equalsIgnoreCase("Home")) {
					if (fragmentPage_home == null) {
						ft.add(R.id.realtabcontent, new FragmentPage_home(),
								"Home");
					} else {
						ft.attach(fragmentPage_home);
					}
				} else if (tabId.equalsIgnoreCase("Page2")) {
					if (fragmentPage2 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage2(),
								"Page2");
					} else {
						ft.attach(fragmentPage2);
					}
				} else if (tabId.equalsIgnoreCase("Page3")) {
					if (fragmentPage3 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage3(),
								"Page3");
					} else {
						ft.attach(fragmentPage3);
					}
				} else if (tabId.equalsIgnoreCase("Page4")) {
					if (fragmentPage4 == null) {
						ft.add(R.id.realtabcontent, new FragmentPage4(),
								"Page4");
					} else {
						ft.attach(fragmentPage4);
					}
				}
				ft.commit();
			}
		};
		tabHost.setOnTabChangedListener(tabChangeListener);

		TabHost.TabSpec tSpec_fragmentPage_home = tabHost.newTabSpec("Home");
		// 为Tab按钮设置图标、文字和内容
		tSpec_fragmentPage_home.setIndicator(getTabItemView(0));
		tSpec_fragmentPage_home
				.setContent(new DummyTabContent(getBaseContext()));

		tabHost.addTab(tSpec_fragmentPage_home);

		TabHost.TabSpec tSpec_fragmentPage2 = tabHost.newTabSpec("Page2");
		tSpec_fragmentPage2.setIndicator(getTabItemView(1));
		tSpec_fragmentPage2.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage2);

		TabHost.TabSpec tSpec_fragmentPage3 = tabHost.newTabSpec("Page3");
		tSpec_fragmentPage3.setIndicator(getTabItemView(2));
		tSpec_fragmentPage3.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage3);

		TabHost.TabSpec tSpec_fragmentPage4 = tabHost.newTabSpec("Page4");
		tSpec_fragmentPage4.setIndicator(getTabItemView(3));
		tSpec_fragmentPage4.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpec_fragmentPage4);

	}

	private void InitiView() {
		Log.v(Base.TAG, "OnCreate");
		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);
		tabHost.setup();
	}

	@SuppressLint("InflateParams")
	private View getTabItemView(int i) {
		
		inflater = LayoutInflater.from(MainFragment.this);
		View view = inflater.inflate(R.layout.tab_items_view, null);
		Log.v(Base.TAG,view.toString());
		ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageview);
		imageView.setImageResource(tab_btn[i]);				
		TextView textView = (TextView) view.findViewById(R.id.tab_textview);		
		textView.setText(tab_String[i]);
		return view;
	}
}
