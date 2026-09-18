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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import android.content.SharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.media.SoundPool;
import android.view.View;
import java.text.DecimalFormat;

public class ShopActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private double Money = 0;
	private boolean Result = false;
	private double Cost = 0;
	private double btn3 = 0;
	
	private LinearLayout linear1;
	private ScrollView vscroll1;
	private TextView cash_view;
	private TextView rate_view;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private LinearLayout linear17;
	private LinearLayout linear9;
	private LinearLayout linear21;
	private LinearLayout linear13;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private LinearLayout linear8;
	private TextView error_icon;
	private Button buy_error;
	private TextView help_icon;
	private Button buy_help;
	private TextView background_icon;
	private Button buy_background;
	private LinearLayout linear10;
	private LinearLayout linear11;
	private LinearLayout linear12;
	private TextView pause_icon;
	private Button buy_pause;
	private TextView bell_icon;
	private Button buy_bell;
	private TextView apple_icon;
	private Button buy_apple;
	private LinearLayout linear14;
	private LinearLayout linear18;
	private LinearLayout linear19;
	private TextView nothing_icon;
	private Button buy_nothing;
	private TextView bungor_icon;
	private Button buy_bungor;
	private TextView donation_icon;
	private Button buy_donation;
	private LinearLayout linear22;
	private LinearLayout linear23;
	private LinearLayout linear24;
	private TextView login_icon;
	private Button buy_login;
	private TextView song_icon;
	private Button buy_song;
	private TextView discount_icon;
	private Button buy_discount;
	private LinearLayout linear20;
	private LinearLayout linear15;
	private LinearLayout linear16;
	private TextView something_icon;
	private Button buy_something;
	private TextView back_icon;
	private Button back_btn;
	private TextView textview3;
	private Button button3;
	
	private SharedPreferences Record;
	private TimerTask Clock;
	private Intent Three = new Intent();
	private SoundPool bell;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.shop);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		cash_view = (TextView) findViewById(R.id.cash_view);
		rate_view = (TextView) findViewById(R.id.rate_view);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear17 = (LinearLayout) findViewById(R.id.linear17);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		linear21 = (LinearLayout) findViewById(R.id.linear21);
		linear13 = (LinearLayout) findViewById(R.id.linear13);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		error_icon = (TextView) findViewById(R.id.error_icon);
		buy_error = (Button) findViewById(R.id.buy_error);
		help_icon = (TextView) findViewById(R.id.help_icon);
		buy_help = (Button) findViewById(R.id.buy_help);
		background_icon = (TextView) findViewById(R.id.background_icon);
		buy_background = (Button) findViewById(R.id.buy_background);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		linear12 = (LinearLayout) findViewById(R.id.linear12);
		pause_icon = (TextView) findViewById(R.id.pause_icon);
		buy_pause = (Button) findViewById(R.id.buy_pause);
		bell_icon = (TextView) findViewById(R.id.bell_icon);
		buy_bell = (Button) findViewById(R.id.buy_bell);
		apple_icon = (TextView) findViewById(R.id.apple_icon);
		buy_apple = (Button) findViewById(R.id.buy_apple);
		linear14 = (LinearLayout) findViewById(R.id.linear14);
		linear18 = (LinearLayout) findViewById(R.id.linear18);
		linear19 = (LinearLayout) findViewById(R.id.linear19);
		nothing_icon = (TextView) findViewById(R.id.nothing_icon);
		buy_nothing = (Button) findViewById(R.id.buy_nothing);
		bungor_icon = (TextView) findViewById(R.id.bungor_icon);
		buy_bungor = (Button) findViewById(R.id.buy_bungor);
		donation_icon = (TextView) findViewById(R.id.donation_icon);
		buy_donation = (Button) findViewById(R.id.buy_donation);
		linear22 = (LinearLayout) findViewById(R.id.linear22);
		linear23 = (LinearLayout) findViewById(R.id.linear23);
		linear24 = (LinearLayout) findViewById(R.id.linear24);
		login_icon = (TextView) findViewById(R.id.login_icon);
		buy_login = (Button) findViewById(R.id.buy_login);
		song_icon = (TextView) findViewById(R.id.song_icon);
		buy_song = (Button) findViewById(R.id.buy_song);
		discount_icon = (TextView) findViewById(R.id.discount_icon);
		buy_discount = (Button) findViewById(R.id.buy_discount);
		linear20 = (LinearLayout) findViewById(R.id.linear20);
		linear15 = (LinearLayout) findViewById(R.id.linear15);
		linear16 = (LinearLayout) findViewById(R.id.linear16);
		something_icon = (TextView) findViewById(R.id.something_icon);
		buy_something = (Button) findViewById(R.id.buy_something);
		back_icon = (TextView) findViewById(R.id.back_icon);
		back_btn = (Button) findViewById(R.id.back_btn);
		textview3 = (TextView) findViewById(R.id.textview3);
		button3 = (Button) findViewById(R.id.button3);
		Record = getSharedPreferences("Stat", Activity.MODE_PRIVATE);
		
		buy_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Record.edit().putString("Money", String.valueOf(Double.parseDouble(Record.getString("Money", "")) - 5)).commit();
				SketchwareUtil.showMessage(getApplicationContext(), "Error 3200: Trying to divide 3 by 0.  The result may not be valid.");
			}
		});
		
		buy_help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Help", "").equals("0")) {
					_Buy(0);
					if (Result) {
						Record.edit().putString("Help", "1").commit();
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You already bought this item!");
				}
			}
		});
		
		buy_background.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Background", "").equals("0")) {
					_Buy(3);
					if (Result) {
						Record.edit().putString("Background", "1").commit();
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You already bought this item!");
				}
			}
		});
		
		buy_pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Pausable", "").equals("0")) {
					_Buy(10);
					if (Result) {
						Record.edit().putString("Pausable", "1").commit();
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You already bought this item!");
				}
			}
		});
		
		bell_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!Record.getString("Bell", "").equals("0")) {
					Cost = bell.play((int)(1), 1.0f, 1.0f, 1, (int)(1), 1.0f);
					SketchwareUtil.showMessage(getApplicationContext(), "Ding ding~");
				}
			}
		});
		
		buy_bell.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_BellCost(Double.parseDouble(Record.getString("Bell", "")));
				if (Cost > Math.pow(2, 63)) {
					SketchwareUtil.showMessage(getApplicationContext(), "This item has been sold out!");
				}
				else {
					_Buy(Cost);
					if (Result) {
						Record.edit().putString("Bell", String.valueOf((long)(1 + Double.parseDouble(Record.getString("Bell", ""))))).commit();
					}
				}
			}
		});
		
		apple_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Double.parseDouble(Record.getString("Apple", "")) > 32) {
					apple_icon.setBackgroundResource(R.drawable.golden_apple);
				}
			}
		});
		
		buy_apple.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Buy(100);
				if (Result) {
					Record.edit().putString("Apple", String.valueOf((long)(1 + Double.parseDouble(Record.getString("Apple", ""))))).commit();
				}
			}
		});
		
		buy_nothing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Buy(10 * (1 + Double.parseDouble(Record.getString("Shop", ""))));
				if (Result) {
					Record.edit().putString("Shop", String.valueOf((long)(1 + Double.parseDouble(Record.getString("Shop", ""))))).commit();
				}
			}
		});
		
		buy_bungor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		buy_donation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Buy(1000);
			}
		});
		
		buy_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Record.getString("Login_hint", "").equals("0")) {
					_Buy(3000);
					if (Result) {
						Record.edit().putString("Login_hint", "1").commit();
					}
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You already bought this item!");
				}
			}
		});
		
		buy_song.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Buy(10000);
				if (Result) {
					Record.edit().putString("Songs", String.valueOf((long)(1 + Double.parseDouble(Record.getString("Songs", ""))))).commit();
				}
			}
		});
		
		buy_discount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Double.parseDouble(Record.getString("Discount", "")) > 4) {
					SketchwareUtil.showMessage(getApplicationContext(), "This item has reached max level!");
				}
				else {
					_Buy(20000 * Math.pow(10, Double.parseDouble(Record.getString("Discount", ""))));
					if (Result) {
						Record.edit().putString("Discount", String.valueOf((long)(1 + Double.parseDouble(Record.getString("Discount", ""))))).commit();
					}
				}
			}
		});
		
		buy_something.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Buy(Math.pow(10, 60));
				if (Result) {
					SketchwareUtil.showMessage(getApplicationContext(), "You win!");
				}
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
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Coming sooner!");
				btn3++;
				if (btn3 > 33) {
					Record.edit().putString("Money", String.valueOf(Double.parseDouble(Record.getString("Money", "")) * 10)).commit();
				}
			}
		});
	}
	private void initializeLogic() {
		if ((Double.parseDouble(Record.getString("Shop", "")) > 5) && (SketchwareUtil.getRandom((int)(1), (int)(3)) == 3)) {
			setTitle("Marketplace");
		}
		else {
			setTitle("Shop");
		}
		bell = new SoundPool((int)(1), AudioManager.STREAM_MUSIC, 0);
		Cost = bell.load(getApplicationContext(), R.raw.bell, 1);
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
						if (Record.getString("Background", "").equals("1")) {
							buy_background.setEnabled(false);
						}
						else {
							buy_background.setEnabled(true);
						}
						if (Record.getString("Pausable", "").equals("1")) {
							buy_pause.setEnabled(false);
						}
						else {
							buy_pause.setEnabled(true);
						}
						if (Record.getString("Help", "").equals("1")) {
							buy_help.setEnabled(false);
						}
						else {
							buy_help.setEnabled(true);
						}
						if (Record.getString("Login_hint", "").equals("1")) {
							buy_login.setEnabled(false);
						}
						else {
							buy_login.setEnabled(true);
						}
						if (Record.getString("Bell", "").equals("0")) {
							bell_icon.setText("Bell");
							buy_bell.setText("$30");
						}
						else {
							_BellCost(Double.parseDouble(Record.getString("Bell", "")));
							bell_icon.setText("Bell\nCount:".concat(Record.getString("Bell", "")));
							if (Cost > Math.pow(2, 63)) {
								buy_bell.setText("Sold out");
								buy_bell.setEnabled(false);
							}
							else {
								buy_bell.setText("$".concat(String.valueOf((long)(Cost))));
								buy_bell.setEnabled(true);
							}
						}
						if (Record.getString("Apple", "").equals("0")) {
							apple_icon.setText("Apple");
						}
						else {
							apple_icon.setText("Apple\nCount:".concat(Record.getString("Apple", "")));
						}
						if (Record.getString("Songs", "").equals("0")) {
							song_icon.setText("More songs");
						}
						else {
							song_icon.setText("More songs\nCount:".concat(Record.getString("Songs", "")));
						}
						buy_nothing.setText("$".concat(String.valueOf((long)(10 * (1 + Double.parseDouble(Record.getString("Shop", "")))))));
						if (Double.parseDouble(Record.getString("Shop", "")) > 0) {
							buy_something.setEnabled(true);
							buy_donation.setEnabled(true);
						}
						else {
							buy_something.setEnabled(false);
							buy_donation.setEnabled(false);
						}
						if (Double.parseDouble(Record.getString("Shop", "")) > 6) {
							linear21.setVisibility(View.VISIBLE);
						}
						else {
							linear21.setVisibility(View.GONE);
						}
						if (Record.getString("Discount", "").equals("0")) {
							discount_icon.setText("Dr. Lee");
							buy_discount.setText("$20000");
						}
						else {
							discount_icon.setText("Dr. Lee\nLevel:".concat(Record.getString("Discount", "")));
							if (Double.parseDouble(Record.getString("Discount", "")) > 4) {
								buy_discount.setText("Max level");
								buy_discount.setEnabled(false);
							}
							else {
								buy_discount.setText("$".concat(String.valueOf((long)(20000 * Math.pow(10, Double.parseDouble(Record.getString("Discount", "")))))));
								buy_discount.setEnabled(true);
							}
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
	
	private void _Buy (final double _Price) {
		Result = false;
		Money = Double.parseDouble(Record.getString("Money", ""));
		if (!(Money < _Price)) {
			Record.edit().putString("Money", String.valueOf(Money - _Price)).commit();
			Result = true;
			SketchwareUtil.showMessage(getApplicationContext(), "Purchase success");
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(_Price - Money).concat(" more.")));
		}
	}
	
	
	private void _BellCost (final double _Bells) {
		if (_Bells == 0) {
			Cost = 30;
		}
		else {
			Cost = Math.floor((30 * Math.pow(1.03d, _Bells)) * (1 - (Double.parseDouble(Record.getString("Discount", "")) * 0.1d)));
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
