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
import android.widget.Switch;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import android.media.MediaPlayer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CompoundButton;
import java.text.DecimalFormat;

public class MainActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private double Money = 0;
	private double Income = 0;
	private double Cost = 0;
	private double Counter = 0;
	private String Old_image = "";
	private double Income_bonus = 0;
	private String Old_playing_song = "";
	
	private LinearLayout linear2;
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private TextView cash_view;
	private TextView rate_view;
	private LinearLayout table;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private LinearLayout linear8;
	private LinearLayout linear9;
	private LinearLayout linear10;
	private LinearLayout linear11;
	private LinearLayout linear12;
	private LinearLayout linear13;
	private Button button1;
	private TextView textview1;
	private TextView textview2;
	private Button button2;
	private TextView textview3;
	private TextView textview4;
	private Button button3;
	private TextView textview5;
	private TextView textview6;
	private Button button4;
	private TextView textview7;
	private TextView textview8;
	private Button button5;
	private TextView textview9;
	private TextView textview10;
	private Button button6;
	private TextView textview11;
	private TextView textview12;
	private Button button7;
	private TextView textview13;
	private TextView textview14;
	private Button button8;
	private TextView textview15;
	private TextView textview16;
	private Button button9;
	private TextView textview17;
	private TextView textview18;
	private Button button10;
	private TextView textview19;
	private TextView textview20;
	private Button more_btn;
	private Switch lessmoreswitch;
	private Button less_btn;
	
	private Intent Three = new Intent();
	private SharedPreferences Record;
	private TimerTask Clock;
	private MediaPlayer Background;
	private AlertDialog.Builder Dialog;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		cash_view = (TextView) findViewById(R.id.cash_view);
		rate_view = (TextView) findViewById(R.id.rate_view);
		table = (LinearLayout) findViewById(R.id.table);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		linear12 = (LinearLayout) findViewById(R.id.linear12);
		linear13 = (LinearLayout) findViewById(R.id.linear13);
		button1 = (Button) findViewById(R.id.button1);
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		button2 = (Button) findViewById(R.id.button2);
		textview3 = (TextView) findViewById(R.id.textview3);
		textview4 = (TextView) findViewById(R.id.textview4);
		button3 = (Button) findViewById(R.id.button3);
		textview5 = (TextView) findViewById(R.id.textview5);
		textview6 = (TextView) findViewById(R.id.textview6);
		button4 = (Button) findViewById(R.id.button4);
		textview7 = (TextView) findViewById(R.id.textview7);
		textview8 = (TextView) findViewById(R.id.textview8);
		button5 = (Button) findViewById(R.id.button5);
		textview9 = (TextView) findViewById(R.id.textview9);
		textview10 = (TextView) findViewById(R.id.textview10);
		button6 = (Button) findViewById(R.id.button6);
		textview11 = (TextView) findViewById(R.id.textview11);
		textview12 = (TextView) findViewById(R.id.textview12);
		button7 = (Button) findViewById(R.id.button7);
		textview13 = (TextView) findViewById(R.id.textview13);
		textview14 = (TextView) findViewById(R.id.textview14);
		button8 = (Button) findViewById(R.id.button8);
		textview15 = (TextView) findViewById(R.id.textview15);
		textview16 = (TextView) findViewById(R.id.textview16);
		button9 = (Button) findViewById(R.id.button9);
		textview17 = (TextView) findViewById(R.id.textview17);
		textview18 = (TextView) findViewById(R.id.textview18);
		button10 = (Button) findViewById(R.id.button10);
		textview19 = (TextView) findViewById(R.id.textview19);
		textview20 = (TextView) findViewById(R.id.textview20);
		more_btn = (Button) findViewById(R.id.more_btn);
		lessmoreswitch = (Switch) findViewById(R.id.lessmoreswitch);
		less_btn = (Button) findViewById(R.id.less_btn);
		Record = getSharedPreferences("Stat", Activity.MODE_PRIVATE);
		Dialog = new AlertDialog.Builder(this);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 10 + (Double.parseDouble(Record.getString("Button1", "")) * 2);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button1", String.valueOf((long)(Double.parseDouble(Record.getString("Button1", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 1))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 36 + (Double.parseDouble(Record.getString("Button2", "")) * 9);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button2", String.valueOf((long)(Double.parseDouble(Record.getString("Button2", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 3))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 160 + (Double.parseDouble(Record.getString("Button3", "")) * 40);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button3", String.valueOf((long)(Double.parseDouble(Record.getString("Button3", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 10))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 600 + (Double.parseDouble(Record.getString("Button4", "")) * 200);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button4", String.valueOf((long)(Double.parseDouble(Record.getString("Button4", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 25))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 5000 + (Double.parseDouble(Record.getString("Button5", "")) * 1250);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button5", String.valueOf((long)(Double.parseDouble(Record.getString("Button5", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 135))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 30000 + (Double.parseDouble(Record.getString("Button6", "")) * 7000);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button6", String.valueOf((long)(Double.parseDouble(Record.getString("Button6", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 600))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 160000 + (Double.parseDouble(Record.getString("Button7", "")) * 40000);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button7", String.valueOf((long)(Double.parseDouble(Record.getString("Button7", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 2500))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 600000 + (Double.parseDouble(Record.getString("Button8", "")) * 130000);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button8", String.valueOf((long)(Double.parseDouble(Record.getString("Button8", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 7000))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 3000000 + (Double.parseDouble(Record.getString("Button9", "")) * 500000);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button9", String.valueOf((long)(Double.parseDouble(Record.getString("Button9", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 22000))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		button10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Money = Double.parseDouble(Record.getString("Money", ""));
				Cost = 100000000 + (Double.parseDouble(Record.getString("Button10", "")) * 10000000);
				if (!(Money < Cost)) {
					Record.edit().putString("Money", String.valueOf(Money - Cost)).commit();
					Record.edit().putString("Button10", String.valueOf((long)(Double.parseDouble(Record.getString("Button10", "")) + 1))).commit();
					Record.edit().putString("Income", String.valueOf((long)(Double.parseDouble(Record.getString("Income", "")) + 600000))).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "You need $".concat(String.valueOf(Cost - Money).concat(" more.")));
				}
			}
		});
		
		more_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (lessmoreswitch.isChecked()) {
					Three.setClass(getApplicationContext(), NothingActivity.class);
				}
				else {
					Three.setClass(getApplicationContext(), MoreActivity.class);
				}
				startActivity(Three);
			}
		});
		
		lessmoreswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				Money = Double.parseDouble(Record.getString("Money", ""));
				Record.edit().putString("Money", String.valueOf(Money + 50)).commit();
			}
		});
		
		less_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (lessmoreswitch.isChecked()) {
					Three.setClass(getApplicationContext(), MoreActivity.class);
				}
				else {
					Three.setClass(getApplicationContext(), NothingActivity.class);
				}
				startActivity(Three);
			}
		});
	}
	private void initializeLogic() {
		setTitle("project 2");
		_Init_var();
		Clock = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Income = Double.parseDouble(Record.getString("Income", ""));
						if (Record.getString("Paused", "").equals("0")) {
							Income_bonus = 1 + (Double.parseDouble(Record.getString("Bell", "")) / 500);
							if (Record.getString("User", "").equals("2")) {
								Income_bonus = Income_bonus + 0.03d;
							}
							Money = Double.parseDouble(Record.getString("Money", "")) + ((Income * Income_bonus) / 10);
						}
						else {
							Money = Double.parseDouble(Record.getString("Money", ""));
						}
						Record.edit().putString("Money", String.valueOf(Money)).commit();
						Record.edit().putString("Income", String.valueOf(Income)).commit();
						if (Money > 10000000000000000d) {
							cash_view.setText("$".concat(Record.getString("Money", "")));
						}
						else {
							cash_view.setText("$".concat(String.valueOf((long)(Math.floor(Money)))));
						}
						rate_view.setText("$".concat(String.valueOf((long)(Math.floor(Income))).concat("/s")));
						if (!Record.getString("Image", "").equals(Old_image)) {
							Old_image = Record.getString("Image", "");
							if (Old_image.equals("")) {
								vscroll1.setBackgroundResource(0);
							}
							else {
								if (Old_image.equals("Three")) {
									vscroll1.setBackgroundResource(R.drawable.three);
								}
								else {
									android.content.res.Resources res = getApplicationContext().getResources();
									android.graphics.drawable.BitmapDrawable bd = new android.graphics.drawable.BitmapDrawable(res, Old_image);
									vscroll1.setBackgroundDrawable(bd);
								}
							}
						}
						if (Double.parseDouble(Record.getString("Apple", "")) > 13) {
							linear10.setVisibility(View.VISIBLE);
						}
						else {
							linear10.setVisibility(View.GONE);
						}
						if (Double.parseDouble(Record.getString("Apple", "")) > 32) {
							linear11.setVisibility(View.VISIBLE);
						}
						else {
							linear11.setVisibility(View.GONE);
						}
						if (Double.parseDouble(Record.getString("Apple", "")) > 53) {
							linear12.setVisibility(View.VISIBLE);
						}
						else {
							linear12.setVisibility(View.GONE);
						}
						if (Double.parseDouble(Record.getString("Apple", "")) > 53) {
							linear12.setVisibility(View.VISIBLE);
						}
						else {
							linear12.setVisibility(View.GONE);
						}
						if (Double.parseDouble(Record.getString("Apple", "")) > 122) {
							linear13.setVisibility(View.VISIBLE);
						}
						else {
							linear13.setVisibility(View.GONE);
						}
						_Music_stuff();
						_update_btn();
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
	
	@Override
	public void onBackPressed() {
		Record.edit().putString("Money", "25").commit();
		Record.edit().putString("Income", "0").commit();
		Record.edit().putString("Paused", "0").commit();
		Record.edit().putString("Pausable", "0").commit();
		Record.edit().putString("Help", "0").commit();
		Record.edit().putString("Background", "0").commit();
		Record.edit().putString("Bell", "0").commit();
		Record.edit().putString("Apple", "0").commit();
		Record.edit().putString("Shop", "0").commit();
		Record.edit().putString("User", "").commit();
		Record.edit().putString("Login_hint", "0").commit();
		Record.edit().putString("Discount", "0").commit();
		Record.edit().putString("Songs", "0").commit();
		Record.edit().putString("Playing_song", "Walk in the forest alarm").commit();
		Counter = 0;
		for(int _repeat16 = 0; _repeat16 < (int)(10); _repeat16++) {
			Counter++;
			Record.edit().putString("Button".concat(String.valueOf((long)(Counter))), "0").commit();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (SketchwareUtil.getRandom((int)(1), (int)(100)) == 3) {
			SketchwareUtil.showMessage(getApplicationContext(), "Well comes back!");
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "Welcome back!");
		}
	}
	private void _Init_var () {
		if (Record.getString("Money", "").equals("")) {
			Record.edit().putString("Money", "25").commit();
		}
		if (Record.getString("Income", "").equals("")) {
			Record.edit().putString("Income", "0").commit();
		}
		if (Record.getString("Paused", "").equals("")) {
			Record.edit().putString("Paused", "0").commit();
		}
		if (Record.getString("Background", "").equals("")) {
			Record.edit().putString("Background", "0").commit();
		}
		if (Record.getString("Pausable", "").equals("")) {
			Record.edit().putString("Pausable", "0").commit();
		}
		if (Record.getString("Help", "").equals("")) {
			Record.edit().putString("Help", "0").commit();
		}
		if (Record.getString("Bell", "").equals("")) {
			Record.edit().putString("Bell", "0").commit();
		}
		if (Record.getString("Apple", "").equals("")) {
			Record.edit().putString("Apple", "0").commit();
		}
		if (Record.getString("Shop", "").equals("")) {
			Record.edit().putString("Shop", "0").commit();
		}
		if (Record.getString("Music", "").equals("")) {
			Record.edit().putString("Music", "1").commit();
		}
		if (Record.getString("Login_hint", "").equals("")) {
			Record.edit().putString("Login_hint", "0").commit();
		}
		if (Record.getString("Discount", "").equals("")) {
			Record.edit().putString("Discount", "0").commit();
		}
		if (Record.getString("Songs", "").equals("")) {
			Record.edit().putString("Songs", "0").commit();
		}
		if (Record.getString("Playing_song", "").equals("")) {
			Record.edit().putString("Playing_song", "Walking in the forest alarm").commit();
		}
		Counter = 0;
		for(int _repeat30 = 0; _repeat30 < (int)(10); _repeat30++) {
			Counter++;
			if (Record.getString("Button".concat(String.valueOf((long)(Counter))), "").equals("")) {
				Record.edit().putString("Button".concat(String.valueOf((long)(Counter))), "0").commit();
			}
		}
		Background = MediaPlayer.create(getApplicationContext(), R.raw.bell);
	}
	
	
	private void _update_btn () {
		Cost = 10 + (Double.parseDouble(Record.getString("Button1", "")) * 2);
		textview2.setText("$".concat(Record.getString("Button1", "").concat("/s")));
		button1.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button1.setEnabled(false);
		}
		else {
			button1.setEnabled(true);
		}
		Cost = 36 + (Double.parseDouble(Record.getString("Button2", "")) * 9);
		textview4.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button2", "")) * 3)).concat("/s")));
		button2.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button2.setEnabled(false);
		}
		else {
			button2.setEnabled(true);
		}
		Cost = 160 + (Double.parseDouble(Record.getString("Button3", "")) * 40);
		textview6.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button3", "")) * 10)).concat("/s")));
		button3.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button3.setEnabled(false);
		}
		else {
			button3.setEnabled(true);
		}
		Cost = 600 + (Double.parseDouble(Record.getString("Button4", "")) * 200);
		textview8.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button4", "")) * 25)).concat("/s")));
		button4.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button4.setEnabled(false);
		}
		else {
			button4.setEnabled(true);
		}
		Cost = 5000 + (Double.parseDouble(Record.getString("Button5", "")) * 1250);
		textview10.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button5", "")) * 135)).concat("/s")));
		button5.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button5.setEnabled(false);
		}
		else {
			button5.setEnabled(true);
		}
		Cost = 30000 + (Double.parseDouble(Record.getString("Button6", "")) * 7000);
		textview12.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button6", "")) * 600)).concat("/s")));
		button6.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button6.setEnabled(false);
		}
		else {
			button6.setEnabled(true);
		}
		Cost = 160000 + (Double.parseDouble(Record.getString("Button7", "")) * 40000);
		textview14.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button7", "")) * 2500)).concat("/s")));
		button7.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button7.setEnabled(false);
		}
		else {
			button7.setEnabled(true);
		}
		Cost = 600000 + (Double.parseDouble(Record.getString("Button8", "")) * 130000);
		textview16.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button8", "")) * 7000)).concat("/s")));
		button8.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button8.setEnabled(false);
		}
		else {
			button8.setEnabled(true);
		}
		Cost = 3000000 + (Double.parseDouble(Record.getString("Button9", "")) * 500000);
		textview18.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button9", "")) * 22000)).concat("/s")));
		button9.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button9.setEnabled(false);
		}
		else {
			button9.setEnabled(true);
		}
		Cost = 100000000 + (Double.parseDouble(Record.getString("Button10", "")) * 10000000);
		textview20.setText("$".concat(String.valueOf((long)(Double.parseDouble(Record.getString("Button10", "")) * 600000)).concat("/s")));
		button10.setText("$".concat(String.valueOf((long)(Cost))));
		if (Cost > Money) {
			button10.setEnabled(false);
		}
		else {
			button10.setEnabled(true);
		}
	}
	
	
	private void _Music_stuff () {
		if (!Record.getString("Playing_song", "").equals(Old_playing_song)) {
			Background.reset();
			if (Record.getString("Playing_song", "").equals("Hangout incoming call")) {
				Background = MediaPlayer.create(getApplicationContext(), R.raw.hangouts_call);
			}
			else {
				if (Record.getString("Playing_song", "").equals("Bell")) {
					Background = MediaPlayer.create(getApplicationContext(), R.raw.bell);
				}
				else {
					Background = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
				}
			}
			Background.setLooping(true);
			Background.start();
			Old_playing_song = Record.getString("Playing_song", "");
		}
		if (Record.getString("Music", "").equals("0")) {
			Background.pause();
		}
		else {
			Background.start();
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
