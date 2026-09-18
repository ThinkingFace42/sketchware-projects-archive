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
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import java.util.Timer;
import java.util.TimerTask;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

public class HelpActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private double Counter = 0;
	private String Share = "";
	private double Init_done = 0;
	
	private ArrayList<String> Userlist = new ArrayList<>();
	private ArrayList<String> Songlist = new ArrayList<>();
	
	private LinearLayout linear1;
	private CheckBox help_checkbox;
	private Button money_btn;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private Spinner song_spinner;
	private Button back_btn;
	private TextView cash_view;
	private TextView rate_view;
	private Button call_btn;
	private Button share_btn;
	private Button version_btn;
	private EditText user_name;
	private Button login_btn;
	
	private Intent Three = new Intent();
	private TimerTask Clock;
	private SharedPreferences Record;
	private AlertDialog.Builder Help;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.help);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		help_checkbox = (CheckBox) findViewById(R.id.help_checkbox);
		money_btn = (Button) findViewById(R.id.money_btn);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		song_spinner = (Spinner) findViewById(R.id.song_spinner);
		back_btn = (Button) findViewById(R.id.back_btn);
		cash_view = (TextView) findViewById(R.id.cash_view);
		rate_view = (TextView) findViewById(R.id.rate_view);
		call_btn = (Button) findViewById(R.id.call_btn);
		share_btn = (Button) findViewById(R.id.share_btn);
		version_btn = (Button) findViewById(R.id.version_btn);
		user_name = (EditText) findViewById(R.id.user_name);
		login_btn = (Button) findViewById(R.id.login_btn);
		Record = getSharedPreferences("Stat", Activity.MODE_PRIVATE);
		Help = new AlertDialog.Builder(this);
		
		help_checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Help.setTitle("Help");
				Help.setMessage("In this help page, you can get help about the game. You can buy various buttons in main page which will pay off through time. In the more  page, you can pause the game and access other pages. In the shop, you can buy something. This new version has auto-save!");
				Help.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				Help.setNegativeButton("Really?", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						Help.create().show();
					}
				});
				Help.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						finish();
					}
				});
				Help.create().show();
			}
		});
		
		money_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (help_checkbox.isChecked()) {
					Record.edit().putString("Money", String.valueOf((long)(Double.parseDouble(Record.getString("Money", "")) + 10000))).commit();
				}
			}
		});
		
		song_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (Init_done == 1) {
					Record.edit().putString("Playing_song", Songlist.get((int)(_position))).commit();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Three.setClass(getApplicationContext(), MoreActivity.class);
				startActivity(Three);
				finish();
			}
		});
		
		call_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Three.setData(Uri.parse("tel:67095526"));
				Three.setAction(Intent.ACTION_DIAL);
				startActivity(Three);
				SketchwareUtil.showMessage(getApplicationContext(), "Three is a holy number.");
			}
		});
		
		share_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("User", "").equals("3")) {
					Share = "Feel the power of 3!";
				}
				else {
					Share = "Three is a holy number.";
				}
				Three.setAction(Intent.ACTION_SEND); Three.setType("text/plain");
				Three.putExtra(android.content.Intent.EXTRA_SUBJECT, "Holy Subject"); Three.putExtra(android.content.Intent.EXTRA_TEXT, Share); startActivity(Intent.createChooser(Three,"Share holiness of 3 using..."));
			}
		});
		
		version_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Help.setTitle("Version Changes");
				Help.setMessage("1.2.3 (30-3-2021)\nAdded more buttons, a music jukebox and more shop items.");
				Help.setPositiveButton("Got it!(3)", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				Help.setNegativeButton("Got it!(2)", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				Help.setNeutralButton("Got it!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				Help.create().show();
			}
		});
		
		login_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("User", "").equals("")) {
					Counter = 0;
					for(int _repeat25 = 0; _repeat25 < (int)(Userlist.size()); _repeat25++) {
						if (user_name.getText().toString().equals(Userlist.get((int)(Counter)))) {
							Record.edit().putString("User", String.valueOf((long)(Counter))).commit();
							SketchwareUtil.showMessage(getApplicationContext(), "Logged in as ".concat(user_name.getText().toString().concat("(ID:".concat(String.valueOf((long)(Counter)).concat(")")))));
							break;
						}
						Counter++;
					}
					if (Counter == Userlist.size()) {
						if (Record.getString("Login_hint", "").equals("1")) {
							SketchwareUtil.showMessage(getApplicationContext(), "Wrong user name! Correct one is ".concat(Userlist.get((int)(0))));
						}
						else {
							SketchwareUtil.showMessage(getApplicationContext(), "Wrong user name!");
						}
						user_name.setText("");
					}
				}
				else {
					Record.edit().putString("User", "").commit();
					user_name.setText("");
				}
			}
		});
	}
	private void initializeLogic() {
		Init_done = 0;
		setTitle("Help");
		Userlist.clear();
		Userlist.add("NormalUser");
		Userlist.add("SuspiciousUser");
		Userlist.add("AuspiciousUser");
		Userlist.add("HolyThree");
		Songlist.clear();
		Songlist.add("Walk in the forest alarm");
		Songlist.add("Bell");
		if (!Record.getString("Songs", "").equals("0")) {
			Songlist.add("Hangout incoming call");
		}
		song_spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Songlist));
		song_spinner.setSelection((int)(Songlist.indexOf(Record.getString("Playing_song", ""))));
		if (!Record.getString("User", "").equals("")) {
			user_name.setText(Userlist.get((int)(Double.parseDouble(Record.getString("User", "")))));
		}
		Init_done = 1;
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
						if (Record.getString("User", "").equals("")) {
							user_name.setEnabled(true);
							login_btn.setText("Login");
						}
						else {
							user_name.setEnabled(false);
							login_btn.setText("Logout");
						}
						((ArrayAdapter)song_spinner.getAdapter()).notifyDataSetChanged();
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
