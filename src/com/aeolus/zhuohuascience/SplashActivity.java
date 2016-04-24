package com.aeolus.zhuohuascience;

import com.aeolus.ui.MainFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View startView = View.inflate(this, R.layout.activity_splash,
				null);
		setContentView(startView);
		AlphaAnimation maAlphaAnimation = new AlphaAnimation(0.3f, 1.0f);
		maAlphaAnimation.setDuration(3000);
		startView.setAnimation(maAlphaAnimation);
		maAlphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				DirectTo();
			}
		});
	}

	protected void DirectTo() {

		/*Intent mainIntent = new Intent(SplashActivity.this, MainFragment.class);
		SplashActivity.this.startActivity(mainIntent);
		SplashActivity.this.finish(); */
		
		Intent mainIntent = new Intent(SplashActivity.this, FactoryActivity.class);
		SplashActivity.this.startActivity(mainIntent);
		SplashActivity.this.finish();
	}
}
