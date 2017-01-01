package com.jiushu.wifipwmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.jiushu.wifipwmanager.R;
import com.jiushu.wifipwmanager.util.RootUtil;

/**
 * Created by simon on 13/11/15.
 */
public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏状态栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		RootUtil.prepareWifiScannerSu(SplashActivity.this);

		// 默认请求http广告，若需要请求https广告，请设置AdSettings.setSupportHttps为true
		AdSettings.setSupportHttps(true);

		// adUnitContainer
		RelativeLayout adsParent = (RelativeLayout) this
				.findViewById(R.id.adsRl);

		/**
		 * 获取版本号
		 */
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			TextView verText = (TextView) findViewById(R.id.versioncode);
			verText.setText("Ver " + version);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// init();
		// the observer of AD
		SplashAdListener listener = new SplashAdListener() {
			@Override
			public void onAdDismissed() {
				Log.i("RSplashActivity", "onAdDismissed");
				jumpWhenCanClick(); // 跳转至您的应用主界面
			}

			@Override
			public void onAdFailed(String arg0) {
				Log.i("RSplashActivity", "onAdFailed");
				jump();
			}

			@Override
			public void onAdPresent() {
				Log.i("RSplashActivity", "onAdPresent");
			}

			@Override
			public void onAdClick() {
				Log.i("RSplashActivity", "onAdClick");
				// 设置开屏可接受点击时，该回调可用
			}
		};
		String adPlaceId = "2058622"; // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
		new SplashAd(this, adsParent, listener, adPlaceId, true);
	}

	/**
	 * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加canJumpImmediately判断。
	 * 另外，点击开屏还需要在onResume中调用jumpWhenCanClick接口。
	 */
	public boolean canJumpImmediately = false;

	private void jumpWhenCanClick() {
		Log.d("test", "this.hasWindowFocus():" + this.hasWindowFocus());
		if (canJumpImmediately) {
			this.startActivity(new Intent(SplashActivity.this,
					MainActivity.class));
			this.finish();
		} else {
			canJumpImmediately = true;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		canJumpImmediately = false;
	}

	/**
	 * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
	 */
	private void jump() {
		this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
		this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (canJumpImmediately) {
			jumpWhenCanClick();
		}
		canJumpImmediately = true;
	}
	/*
	 * private void init() {
	 * 
	 * new Handler().postDelayed(new Runnable() { // @Override public void run()
	 * { Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	 * startActivity(intent); SplashActivity.this.finish(); } }, 2000); }
	 */
}
