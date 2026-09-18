package com.my.bulletstuff;

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
import java.util.HashMap;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeActivity extends Activity {
	
	
	private HashMap<String, Object> Translations = new HashMap<>();
	
	private TextView textview1;
	private TextView textview2;
	private CheckBox tutorial_checkbox;
	private Button play_btn;
	private Button deck_btn;
	private Button language_btn;
	private Button credits_btn;
	
	private Intent good_intent = new Intent();
	private AlertDialog.Builder complex_start_dialog;
	private AlertDialog.Builder credits;
	private SharedPreferences trans;
	private SharedPreferences player;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		tutorial_checkbox = (CheckBox) findViewById(R.id.tutorial_checkbox);
		play_btn = (Button) findViewById(R.id.play_btn);
		deck_btn = (Button) findViewById(R.id.deck_btn);
		language_btn = (Button) findViewById(R.id.language_btn);
		credits_btn = (Button) findViewById(R.id.credits_btn);
		complex_start_dialog = new AlertDialog.Builder(this);
		credits = new AlertDialog.Builder(this);
		trans = getSharedPreferences("translations", Activity.MODE_PRIVATE);
		player = getSharedPreferences("player", Activity.MODE_PRIVATE);
		
		tutorial_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					player.edit().putString("show_tutorial", "1").commit();
				}
				else {
					player.edit().putString("show_tutorial", "0").commit();
				}
			}
		});
		
		play_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), GameActivity.class);
				if (tutorial_checkbox.isChecked()) {
					good_intent.putExtra("tutorial", "1");
				}
				else {
					good_intent.putExtra("tutorial", "0");
				}
				good_intent.putExtra("seed", "");
				startActivity(good_intent);
			}});
		play_btn.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View _view){
				final EditText editttext1= new EditText(HomeActivity.this);
				LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				
				editttext1.setLines(1);
				editttext1.setLayoutParams(lpar);
				complex_start_dialog.setView(editttext1);
				complex_start_dialog.setTitle("Complex start");
				complex_start_dialog.setMessage("Seed: (leave blank for random)");
				complex_start_dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						good_intent.setClass(getApplicationContext(), GameActivity.class);
						if (tutorial_checkbox.isChecked()) {
							good_intent.putExtra("tutorial", "1");
						}
						else {
							good_intent.putExtra("tutorial", "0");
						}
						good_intent.putExtra("seed",editttext1.getText().toString());
						startActivity(good_intent);
					}
				});
				complex_start_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				complex_start_dialog.create().show();
				return true;
			}
		});
		
		deck_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), DeckActivity.class);
				startActivity(good_intent);
			}
		});
		
		language_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), LanguageActivity.class);
				startActivity(good_intent);
				finish();
			}
		});
		
		credits_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				credits.setTitle("Credits");
				credits.setMessage("Made with Sketchware\nImages from:\nhttps://emojipedia.org/\nDeveloper contact coming soon (maybe? Still closed alpha now)");
				credits.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				credits.create().show();
			}
		});
	}
	private void initializeLogic() {
		if (player.getString("show_tutorial", "").equals("1")) {
			tutorial_checkbox.setChecked(true);
		}
		if (trans.getString(player.getString("language", ""), "").equals("")) {
			good_intent.setClass(getApplicationContext(), LanguageActivity.class);
			startActivity(good_intent);
			finish();
		}
		else {
			Translations = new Gson().fromJson(trans.getString(player.getString("language", ""), ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			tutorial_checkbox.setText(translate("show_tutorial"));
			play_btn.setText(translate("play_btn"));
			deck_btn.setText(translate("deck_btn"));
			language_btn.setText(translate("language_btn"));
			credits_btn.setText(translate("credits_btn"));
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _asd () {
	}
	String translate(String text){
		if(Translations!=null&& Translations.containsKey(text.toLowerCase()))return Translations.get(text.toLowerCase()).toString();
		return text;
	}
	{
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
