package com.my.cardbattle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.regex.*;
import org.json.*;

public class SettingsActivity extends Activity {
	
	private ArrayList<String> ui_modes = new ArrayList<>();
	
	private Button back_btn;
	private LinearLayout linear1;
	private TextView ui_mode_text;
	private Spinner ui_mode_spinner;
	
	private SharedPreferences settings_filesaver;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		back_btn = findViewById(R.id.back_btn);
		linear1 = findViewById(R.id.linear1);
		ui_mode_text = findViewById(R.id.ui_mode_text);
		ui_mode_spinner = findViewById(R.id.ui_mode_spinner);
		settings_filesaver = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		ui_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				settings_filesaver.edit().putString("ui_mode", ui_modes.get((int)(_position))).commit();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
	}
	
	private void initializeLogic() {
		if (!settings_filesaver.contains("ui_mode")) {
			settings_filesaver.edit().putString("ui_mode", "webview").commit();
		} else {
			
		}
		ui_modes.add("webview");
		ui_modes.add("classic");
		ui_mode_spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, ui_modes));
		if (ui_modes.contains(settings_filesaver.getString("ui_mode", ""))) {
			ui_mode_spinner.setSelection((int)(ui_modes.indexOf(settings_filesaver.getString("ui_mode", ""))));
		}
		((ArrayAdapter)ui_mode_spinner.getAdapter()).notifyDataSetChanged();
	}
	
}