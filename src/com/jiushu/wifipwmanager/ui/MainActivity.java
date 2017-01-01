package com.jiushu.wifipwmanager.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiushu.wifipwmanager.R;
import com.jiushu.wifipwmanager.data.WiFiNetWork;
import com.jiushu.wifipwmanager.util.Constants;
import com.jiushu.wifipwmanager.util.CustomDialog;
import com.jiushu.wifipwmanager.util.DBUtil;
import com.jiushu.wifipwmanager.util.FileUtil;
import com.jiushu.wifipwmanager.util.PopupMenuUtil;
import com.jiushu.wifipwmanager.util.RootUtil;
import com.jiushu.wifipwmanager.util.StringUtil;

public class MainActivity extends Activity implements
		PopupMenu.OnItemSelectedListener {

	private Context context;
	private List<WiFiNetWork> dataSource;
	private List<WiFiNetWork> dataSourceCopy;

	private ListViewAdapter listViewAdapter;
	private ListView listView;
	private int selectedItemPosition;
	private WiFiNetWork selectedWiFiNetWork;

	private TextView ssidCounterTV;
	private Button searchBtn;
	private EditText searchEdit;
	private RelativeLayout searchbarRLayout;
	private TextView searchResultCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;

		setContentView(R.layout.activity_main);

		initData();
		initUI();
	}

	class GetRootPermissionThread extends Thread {
		public void run() {
			RootUtil.prepareWifiScannerSu(MainActivity.this);
		}
	}

	private void initData() {

		dataSource = new ArrayList<>();

		String copyFrom = "/data/misc/wifi/";
		String copyTo = "/data/data/" + getPackageName() + "/files/";

		FileUtil.cpFile(MainActivity.this, copyFrom, copyTo);
		dataSource = FileUtil.readFiles(MainActivity.this);
		dataSourceCopy = dataSource;
	}

	private void initUI() {
		initTitleView();
		initListView();
	}

	private void initTitleView() {
		initSearchBar();
	}

	private void initSearchBar() {
		ssidCounterTV = (TextView) findViewById(R.id.content_view_top_ssidcounter);
		ssidCounterTV.setVisibility(View.VISIBLE);

		searchbarRLayout = (RelativeLayout) findViewById(R.id.content_view_top_searchbar);
		searchbarRLayout.setVisibility(View.GONE);

		searchEdit = (EditText) findViewById(R.id.content_view_top_searchbar_textedit);
		searchEdit.addTextChangedListener(textWatcher);

		searchResultCounter = (TextView) findViewById(R.id.content_view_top_searchbar_filterresult_counter);
		searchResultCounter.setText("结果：" + dataSource.size());

		searchBtn = (Button) findViewById(R.id.title_searchbtn);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ssidCounterTV.getVisibility() == View.VISIBLE) {
					ssidCounterTV.setVisibility(View.GONE);
					searchbarRLayout.setVisibility(View.VISIBLE);
				} else {
					ssidCounterTV.setVisibility(View.VISIBLE);
					searchbarRLayout.setVisibility(View.GONE);

					searchEdit.setText("");
				}
			}
		});
	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.main_listView);
		// listView.setTextFilterEnabled(true);
		// GridView gridView = (GridView) findViewById(R.id.main_gridView);

		ssidCounterTV = (TextView) findViewById(R.id.content_view_top_ssidcounter);

		if (dataSource == null)
			dataSource = new ArrayList<>();
		ssidCounterTV.setText("WiFi数量：" + dataSource.size());

		listViewAdapter = new ListViewAdapter(dataSource, MainActivity.this);
		listView.setAdapter(listViewAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItemPosition = position;
				selectedWiFiNetWork = dataSource.get(position);
				showPopupMenu(view);
			}
		});
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			Log.d("TAG", "afterTextChanged--------------->");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			Log.d("TAG", "beforeTextChanged--------------->");
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Log.d("TAG", "onTextChanged--------------->");
			String str = searchEdit.getText().toString();
			try {
				// if ((heighText.getText().toString())!=null)
				search(str);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	public void search(String searchVal) {
		dataSource = dataSourceCopy;

		List<WiFiNetWork> mSearchResult = new ArrayList<>();

		String filterString = searchVal.toString().trim().toLowerCase();

		// 如果搜索框内容为空，就恢复原始数据
		if (StringUtil.isEmpty(filterString)) {
			mSearchResult = dataSourceCopy;
		} else {
			// 过滤出新数据
			for (WiFiNetWork wiFiNetWork : dataSource) {
				if (-1 != wiFiNetWork.getSsid().toLowerCase()
						.indexOf(filterString)
						|| (!StringUtil.isEmpty(wiFiNetWork.getComment()) && -1 != wiFiNetWork
								.getComment().toLowerCase()
								.indexOf(filterString))) {
					mSearchResult.add(wiFiNetWork);
				}
			}
		}

		dataSource = mSearchResult;
		searchResultCounter.setText("结果：" + dataSource.size());
		initListView();
	}

	@SuppressWarnings("deprecation")
	private void showPopupMenu(View view) {
		PopupMenu menu = new PopupMenu(this);

		menu.setHeaderTitle("热点："
				+ dataSource.get(selectedItemPosition).getSsid());
		// Set Listener
		menu.setOnItemSelectedListener(this);
		// Add Menu (Android menu like style)
		menu.add(Constants.PopupMenuItem.SET_COMMENT,
				R.string.popmenu_setcomment).setIcon(
				getResources().getDrawable(R.drawable.popupmenu_set_comment));

		menu.add(Constants.PopupMenuItem.COPY_SSID_PW,
				R.string.popmenu_wifiname_and_pw).setIcon(
				getResources().getDrawable(R.drawable.popupmenu_copy_ssid_pw));

		menu.add(Constants.PopupMenuItem.COPY_PW, R.string.popmenu_wifipw)
				.setIcon(
						getResources()
								.getDrawable(R.drawable.popupmenu_copy_pw));

		menu.add(Constants.PopupMenuItem.SHARE, R.string.popmenu_share)
				.setIcon(getResources().getDrawable(R.drawable.popupmenu_share));

		menu.add(Constants.PopupMenuItem.DELETE, R.string.popmenu_del).setIcon(
				getResources().getDrawable(R.drawable.popupmenu_del));

		menu.show(view);
	}

	@Override
	public void onItemSelected(PopupMenuItem item) {
		final WiFiNetWork selectedWiFiNetWork = dataSource
				.get(selectedItemPosition);

		switch (item.getItemId()) {
		case Constants.PopupMenuItem.SET_COMMENT:
			// listViewAdapter.updataView(listView, selectedItemPosition,
			// "123");
			setComment();
			break;

		case Constants.PopupMenuItem.COPY_PW:
			PopupMenuUtil.copyPw(context, selectedWiFiNetWork);
			break;

		case Constants.PopupMenuItem.COPY_SSID_PW:
			PopupMenuUtil.copySsidAndPw(context, selectedWiFiNetWork);
			break;

		case Constants.PopupMenuItem.SHARE:
			PopupMenuUtil.share(context, selectedWiFiNetWork);
			break;

		case Constants.PopupMenuItem.DELETE:
			selectedWiFiNetWork.setIsDel(true);
			@SuppressWarnings("unused")
			Map<String, Integer> result = PopupMenuUtil.delete(context,
					selectedWiFiNetWork);
			dataSource.remove(selectedWiFiNetWork);
			ssidCounterTV.setText("WiFi数量：" + dataSource.size());
			listViewAdapter.notifyDataSetChanged();
			break;

		}
	}

	public void setComment() {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View setcommentView = inflater.inflate(R.layout.popup_menu_setcomment,
				null);
		@SuppressWarnings("unused")
		EditText commentET = (EditText) setcommentView
				.findViewById(R.id.setcomment_editor);

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage("    添加备注信息只是为了方便区分软件内的热点，并不影响系统中WiFi的任何信息。");
		builder.setTitle("热点：" + selectedWiFiNetWork.getSsid());
		builder.setPositiveButton("确定",
				new CustomDialog.OnPositiveButtonClickListener() {
					@Override
					public void getText(String valStr) {
						selectedWiFiNetWork.setComment(valStr);
						DBUtil.upsert(
								context,
								Arrays.asList(new WiFiNetWork[] { selectedWiFiNetWork }));

						listViewAdapter.notifyDataSetChanged();
					}
				});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (searchbarRLayout.getVisibility() == View.VISIBLE) {
				ssidCounterTV.setVisibility(View.VISIBLE);
				searchbarRLayout.setVisibility(View.GONE);

				searchEdit.setText("");

				return false;
			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("警告");
				builder.setMessage("确认退出？");
				builder.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								MainActivity.this.finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}