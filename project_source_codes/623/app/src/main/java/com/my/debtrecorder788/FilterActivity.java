package com.my.debtrecorder788;

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
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.SharedPreferences;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FilterActivity extends Activity {
	
	
	private HashMap<String, Object> temp = new HashMap<>();
	
	private ArrayList<String> sort_options = new ArrayList<>();
	
	private TextView textview1;
	private TextView textview2;
	private EditText edit_debtor;
	private TextView textview3;
	private EditText edit_creditor;
	private TextView textview4;
	private EditText edit_currency;
	private TextView textview5;
	private EditText edit_note;
	private TextView textview6;
	private Spinner sort_spinner;
	private Button save_button;
	
	private SharedPreferences file;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.filter);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		edit_debtor = (EditText) findViewById(R.id.edit_debtor);
		textview3 = (TextView) findViewById(R.id.textview3);
		edit_creditor = (EditText) findViewById(R.id.edit_creditor);
		textview4 = (TextView) findViewById(R.id.textview4);
		edit_currency = (EditText) findViewById(R.id.edit_currency);
		textview5 = (TextView) findViewById(R.id.textview5);
		edit_note = (EditText) findViewById(R.id.edit_note);
		textview6 = (TextView) findViewById(R.id.textview6);
		sort_spinner = (Spinner) findViewById(R.id.sort_spinner);
		save_button = (Button) findViewById(R.id.save_button);
		file = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		save_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				temp = new HashMap<>();
				if (!edit_debtor.getText().toString().equals("")) {
					temp.put("debtor", edit_debtor.getText().toString());
				}
				if (!edit_creditor.getText().toString().equals("")) {
					temp.put("creditor", edit_creditor.getText().toString());
				}
				if (!edit_currency.getText().toString().equals("")) {
					temp.put("currency", edit_currency.getText().toString());
				}
				if (!edit_note.getText().toString().equals("")) {
					temp.put("note", edit_note.getText().toString().toLowerCase());
				}
				temp.put("sort", sort_options.get((int)(sort_spinner.getSelectedItemPosition())).toLowerCase());
				file.edit().putString("filter", new Gson().toJson(temp)).commit();
				finish();
			}
		});
	}
	private void initializeLogic() {
		temp = new Gson().fromJson(file.getString("filter", ""), new TypeToken<HashMap<String, Object>>(){}.getType());
		sort_options.add("Debtor");
		sort_options.add("Creditor");
		sort_options.add("None");
		sort_spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, sort_options));
		((ArrayAdapter)sort_spinner.getAdapter()).notifyDataSetChanged();
		if(temp!=null){
			edit_debtor.setText(temp.containsKey("debtor")?temp.get("debtor").toString():"");
			edit_creditor.setText(temp.containsKey("creditor")?temp.get("creditor").toString():"");
			//edit_number.setText(temp.containsKey("number")?temp.get("number").toString():"0");
			edit_currency.setText(temp.containsKey("currency")?temp.get("currency").toString():"");
			edit_note.setText(temp.containsKey("note")?temp.get("note").toString():"");
			sort_spinner.setSelection(sort_options.indexOf(temp.containsKey("sort")?temp.get("sort").toString():"None"));
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
