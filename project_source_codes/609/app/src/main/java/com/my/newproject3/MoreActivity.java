package com.my.newproject3;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.view.View;

public class MoreActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private TextView cash_view;
	private TextView rate_view;
	private LinearLayout linear4;
	private LinearLayout linear3;
	private Button pause_btn;
	private Button shop_btn;
	private Button background_btn;
	private Button music_btn;
	private Button back_btn;
	private Button help_btn;
	
	private TimerTask Clock;
	private Intent Three = new Intent();
	private SharedPreferences Record;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.more);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		cash_view = (TextView) findViewById(R.id.cash_view);
		rate_view = (TextView) findViewById(R.id.rate_view);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		pause_btn = (Button) findViewById(R.id.pause_btn);
		shop_btn = (Button) findViewById(R.id.shop_btn);
		background_btn = (Button) findViewById(R.id.background_btn);
		music_btn = (Button) findViewById(R.id.music_btn);
		back_btn = (Button) findViewById(R.id.back_btn);
		help_btn = (Button) findViewById(R.id.help_btn);
		Record = getSharedPreferences("Stat", Activity.MODE_PRIVATE);
		
		pause_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Paused", "").equals("0")) {
					Record.edit().putString("Paused", "1").commit();
				}
				else {
					Record.edit().putString("Paused", "0").commit();
				}
			}
		});
		
		shop_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Three.setClass(getApplicationContext(), ShopActivity.class);
				startActivity(Three);
				finish();
			}
		});
		
		background_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Three.setClass(getApplicationContext(), BackgroundActivity.class);
				startActivity(Three);
				finish();
			}
		});
		
		music_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Music", "").equals("0")) {
					Record.edit().putString("Music", "1").commit();
				}
				else {
					Record.edit().putString("Music", "0").commit();
				}
			}
		});
		
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		help_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Three.setClass(getApplicationContext(), HelpActivity.class);
				startActivity(Three);
				finish();
			}
		});
	}
	private void initializeLogic() {
		setTitle("More...");
		Clock = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (Double.parseDouble(Record.getString("Money", "")) > 10000000000000000d) {
							cash_view.setText("$".concat(Record.getString("Money", "")));
						}
						else {
							cash_view.setText("$".concat(String.valueOf((long)(Math.floor(Double.parseDouble(Record.getString("Money", "")))))));
						}
						rate_view.setText("$".concat(String.valueOf((long)(Math.floor(Double.parseDouble(Record.getString("Income", ""))))).concat("/s")));
						if (Record.getString("Pausable", "").equals("0")) {
							pause_btn.setEnabled(false);
							music_btn.setEnabled(false);
						}
						else {
							pause_btn.setEnabled(true);
							music_btn.setEnabled(true);
						}
						if (Record.getString("Paused", "").equals("0")) {
							pause_btn.setText("Pause");
						}
						else {
							pause_btn.setText("Resume");
						}
						if (Record.getString("Music", "").equals("0")) {
							music_btn.setText("Resume music");
						}
						else {
							music_btn.setText("Pause music");
						}
						if (Record.getString("Background", "").equals("0")) {
							background_btn.setEnabled(false);
						}
						else {
							background_btn.setEnabled(true);
						}
						if (Record.getString("Help", "").equals("0")) {
							help_btn.setEnabled(false);
						}
						else {
							help_btn.setEnabled(true);
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(Clock, (int)(0), (int)(100));
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
