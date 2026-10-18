package com.my.cardbattle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class AccountActivity extends Activity {
	
	private HashMap<String, Object> acc_temp = new HashMap<>();
	private String dialog_result = "";
	private String acc_name = "";
	
	private ArrayList<HashMap<String, Object>> placeholder_l = new ArrayList<>();
	private ArrayList<String> acc_names = new ArrayList<>();
	
	private LinearLayout linear1;
	private TextView no_account_text;
	private ListView listview1;
	private Button add_account_btn;
	private Button delete_all_account_btn;
	private TextView textview1;
	
	private Intent intent = new Intent();
	private SharedPreferences settings_filesaver;
	private SharedPreferences campaign_filesaver;
	private AlertDialog.Builder acc1;
	private AlertDialog.Builder acc2;
	private AlertDialog.Builder delete;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.account);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		no_account_text = findViewById(R.id.no_account_text);
		listview1 = findViewById(R.id.listview1);
		add_account_btn = findViewById(R.id.add_account_btn);
		delete_all_account_btn = findViewById(R.id.delete_all_account_btn);
		textview1 = findViewById(R.id.textview1);
		settings_filesaver = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		campaign_filesaver = getSharedPreferences("campaign", Activity.MODE_PRIVATE);
		acc1 = new AlertDialog.Builder(this);
		acc2 = new AlertDialog.Builder(this);
		delete = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				delete.setTitle("Load account");
				delete.setMessage("Do you want to load ".concat(acc_names.get((int)(_position)).concat("?")));
				delete.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						settings_filesaver.edit().putString("account", acc_names.get((int)(_position))).commit();
						intent.setClass(getApplicationContext(), HomeActivity.class);
						startActivity(intent);
						finish();
					}
				});
				delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				delete.create().show();
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				delete.setTitle("Delete account");
				delete.setMessage("Do you want to delete ".concat(acc_names.get((int)(_position)).concat("?")));
				delete.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						campaign_filesaver.edit().remove(acc_names.get((int)(_position))).commit();
						SketchwareUtil.showMessage(getApplicationContext(), acc_names.get((int)(_position)).concat(" deleted"));
						_Refresh();
					}
				});
				delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				delete.create().show();
				return true;
			}
		});
		
		add_account_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_New_acc();
			}
		});
		
		delete_all_account_btn.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				delete.setTitle("Super reset");
				delete.setMessage("Do you want to delete all shared campaign and settings data??? You have to restart the app afterwards.");
				delete.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						settings_filesaver.edit().clear().apply();
						campaign_filesaver.edit().clear().apply();
						SketchwareUtil.showMessage(getApplicationContext(), "All accounts deleted");
						finish();
					}
				});
				delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				delete.create().show();
				return true;
			}
		});
		
		delete_all_account_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				delete.setTitle("Delete accounts");
				delete.setMessage("Do you want to delete all accounts?");
				delete.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						campaign_filesaver.edit().clear().apply();
						SketchwareUtil.showMessage(getApplicationContext(), "All accounts deleted");
						_Refresh();
					}
				});
				delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				delete.create().show();
			}
		});
	}
	
	private void initializeLogic() {
		placeholder_l = new Gson().fromJson("[]", new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		setTitle("Accounts management");
		listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, acc_names));
		_Refresh();
	}
	
	public void _Refresh() {
		Map<String, ?> all=campaign_filesaver.getAll(); 
		TreeMap<String, Object> all2=new TreeMap<String, Object>(all);
		acc_names.clear();
		acc_names.addAll(all2.keySet());
		if (acc_names.size() == 0) {
			no_account_text.setVisibility(View.VISIBLE);
		} else {
			no_account_text.setVisibility(View.GONE);
		}
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	
	public void _New_acc() {
		acc_temp = new HashMap<>();
		acc2.setTitle("New account");
		acc2.setMessage("Choose a campaign type,  difficulty and how cards will be drawn...");
		final LinearLayout ll= new LinearLayout(AccountActivity.this);
		final Spinner spin2= new Spinner(AccountActivity.this);
		LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		ll.setOrientation(1);
		
		spin2.setLayoutParams(lpar);
		acc2.setView(ll);
		ll.addView(spin2);
		ArrayList<String>skills=new ArrayList<>();
		skills.add("1.5 campaign");
		skills.add("1.6 campaign");
		spin2.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, skills));
		spin2.setSelection((int)(0));
		final Spinner editttext3= new Spinner(AccountActivity.this);
		
		editttext3.setLayoutParams(lpar);
		
		ll.addView(editttext3);
		ArrayList<String>diff=new ArrayList<>();
		diff.add("Trivial");
		diff.add("Very easy");
		diff.add("Easy");
		diff.add("Normal");
		diff.add("Hard");
		diff.add("Harder");
		diff.add("Very hard");
		diff.add("Hardcore");
		diff.add("Extreme");
		diff.add("Insane");
		diff.add("Terrible");
		diff.add("Mindblowing");
		diff.add("Apocalypse");
		diff.add("Unplayable");
		diff.add("Infinite");
		diff.add("Abnormal");
		editttext3.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, diff));
		editttext3.setSelection(3);
		final Spinner editttext4= new Spinner(AccountActivity.this);
		
		editttext4.setLayoutParams(lpar);
		
		ll.addView(editttext4);
		ArrayList<String>draw=new ArrayList<>();
		draw.add("Draw 5, discard at turn end");
		draw.add("Draw 2, retain at turn end");
		editttext4.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, draw));
		editttext4.setSelection(0);
		acc2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				dialog_result=spin2.getSelectedItem().toString();
				acc_temp.put("campaign_type",dialog_result);
				acc_temp.put("version", textview1.getText().toString());
				acc_temp.put("difficulty",editttext3.getSelectedItem().toString());
				acc_temp.put("draw_type",editttext4.getSelectedItem().toString());
				campaign_filesaver.edit().putString(acc_name, new Gson().toJson(acc_temp)).commit();
				settings_filesaver.edit().putString("account", acc_name).commit();
				intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		acc2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				_Refresh();
			}
		});
		final EditText editttext1= new EditText(AccountActivity.this);
		
		editttext1.setLines(1);
		editttext1.setLayoutParams(lpar);
		acc1.setView(editttext1);
		acc1.setTitle("New account");
		acc1.setMessage("Enter a name...");
		acc1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				dialog_result=editttext1.getText().toString();
				if(dialog_result.length()>333){
					dialog_result=dialog_result.substring(0,332);
				}
				if (dialog_result.equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Enter a name!");
				} else {
					if (campaign_filesaver.getString(dialog_result, "").equals("")) {
						acc_name = dialog_result;
						acc2.create().show();
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "This account name exists already!!");
					}
				}
			}
		});
		acc1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		acc1.create().show();
	}
	
}