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
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.content.ClipData;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;

public class BackgroundActivity extends Activity {
	
	public final int REQ_CD_IMAGEPICKER = 101;
	
	private Button none_btn;
	private Button three_btn;
	private Button select_btn;
	private Button back_btn;
	
	private Intent Three = new Intent();
	private SharedPreferences Record;
	private Intent ImagePicker = new Intent(Intent.ACTION_GET_CONTENT);
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.background);
		initialize(_savedInstanceState);
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
			}
			else {
				initializeLogic();
			}
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		none_btn = (Button) findViewById(R.id.none_btn);
		three_btn = (Button) findViewById(R.id.three_btn);
		select_btn = (Button) findViewById(R.id.select_btn);
		back_btn = (Button) findViewById(R.id.back_btn);
		Record = getSharedPreferences("Stat", Activity.MODE_PRIVATE);
		ImagePicker.setType("image/*");
		ImagePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		none_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Record.edit().putString("Image", "").commit();
				finish();
			}
		});
		
		three_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Record.edit().putString("Image", "Three").commit();
				finish();
			}
		});
		
		select_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(ImagePicker, REQ_CD_IMAGEPICKER);
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
	}
	private void initializeLogic() {
		setTitle("Select background...");
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_IMAGEPICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				Record.edit().putString("Image", _filePath.get((int)(0))).commit();
				SketchwareUtil.showMessage(getApplicationContext(), "Due to devloper receiving help, this is implemented.");
				Three.setClass(getApplicationContext(), MoreActivity.class);
				startActivity(Three);
				finish();
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "Cancelled!");
			}
			break;
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
