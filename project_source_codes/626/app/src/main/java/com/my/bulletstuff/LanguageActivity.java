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
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LanguageActivity extends Activity {
	
	
	private HashMap<String, Object> lang_internal = new HashMap<>();
	
	private ArrayList<String> lang_names = new ArrayList<>();
	
	private ListView listview1;
	
	private SharedPreferences trans;
	private SharedPreferences player;
	private Intent creative_intent = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.language);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		listview1 = (ListView) findViewById(R.id.listview1);
		trans = getSharedPreferences("translations", Activity.MODE_PRIVATE);
		player = getSharedPreferences("player", Activity.MODE_PRIVATE);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				player.edit().putString("language", lang_internal.get(lang_names.get((int)(_position))).toString()).commit();
				creative_intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(creative_intent);
				finish();
			}
		});
	}
	private void initializeLogic() {
		lang_internal = new Gson().fromJson("{}", new TypeToken<HashMap<String, Object>>(){}.getType());
		Map<String, ?> langs=trans.getAll(); 
		for (Map.Entry<String, ?> en:langs.entrySet()){
			String v=en.getValue().toString();
			String display=en.getKey();
			try{HashMap<String,String>lang= new Gson().fromJson(v,new TypeToken<HashMap<String, String>>(){}.getType());
				if(lang.containsKey("lang_name"))display=lang.get("lang_name");
			}catch(Exception e){
				SketchwareUtil.showMessage(getApplicationContext(),"Warning: there was error when loading "+display);}
			lang_names.add(display);
			lang_internal.put(display, en.getKey());
		}
		listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, lang_names));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		if (lang_names.size() == 0) {
			SketchwareUtil.showMessage(getApplicationContext(), "No language available!");
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
