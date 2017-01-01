package com.jiushu.wifipwmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

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

		/**
		 * 获取版本号
		 */
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			TextView verText = (TextView) findViewById(R.id.versioncode);
			verText.setText("Ver "+version);
		} catch (Exception e) {
			e.printStackTrace();
		}

		init();
	}

	private void init() {

		new Handler().postDelayed(new Runnable() {
			// @Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 2000);
	}
}
