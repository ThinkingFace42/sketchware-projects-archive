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
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.CheckBox;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddActivity extends Activity {
	
	
	private HashMap<String, Object> debt = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> debts = new ArrayList<>();
	
	private TextView textview1;
	private EditText edit_debtor;
	private TextView textview2;
	private EditText edit_creditor;
	private TextView textview3;
	private EditText edit_number;
	private LinearLayout linear1;
	private TextView textview5;
	private EditText edit_date;
	private TextView textview4;
	private EditText edit_note;
	private Button save_btn;
	private CheckBox checkbox1;
	private EditText edit_currency;
	
	private Calendar calendar = Calendar.getInstance();
	private SharedPreferences file;
	private AlertDialog.Builder confirm_leave;
	private AlertDialog.Builder warning;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.add);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		textview1 = (TextView) findViewById(R.id.textview1);
		edit_debtor = (EditText) findViewById(R.id.edit_debtor);
		textview2 = (TextView) findViewById(R.id.textview2);
		edit_creditor = (EditText) findViewById(R.id.edit_creditor);
		textview3 = (TextView) findViewById(R.id.textview3);
		edit_number = (EditText) findViewById(R.id.edit_number);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		textview5 = (TextView) findViewById(R.id.textview5);
		edit_date = (EditText) findViewById(R.id.edit_date);
		textview4 = (TextView) findViewById(R.id.textview4);
		edit_note = (EditText) findViewById(R.id.edit_note);
		save_btn = (Button) findViewById(R.id.save_btn);
		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
		edit_currency = (EditText) findViewById(R.id.edit_currency);
		file = getSharedPreferences("data", Activity.MODE_PRIVATE);
		confirm_leave = new AlertDialog.Builder(this);
		warning = new AlertDialog.Builder(this);
		
		save_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edit_debtor.getText().toString().equals("") || (edit_creditor.getText().toString().equals("") || edit_number.getText().toString().equals(""))) {
					warning.setTitle("Are you sure?");
					warning.setMessage("Some key information are not filled in. Are you sure to proceed?");
					warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							_save_debt();
						}
					});
					warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					warning.create().show();
				}
				else {
					_save_debt();
				}
			}
		});
		
		checkbox1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					edit_currency.setVisibility(View.VISIBLE);
				}
				else {
					edit_currency.setVisibility(View.GONE);
				}
			}
		});
	}
	private void initializeLogic() {
		edit_currency.setVisibility(View.GONE);
		calendar = Calendar.getInstance();
		Bundle extras= getIntent().getExtras();
		HashMap<String, String> temp=null;
		if (extras != null){
			if (extras.containsKey("template")) {
				temp=new Gson().fromJson(extras.getString("template","{}"),new TypeToken<HashMap<String, String>>(){}.getType());
			}
		}
		if(temp!=null){
			edit_debtor.setText(temp.containsKey("debtor")?temp.get("debtor").toString():"Someone");
			edit_creditor.setText(temp.containsKey("creditor")?temp.get("creditor").toString():"someone");
			edit_number.setText(temp.containsKey("number")?temp.get("number").toString():"0");
			if(temp.containsKey("currency")){
				checkbox1.setChecked(true);
				edit_currency.setText(temp.get("currency"));}
			edit_note.setText(temp.containsKey("note")?temp.get("note").toString():"");
		}
		edit_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
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
		confirm_leave.setTitle("Warning");
		confirm_leave.setMessage("Are you sure you want to leave? Your data will be discarded!!");
		confirm_leave.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				finish();
			}
		});
		confirm_leave.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		confirm_leave.create().show();
	}
	private void _save_debt () {
		debt = new HashMap<>();
		if (edit_debtor.getText().toString().equals("")) {
			debt.put("debtor", "Someone");
		}
		else {
			debt.put("debtor", edit_debtor.getText().toString());
		}
		if (edit_creditor.getText().toString().equals("")) {
			debt.put("creditor", "someone");
		}
		else {
			debt.put("creditor", edit_creditor.getText().toString());
		}
		if (edit_number.getText().toString().equals("")) {
			debt.put("number", "0");
		}
		else {
			debt.put("number", edit_number.getText().toString());
		}
		debt.put("date", edit_date.getText().toString());
		if (!edit_note.getText().toString().equals("")) {
			debt.put("note", edit_note.getText().toString());
		}
		if (checkbox1.isChecked()) {
			debt.put("currency", edit_currency.getText().toString());
		}
		else {
			debt.put("currency", "$");
		}
		debts = new Gson().fromJson(file.getString("debts", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		debts.add(debt);
		file.edit().putString("debts", new Gson().toJson(debts)).commit();
		finish();
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
