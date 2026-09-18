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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.ClipData;
import android.content.ClipboardManager;

public class DeckActivity extends Activity {
	
	
	private HashMap<String, Object> ShownCard = new HashMap<>();
	private String DeckName = "";
	private HashMap<String, Object> Placeholder = new HashMap<>();
	private HashMap<String, Object> CurrentDeck = new HashMap<>();
	private String dialog_result = "";
	private String dialog_result2 = "";
	private HashMap<String, Object> Translations = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> ShownCards = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> AllUpgrades = new ArrayList<>();
	private ArrayList<String> DeckNames = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> Decks = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout linear2;
	private TextView deckname_text;
	private Spinner deck_select;
	private LinearLayout buttons_box;
	private TextView click_card_text;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private Button delete_btn;
	private Button newdeck_btn;
	private Button clipboard_btn;
	private Button import_btn;
	private TextView nodeck_text;
	private ListView card_view;
	
	private SharedPreferences upgrades;
	private SharedPreferences decks;
	private SharedPreferences player;
	private AlertDialog.Builder newdeck1;
	private AlertDialog.Builder card_dialog;
	private AlertDialog.Builder delete_dialog;
	private AlertDialog.Builder importdeck1;
	private AlertDialog.Builder importdeckwarning;
	private SharedPreferences trans;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.deck);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		deckname_text = (TextView) findViewById(R.id.deckname_text);
		deck_select = (Spinner) findViewById(R.id.deck_select);
		buttons_box = (LinearLayout) findViewById(R.id.buttons_box);
		click_card_text = (TextView) findViewById(R.id.click_card_text);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		delete_btn = (Button) findViewById(R.id.delete_btn);
		newdeck_btn = (Button) findViewById(R.id.newdeck_btn);
		clipboard_btn = (Button) findViewById(R.id.clipboard_btn);
		import_btn = (Button) findViewById(R.id.import_btn);
		nodeck_text = (TextView) findViewById(R.id.nodeck_text);
		card_view = (ListView) findViewById(R.id.card_view);
		upgrades = getSharedPreferences("upgrades", Activity.MODE_PRIVATE);
		decks = getSharedPreferences("decks", Activity.MODE_PRIVATE);
		player = getSharedPreferences("player", Activity.MODE_PRIVATE);
		newdeck1 = new AlertDialog.Builder(this);
		card_dialog = new AlertDialog.Builder(this);
		delete_dialog = new AlertDialog.Builder(this);
		importdeck1 = new AlertDialog.Builder(this);
		importdeckwarning = new AlertDialog.Builder(this);
		trans = getSharedPreferences("translations", Activity.MODE_PRIVATE);
		
		deckname_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(),decks.getAll().toString());
			}
		});
		
		deck_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				_Load_decks2(false);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		delete_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				delete_dialog.setTitle("Delete deck?");
				delete_dialog.setMessage("Do you want to delete ".concat(DeckName.concat("?")));
				delete_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						decks.edit().remove(DeckName).commit();
						_Load_decks2(false);
					}
				});
				delete_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_decks2(false);
					}
				});
				delete_dialog.create().show();
			}
		});
		
		newdeck_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final EditText editttext1= new EditText(DeckActivity.this);
				
				editttext1.setLines(1);
				LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				editttext1.setLayoutParams(lpar);
				newdeck1.setView(editttext1);
				newdeck1.setTitle(translate("new_deck_btn"));
				newdeck1.setMessage(translate("enter_name"));
				newdeck1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						dialog_result=editttext1.getText().toString();
						if(dialog_result.length()>333){
							dialog_result=dialog_result.substring(0,332);
						}
						if (dialog_result.equals("")) {
							SketchwareUtil.showMessage(getApplicationContext(), "Enter a name!");
						}
						else {
							if (decks.getString(dialog_result, "").equals("")) {
								decks.edit().putString(dialog_result, "{}").commit();
								player.edit().putString("selected_deck", dialog_result).commit();
								_Load_decks2(true);
							}
							else {
								SketchwareUtil.showMessage(getApplicationContext(), "This account name exists already!!");
							}
						}
					}
				});
				newdeck1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_decks2(false);
					}
				});
				newdeck1.create().show();
			}
		});
		
		clipboard_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", decks.getString(DeckName, "")));
				SketchwareUtil.showMessage(getApplicationContext(), "Copied to clipboard!");
			}
		});
		
		import_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				{
					final EditText editttext1= new EditText(DeckActivity.this);
					
					LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					editttext1.setLayoutParams(lpar);
					importdeck1.setView(editttext1);
					importdeck1.setTitle("Import deck");
					importdeck1.setMessage("Enter the deck...");
					importdeck1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							dialog_result2=editttext1.getText().toString();
							if(dialog_result2.length()>333){
								dialog_result2=dialog_result2.substring(0,332);
							}
							if (dialog_result2.equals("")) {
								SketchwareUtil.showMessage(getApplicationContext(), "Enter a deck!");
							}
							else {
								if (decks.getString(dialog_result, "").equals("")) {
									try{
										
										Placeholder = new Gson().fromJson(dialog_result2, new TypeToken<HashMap<String, Object>>(){}.getType());
										decks.edit().putString(dialog_result, dialog_result2).commit();
										player.edit().putString("selected_deck", dialog_result).commit();
									}
									catch(Exception e)
									{
										importdeckwarning.setTitle("Corrupted deck?");
										importdeckwarning.setMessage("We have problems reading your deck. Do you really want to import this deck?"+"\n"+e.toString());
										importdeckwarning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface _dialog, int _which) {
												decks.edit().putString(dialog_result, dialog_result2).commit();
												player.edit().putString("selected_deck", dialog_result).commit();
											}
										});
										importdeckwarning.setNegativeButton("No", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface _dialog, int _which) {
												_Load_decks2(true);
											}
										});
										importdeckwarning.create().show();
									}
									_Load_decks2(true);
								}
								else {
									SketchwareUtil.showMessage(getApplicationContext(), "This deck name exists already!!");
								}
							}
						}
					});
					importdeck1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							_Load_decks2(false);
						}
					});
				}{
					final EditText editttext1= new EditText(DeckActivity.this);
					
					editttext1.setLines(1);
					LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					editttext1.setLayoutParams(lpar);
					newdeck1.setView(editttext1);
					newdeck1.setTitle("Import deck");
					newdeck1.setMessage("Enter a name...");
					newdeck1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							dialog_result=editttext1.getText().toString();
							if(dialog_result.length()>333){
								dialog_result=dialog_result.substring(0,332);
							}
							if (dialog_result.equals("")) {
								SketchwareUtil.showMessage(getApplicationContext(), "Enter a name!");
							}
							else {
								if (decks.getString(dialog_result, "").equals("")) {
									importdeck1.create().show();
								}
								else {
									SketchwareUtil.showMessage(getApplicationContext(), "This deck name exists already!!");
								}
							}
						}
					});
					newdeck1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							_Load_decks2(false);
						}
					});
					newdeck1.create().show();
				}
			}
		});
		
		card_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				HashMap<String, Object> s=ShownCards.get(_position);
				
				ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				ImageView im=new ImageView(DeckActivity.this);
				if(s.containsKey("image"))
				im.setBackgroundResource(getImage(s.get("image").toString()));
				im.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
				TextView tv=new TextView(DeckActivity.this);
				tv.setLayoutParams(lp);
				tv.setGravity(1);
				tv.setTextSize(25);
				String exp=s.containsKey("cost")?s.get("cost").toString():"N/A";
				tv.setText(translate((s.containsKey("name")?s.get("name").toString():"Unknown card"))+"\n"+(exp.equals("2147483647")?"N/A":(exp+" EXP")));
				TextView tv2=new TextView(DeckActivity.this);
				tv2.setLayoutParams(lp);
				tv2.setGravity(1);
				tv2.setText(translate(s.containsKey("desc")?s.get("desc").toString():"No description available :("));
				LinearLayout ll2=new LinearLayout(DeckActivity.this);
				ll2.setOrientation(1);
				ll2.setGravity(1); //center horizontal
				ll2.setLayoutParams(lp);
				ll2.addView(im);ll2.addView(tv);ll2.addView(tv2);
				
				card_dialog.setView(ll2);
				card_dialog.setTitle(translate("card_info"));
				card_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				card_dialog.create().show();
			}
		});
	}
	private void initializeLogic() {
		card_view.setAdapter(new Card_viewAdapter(ShownCards));
		deck_select.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, DeckNames));
		_Load_decks2(true);
		if (!player.getString("selected_deck", "").equals("") && DeckNames.contains(player.getString("selected_deck", ""))) {
			deck_select.setSelection((int)(DeckNames.indexOf(player.getString("selected_deck", ""))));
		}
		else {
			deck_select.setSelection((int)(0));
		}
		if (trans.getString(player.getString("language", ""), "").equals("")) {
			SketchwareUtil.showMessage(getApplicationContext(), "Translations not found!");
		}
		else {
			Translations = new Gson().fromJson(trans.getString(player.getString("language", ""), ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			deckname_text.setText(translate("Deck")+":");
			delete_btn.setText(translate("delete_deck_btn"));
			newdeck_btn.setText(translate("new_deck_btn"));
			clipboard_btn.setText(translate("clipboard_deck_btn"));
			import_btn.setText(translate("import_deck_btn"));
			click_card_text.setText(translate("click_card_text"));
			nodeck_text.setText(translate("nodeck_text"));
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
	
	private void _Refresh () {
		((BaseAdapter)card_view.getAdapter()).notifyDataSetChanged();
		((ArrayAdapter)deck_select.getAdapter()).notifyDataSetChanged();
	}
	
	
	private void _Load_upgrades () {
		try{
			AllUpgrades = new Gson().fromJson(upgrades.getString("upgrades", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			if (decks.getString(DeckName, "").equals("")) {
				CurrentDeck = new HashMap<>();
			}
			else {
				CurrentDeck = new Gson().fromJson(decks.getString(DeckName, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			}
			AllUpgrades.sort(new SortByName());
			ShownCards.clear();
			for(HashMap<String, Object> upgrade:AllUpgrades){
				upgrade.put("count","0");
				if(upgrade.containsKey("name")&&CurrentDeck.containsKey(upgrade.get("name").toString()))upgrade.put("count",CurrentDeck.get(upgrade.get("name").toString()));
				ShownCards.add(upgrade);
			}
			_Refresh();
		}catch(Exception e)
		{
			SketchwareUtil.showMessage(getApplicationContext(),"An error occured! Your deck seems to be corrupted.\n"+e.toString());
		}
	}
	
	
	private void _asd () {
	}
	public class SortByName implements Comparator<HashMap<String, Object>>{
		public int compare(HashMap<String, Object> a, HashMap<String, Object> b){
			String name1=a.containsKey ("name")?(String)a.get("name"):"";
			String name2=b.containsKey ("name")?(String)b.get("name"):"";
			if(name1.equals(name2))return a.hashCode()-b.hashCode();
			return name1.compareTo(name2);
		}
	}
	int getImage(String s){
		//res_name is the name of the image resource
		String res_name = s.toLowerCase();
		res_name = res_name.replace(" ", "_");
		int result=getResources ().getIdentifier(res_name, "drawable", getPackageName());
		return (result==0?R.drawable.default_image:result);
	}
	String translate(String text){
		if(Translations!=null&& Translations.containsKey(text.toLowerCase()))return Translations.get(text.toLowerCase()).toString();
		return text;
	}
	{
	}
	
	
	private void _Save_deck (final String _name) {
		if (!_name.equals("")) {
			CurrentDeck = new HashMap<>();
			for(HashMap<String,Object> upgrade:AllUpgrades){
				if(upgrade.containsKey("name")&&upgrade.containsKey("count")&&!upgrade.get("count").equals("0")){
					CurrentDeck.put(upgrade.get("name").toString(),String.valueOf(upgrade.get("count")));
				}
			}
			decks.edit().putString(_name, new Gson().toJson(CurrentDeck)).commit();
			_Refresh();
		}
	}
	
	
	private void _Load_decks2 (final boolean _setselection) {
		Map<String, ?> all=decks.getAll(); 
		TreeMap<String, Object> all2=new TreeMap<String, Object>(all);
		DeckNames.clear();
		DeckNames.addAll(all2.keySet());
		if (_setselection) {
			if (!player.getString("selected_deck", "").equals("") && DeckNames.contains(player.getString("selected_deck", ""))) {
				deck_select.setSelection((int)(DeckNames.indexOf(player.getString("selected_deck", ""))));
			}
			else {
				deck_select.setSelection((int)(0));
			}
			DeckName = DeckNames.get((int)(deck_select.getSelectedItemPosition()));
		}
		else {
			if ((deck_select.getSelectedItemPosition() > -1) && (deck_select.getSelectedItemPosition() < DeckNames.size())) {
				DeckName = DeckNames.get((int)(deck_select.getSelectedItemPosition()));
			}
			else {
				deck_select.setSelection((int)(0));
			}
			player.edit().putString("selected_deck", DeckName).commit();
		}
		if (DeckNames.size() == 0) {
			DeckName = "";
			nodeck_text.setVisibility(View.VISIBLE);
			card_view.setVisibility(View.GONE);
		}
		else {
			nodeck_text.setVisibility(View.GONE);
			card_view.setVisibility(View.VISIBLE);
		}
		_Load_upgrades();
	}
	
	
	public class Card_viewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Card_viewAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_v = _inflater.inflate(R.layout.deck_editor, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final TextView name_text = (TextView) _v.findViewById(R.id.name_text);
			final Button add_btn = (Button) _v.findViewById(R.id.add_btn);
			final TextView count_text = (TextView) _v.findViewById(R.id.count_text);
			final Button minus_btn = (Button) _v.findViewById(R.id.minus_btn);
			
			ShownCard = _data.get((int)_position);
			if (ShownCard.containsKey("name")) {
				name_text.setText(translate(ShownCard.get("name").toString()));
			}
			else {
				name_text.setText("Unknown card");
			}
			if (ShownCard.containsKey("count")) {
				count_text.setText(ShownCard.get("count").toString());
			}
			else {
				count_text.setText("0");
			}
			add_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					ShownCard = _data.get((int)_position);
					if (ShownCard.containsKey("count")) {
						ShownCard.put("count", String.valueOf((long)(Double.parseDouble(ShownCard.get("count").toString()) + 1)));
					}
					else {
						ShownCard.put("count", "1");
					}
					_Save_deck(DeckName);
				}
			});
			minus_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					ShownCard = _data.get((int)_position);
					if (ShownCard.containsKey("count") && (Double.parseDouble(ShownCard.get("count").toString()) > 1)) {
						ShownCard.put("count", String.valueOf((long)(Double.parseDouble(ShownCard.get("count").toString()) + -1)));
					}
					else {
						ShownCard.put("count", "0");
					}
					_Save_deck(DeckName);
				}
			});
			
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
