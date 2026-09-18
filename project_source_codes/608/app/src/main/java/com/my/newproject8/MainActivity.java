package com.my.newproject8;

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
import android.widget.ScrollView;
import android.widget.CalendarView;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import android.content.ClipData;
import android.content.ClipboardManager;

public class MainActivity extends Activity {
	
	
	private String Display = "";
	private double Day = 0;
	private double Result = 0;
	private String realdisplay = "";
	
	private ScrollView vscroll2;
	private CalendarView calendarview1;
	private Button button1;
	private TextView textview1;
	
	private Calendar Daytime = Calendar.getInstance();
	private Calendar DateCalculating = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		vscroll2 = (ScrollView) findViewById(R.id.vscroll2);
		calendarview1 = (CalendarView) findViewById(R.id.calendarview1);
		button1 = (Button) findViewById(R.id.button1);
		textview1 = (TextView) findViewById(R.id.textview1);
		
		calendarview1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView _param1, int _param2, int _param3, int _param4) {
				final int _year = _param2;
				final int _month = _param3;
				final int _day = _param4;
				Display = "Selected day is:".concat(String.valueOf((long)(_day)).concat("/".concat(String.valueOf((long)(_month + 1))).concat("/".concat(String.valueOf((long)(_year))))));
				_Cal_YuenFun(_year, _month + 1, _day);
				Display = Display.concat("\n Yuen fun is:".concat(String.valueOf((long)(Result))));
				textview1.setText(Display);
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				DateCalculating = Calendar.getInstance();
				calendarview1.setDate((long)(DateCalculating.getTimeInMillis()), true, true);
				Display = "Today is:".concat(new SimpleDateFormat("dd/MM/yyyy").format(DateCalculating.getTime()));
				_Cal_YuenFun(Double.parseDouble(new SimpleDateFormat("yyyy").format(DateCalculating.getTime())), Double.parseDouble(new SimpleDateFormat("MM").format(DateCalculating.getTime())), Double.parseDouble(new SimpleDateFormat("dd").format(DateCalculating.getTime())));
				Display = Display.concat("\n Yuen fun today is:".concat(String.valueOf((long)(Result))));
				Display = Display.concat("\n");
				for(int _repeat82 = 0; _repeat82 < (int)(7); _repeat82++) {
					DateCalculating.add(Calendar.DAY_OF_MONTH, (int)(1));
					_Cal_YuenFun(Double.parseDouble(new SimpleDateFormat("yyyy").format(DateCalculating.getTime())), Double.parseDouble(new SimpleDateFormat("MM").format(DateCalculating.getTime())), Double.parseDouble(new SimpleDateFormat("dd").format(DateCalculating.getTime())));
					Display = Display.concat("\n Yuen fun on ".concat(new SimpleDateFormat("dd/MM/yyyy").format(DateCalculating.getTime()).concat(" is:".concat(String.valueOf((long)(Result))))));
				}
				textview1.setText(Display);
			}
		});
	}
	private void initializeLogic() {
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _Cal_YuenFun (final double _Year, final double _Month, final double _Day) {
		Daytime.set(Calendar.YEAR, (int)(_Year));
		Daytime.set(Calendar.MONTH, (int)(_Month - 1));
		Daytime.set(Calendar.DAY_OF_MONTH, (int)(_Day));
		Daytime.add(Calendar.MONTH, (int)(Double.parseDouble(new SimpleDateFormat("d").format(Daytime.getTime()))));
		Daytime.add(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(new SimpleDateFormat("M").format(Daytime.getTime())) * 5));
		Daytime.add(Calendar.YEAR, (int)(Double.parseDouble(new SimpleDateFormat("M").format(Daytime.getTime())) * Double.parseDouble(new SimpleDateFormat("d").format(Daytime.getTime()))));
		Day = Double.parseDouble(new SimpleDateFormat("yyyyMMdd").format(Daytime.getTime()));
		if (false) {
			SketchwareUtil.showMessage(getApplicationContext(), String.valueOf((long)(Day)));
		}
		Day = Day - (65536 * Math.floor(Day / 65536));
		Result = ((2 * (Day * Day)) + (Day * 97)) + 333;
		Result = Result - (65536 * Math.floor(Result / 65536));
	}
	
	
	private void _Quote (final double _Input) {
		
	}
	
	
	private void _extra () {
	}
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		menu.add(0, 0, 0, "Condemn!🍎");
		menu.add(1, 1, 1, "Copy Yuen Fun");
		menu.add(2, 2, 2, "Share Yuen Fun");
		return true;
	}
	 @Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case 0:
			_more1();
			break;
			case 1:
			_more2();
			break;
			case 2:
			_more3();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void _more1 () {
		SketchwareUtil.showMessage(getApplicationContext(), "Condemn!");
	}
	
	
	private void _more2 () {
		((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", textview1.getText().toString()));
		SketchwareUtil.showMessage(getApplicationContext(), "Copied.");
	}
	
	
	private void _more3 () {
		realdisplay = textview1.getText().toString();
		Intent i = new Intent(android.content.Intent.ACTION_SEND); i.setType("text/plain");
		i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bungorful Subject"); i.putExtra(android.content.Intent.EXTRA_TEXT, realdisplay); startActivity(Intent.createChooser(i,"Share Bungorfully using..."));
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
