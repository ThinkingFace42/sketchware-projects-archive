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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeActivity extends Activity {
	
	
	private String Result = "";
	private HashMap<String, Object> debt = new HashMap<>();
	private HashMap<String, Object> filter = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> debts = new ArrayList<>();
	private ArrayList<String> debt_text = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> display_debts = new ArrayList<>();
	
	private LinearLayout linear1;
	private TextView status_text;
	private ListView debt_view;
	private Button add_btn;
	private Button filter_btn;
	private Button button3;
	private Button help_btn;
	
	private Intent good_intent = new Intent();
	private SharedPreferences file;
	private AlertDialog.Builder help_dialog;
	private AlertDialog.Builder details_dialog;
	private Intent special_intent = new Intent();
	private AlertDialog.Builder delete_dialog;
	private SharedPreferences set;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		status_text = (TextView) findViewById(R.id.status_text);
		debt_view = (ListView) findViewById(R.id.debt_view);
		add_btn = (Button) findViewById(R.id.add_btn);
		filter_btn = (Button) findViewById(R.id.filter_btn);
		button3 = (Button) findViewById(R.id.button3);
		help_btn = (Button) findViewById(R.id.help_btn);
		file = getSharedPreferences("data", Activity.MODE_PRIVATE);
		help_dialog = new AlertDialog.Builder(this);
		details_dialog = new AlertDialog.Builder(this);
		delete_dialog = new AlertDialog.Builder(this);
		set = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		
		debt_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				details_dialog.setTitle("Info");
				debt = display_debts.get((int)_position);
				_DebtToText(debt);
				details_dialog.setMessage(Result+(debt.containsKey("note")?("\nNotes:"+debt.get("note").toString()):""));
				details_dialog.setPositiveButton("Duplicate", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						special_intent.setClass(getApplicationContext(), AddActivity.class);
						special_intent.putExtra("template", new Gson().toJson(debt));
						startActivity(special_intent);
					}
				});
				details_dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						debts.remove(debt);
						_Save();
					}
				});
				if (debt.containsKey("resolved") && debt.get("resolved").toString().equals("1")) {
					details_dialog.setNeutralButton("Un-resolve", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							debt.put("resolved", "0");
							_Save();
						}
					});
				}
				else {
					details_dialog.setNeutralButton("Resolve", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							debt.put("resolved", "1");
							_Save();
						}
					});
				}
				details_dialog.create().show();
			}
		});
		
		debt_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				debt = display_debts.get((int)_position);
				if (set.getString("delete_dialog", "").equals("1")) {
					_DebtToText(debt);
					delete_dialog.setTitle("Delete confirmation");
					delete_dialog.setMessage("Are you sure you want to delete this?\n\n".concat(Result));
					delete_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							debts.remove(debt);
							_Save();
							SketchwareUtil.showMessage(getApplicationContext(), "Deleted");
						}
					});
					delete_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					delete_dialog.create().show();
				}
				else {
					debts.remove(debt);
					_Save();
					SketchwareUtil.showMessage(getApplicationContext(), "Deleted");
				}
				return true;
			}
		});
		
		add_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), AddActivity.class);
				startActivity(good_intent);
			}
		});
		
		filter_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), FilterActivity.class);
				startActivity(good_intent);
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				good_intent.setClass(getApplicationContext(), SettingActivity.class);
				startActivity(good_intent);
			}
		});
		
		help_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				help_dialog.setTitle("Help");
				help_dialog.setMessage("-Click on \"ADD\" to add a new debt.\n-Click on \"FILTER\" to access filtering and sorting options.\n-Click on an item to see info and actions.\n-Long click on an item to delete it.\nOther features are WIP!!\n\nVersion 3.728");
				help_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				help_dialog.create().show();
			}
		});
	}
	private void initializeLogic() {
		if (file.getString("debts", "").equals("")) {
			file.edit().putString("debts", "[{\"debtor\":\"A Wu\",\"creditor\":\"Kyrre\",\"number\":\"788\",\"date\":\"03-03-2023\",\"note\":\"Give me 788\"},{\"creditor\":\"Ivan\",\"number\":\"333\",\"currency\":\"bungor\",\"note\":\"3 is a holy number.\"}]").commit();
		}
		if (file.getString("filter", "").equals("")) {
			file.edit().putString("filter", "{\"debtor\":\"Kyrre\",\"creditor\":\"Ivan\",\"note\":\"Yoshinoya\"}").commit();
		}
		if (set.getString("delete_dialog", "").equals("")) {
			set.edit().putString("delete_dialog", "1").commit();
		}
		_Refresh();
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
	public void onResume() {
		super.onResume();
		_Refresh();
	}
	private void _DebtToText (final HashMap<String, Object> _Debt) {
		Result = "";
		Result=(_Debt.containsKey("debtor")?_Debt.get("debtor").toString():"Someone")+" owes "+(_Debt.containsKey("creditor")?_Debt.get("creditor").toString():"someone")+" ";
		if("1".equals(_Debt.get("resolved"))){Result+=" (Resolved)";}
		String curr=(_Debt.containsKey("currency")?_Debt.get("currency").toString():"$");
		String num=(_Debt.containsKey("number")?_Debt.get("number").toString():"?");
		if(curr.length()>1){Result+=num+" "+curr;}
		else{Result+=curr+num;}
		Result+=".";
		if(_Debt.containsKey("date")){Result+=" ("+_Debt.get("date").toString()+")";}
	}
	
	
	private void _Refresh () {
		debts = new Gson().fromJson(file.getString("debts", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		filter = new Gson().fromJson(file.getString("filter", ""), new TypeToken<HashMap<String, Object>>(){}.getType());
		//debt_text.clear();
		display_debts.clear();
		int unresolved=0;
		
		debts.sort(new DebtSorter(filter.containsKey("sort")?filter.remove("sort").toString():"default"));
		for(HashMap<String, Object> d:debts){
			if(!(filter.containsKey("debtor")&&!filter.get("debtor").equals(d.get("debtor")) ||filter.containsKey("creditor")&&!filter.get("creditor").equals(d.get("creditor")) ||
			filter.containsKey("currency")&&!filter.get("currency").equals(d.get("currency")) ||
			filter.containsKey("note")&&!(d.containsKey("note")&&d.get("note").toString().toLowerCase().contains(filter.get("note").toString())))){
				display_debts.add(d);
				if(!"1".equals(d.get("resolved"))){
					unresolved++;}
			}
			
			//_DebtToText(d);
			//debt_text.add(Result);
		}
		HashMap<String, Double> total=new HashMap<>();
		Boolean b=false;
		for(HashMap<String, Object> d:display_debts){
			if(!(d.containsKey("resolved")&&d.get("resolved").equals("1"))&&d.containsKey("currency")){
				String c=d.get("currency").toString();
				if(d.containsKey("number")){
					String n=d.get("number").toString();
					Double x=0d;
					if(n.equals("∞"))x=Double.MAX_VALUE;
					else if(n.equals("-∞"))x=Double.MIN_VALUE;
					else{
						try{x=Double.parseDouble(n);}
						catch(Exception e){b=true;}
					}
					if(total.containsKey(c)){
						total.put(c,x+total.get(c));}
					else{total.put(c,x);}
				}}
		}
		String s2="Total:"+(b?"(Some entries unrecognized!)":"");
		for (Map.Entry<String, Double> en: total.entrySet()) {
			s2+=en.getKey()+":"+String.valueOf(en.getValue())+" ";
			 }
		String s=(filter.size()==0?"":"(Filtered) ");
		if(debts.size()==0){
			s+=("No entries yet, create one with ADD button!");
		}
		else if(unresolved==0){s+=(String.valueOf(display_debts.size())+" entries, all resolved");}
		else{s+=(String.valueOf(display_debts.size())+" entries, "+String.valueOf(unresolved)+ " unresolved");}
		status_text.setText(s+"\n"+s2);
		if (false) {
			debt_view.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, debt_text));
		}
		else {
			debt_view.setAdapter(new Debt_viewAdapter(display_debts));
		}
		((BaseAdapter)debt_view.getAdapter()).notifyDataSetChanged();
		if (false) {
			SketchwareUtil.showMessage(getApplicationContext(), "Refreshed");
		}
	}
	
	
	private void _Save () {
		file.edit().putString("debts", new Gson().toJson(debts)).commit();
		_Refresh();
	}
	
	
	private void _asd () {
	}
	public class DebtSorter implements Comparator<HashMap<String,Object>>{
		String mode;
		public DebtSorter(String mode){
			this.mode=mode;
		}
		public int compare(HashMap<String, Object> a, HashMap<String, Object> b){
			int i=0;
			String s1="";
			String s2="";
			switch(mode){
				case "debtor":
				s1=(a.containsKey("debtor")?a.get("debtor").toString():"");
				s2=(b.containsKey("debtor")?b.get("debtor").toString():"");
				i=s1.compareTo(s2);
				break;
				case "creditor":
				s1=(a.containsKey("creditor")?a.get("creditor").toString():"");
				s2=(b.containsKey("creditor")?b.get("creditor").toString():"");
				i=s1.compareTo(s2);
				break;
			}
			return i+evaluate(a)-evaluate(b);
		}
		int evaluate(HashMap<String, Object> a){
			return ("1".equals(a.get("resolved"))?1000000:0);
		}
	}
	/*
Boolean isInt(String s){
return (s.matches("-?[0123456789]{1,9}"));
}
*/
	{
	}
	
	
	public class Debt_viewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Debt_viewAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.debt_view, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView num_text = (TextView) _v.findViewById(R.id.num_text);
			final TextView name_text = (TextView) _v.findViewById(R.id.name_text);
			final TextView info_text = (TextView) _v.findViewById(R.id.info_text);
			
			debt = _data.get((int)_position);
			name_text.setText((debt.containsKey("debtor")?debt.get("debtor").toString():"Someone")+"<-"+(debt.containsKey("creditor")?debt.get("creditor").toString():"someone")+("1".equals(debt.get("resolved"))?" (Resolved)":""));
			
			String curr=(debt.containsKey("currency")?debt.get("currency").toString():"$");
			String num=(debt.containsKey("number")?debt.get("number").toString():"?");
			if(curr.length()>1){num_text.setText(num+" "+curr);}
			else{num_text.setText(curr+num);}
			String info=(debt.containsKey("note")?debt.get("note").toString():"")+(debt.containsKey("date")?(" ("+debt.get("date").toString()+")"):"");
			info_text.setText(info);
			if (debt.containsKey("resolved") && debt.get("resolved").toString().equals("1")) {
				num_text.setTextColor(0xFFBDBDBD);
				name_text.setTextColor(0xFFBDBDBD);
				info_text.setTextColor(0xFFBDBDBD);
			}
			else {
				num_text.setTextColor(0xFF000000);
				name_text.setTextColor(0xFF000000);
				info_text.setTextColor(0xFF616161);
			}
			
			return _v;
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
