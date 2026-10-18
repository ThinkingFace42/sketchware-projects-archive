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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class EditorActivity extends Activity {
	
	private HashMap<String, Object> empty_map = new HashMap<>();
	private String selected_name = "";
	private LinkedHashMap<String,String> loaded_card;
	private String dialog_result = "";
	private String value_filter_string = "";
	private String tag_filter_string = "";
	private HashMap<String, Object> value_filter_map = new HashMap<>();
	private HashMap<String, Object> tag_filter_map = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> Loaded_cards = new ArrayList<>();
	private ArrayList<String> Loaded_card_names = new ArrayList<>();
	private ArrayList<String> Loaded_card_keys = new ArrayList<>();
	private ArrayList<String> Loaded_card_entries = new ArrayList<>();
	
	private Spinner card_select_spinner;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private Button add_card_btn;
	private Button delete_card_btn;
	private Button filter_card_btn;
	private Button help_btn;
	private LinearLayout linear4;
	private ScrollView vscroll2;
	private ListView card_entry_view;
	private LinearLayout linear5;
	private EditText card_edittext;
	private Button save_card_edit_btn;
	
	private SharedPreferences cards_filesaver;
	private AlertDialog.Builder edit_value_dialog;
	private AlertDialog.Builder help_dialog;
	private AlertDialog.Builder filter_dialog;
	private AlertDialog.Builder delete_key_dialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.editor);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		card_select_spinner = findViewById(R.id.card_select_spinner);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		add_card_btn = findViewById(R.id.add_card_btn);
		delete_card_btn = findViewById(R.id.delete_card_btn);
		filter_card_btn = findViewById(R.id.filter_card_btn);
		help_btn = findViewById(R.id.help_btn);
		linear4 = findViewById(R.id.linear4);
		vscroll2 = findViewById(R.id.vscroll2);
		card_entry_view = findViewById(R.id.card_entry_view);
		linear5 = findViewById(R.id.linear5);
		card_edittext = findViewById(R.id.card_edittext);
		save_card_edit_btn = findViewById(R.id.save_card_edit_btn);
		cards_filesaver = getSharedPreferences("cards", Activity.MODE_PRIVATE);
		edit_value_dialog = new AlertDialog.Builder(this);
		help_dialog = new AlertDialog.Builder(this);
		filter_dialog = new AlertDialog.Builder(this);
		delete_key_dialog = new AlertDialog.Builder(this);
		
		card_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (_position > -1) {
					_Load_card_info(Loaded_card_names.get((int)(_position)));
				} else {
					_Load_card_info("");
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		add_card_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final EditText editttext1= new EditText(EditorActivity.this);
				
				LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				editttext1.setLines(6);
				editttext1.setLayoutParams(lpar);
				edit_value_dialog.setView(editttext1);
				edit_value_dialog.setTitle("Enter name");
				edit_value_dialog.setMessage("Enter name for the new card");
				edit_value_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						dialog_result=editttext1.getText().toString();
						if (dialog_result.equals("")) {
							SketchwareUtil.showMessage(getApplicationContext(), "Please enter a nonempty card name!");
						} else {
							if (cards_filesaver.contains(dialog_result)) {
								SketchwareUtil.showMessage(getApplicationContext(), "A card with this name exist already!");
							} else {
								selected_name = dialog_result;
								loaded_card=new LinkedHashMap<>();
								loaded_card.put("name",dialog_result);
								_Writeback_card_info();
								_Load_card_names();
							}
						}
					}
				});
				edit_value_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_card_names();
					}
				});
				edit_value_dialog.create().show();
			}
		});
		
		delete_card_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				cards_filesaver.edit().remove(selected_name).commit();
				SketchwareUtil.showMessage(getApplicationContext(), selected_name.concat(" removed from database"));
				_Load_card_names();
			}
		});
		
		filter_card_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final EditText editttext1= new EditText(EditorActivity.this);
				final EditText editttext2= new EditText(EditorActivity.this);
				
				LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				editttext1.setLines(3);
				editttext1.setLayoutParams(lpar);
				editttext1.setText(value_filter_string);
				editttext1.setHint("Filter by entries");
				editttext2.setLines(3);
				editttext2.setLayoutParams(lpar);
				editttext2.setText(tag_filter_string);
				editttext2.setHint("Filter by tags");
				
				LinearLayout ll=new LinearLayout(EditorActivity.this);
				ll.setOrientation(1);
				ll.setLayoutParams(lpar);
				ll.addView(editttext1);
				ll.addView(editttext2);
				filter_dialog.setView(ll);
				
				filter_dialog.setTitle("Change filter");
				filter_dialog.setMessage("The first filter is for general key-value pairs (eg {\"cost\":\"3\"}), while the second looks inside tag (eg {\"rarity\":\"common\"}).");
				filter_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						dialog_result=editttext1.getText().toString();
						if (dialog_result.equals("")) {
							dialog_result = "{}";
						}
						try{
							value_filter_map=new Gson().fromJson(dialog_result,new TypeToken<HashMap<String, Object>>(){}.getType());
							value_filter_string=dialog_result;
						}catch(RuntimeException ex){
							SketchwareUtil.showMessage(getApplicationContext(), "Value filtering failed. Maybe you submitted a malformed map?");
						}
						dialog_result=editttext2.getText().toString();
						if (dialog_result.equals("")) {
							dialog_result = "{}";
						}
						try{
							tag_filter_map=new Gson().fromJson(dialog_result,new TypeToken<HashMap<String, Object>>(){}.getType());
							tag_filter_string=dialog_result;
						}catch(RuntimeException ex){
							SketchwareUtil.showMessage(getApplicationContext(), "Tag filtering failed. Maybe you submitted a malformed map?");
						}
						_Load_card_names();
					}
				});
				filter_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_card_names();
					}
				});
				filter_dialog.create().show();
			}
		});
		
		help_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				help_dialog.setTitle("Card editor help");
				help_dialog.setMessage("-Select cards to edit from the top spinner.\n-Edit value of attributes by clicking on the list view.\n-Alternatively, edit the underlying string directly. Remember to press save though (there is validation to ensure it is a map).\n-Add new cards or remove the selected one using the buttons on top.\n-After you are done with editing, you may want to press reset on the home page because the home page preloads cards and pregenerates the campaign.\n-Currently, upon restarting the card collection is reset to the default collection of the version you are on.");
				help_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				help_dialog.create().show();
			}
		});
		
		card_entry_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				final EditText editttext1= new EditText(EditorActivity.this);
				
				LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				editttext1.setLines(6);
				editttext1.setLayoutParams(lpar);
				editttext1.setText(loaded_card.get(Loaded_card_keys.get(_position)));
				edit_value_dialog.setView(editttext1);
				edit_value_dialog.setTitle("Edit value");
				edit_value_dialog.setMessage("Enter new value for ".concat(Loaded_card_keys.get((int)(_position))));
				edit_value_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						String dialog_result=editttext1.getText().toString();
						loaded_card.put(Loaded_card_keys.get(_position),dialog_result);
						_Writeback_card_info();
					}
				});
				edit_value_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_card_info(selected_name);
					}
				});
				edit_value_dialog.create().show();
			}
		});
		
		card_entry_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				delete_key_dialog.setTitle("Delete key");
				delete_key_dialog.setMessage("Delete key ".concat(Loaded_card_keys.get((int)(_position)).concat("?")));
				delete_key_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						loaded_card.remove(Loaded_card_keys.get(_position));
						_Writeback_card_info();
					}
				});
				edit_value_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Load_card_info(selected_name);
					}
				});
				edit_value_dialog.create().show();
				return true;
			}
		});
		
		save_card_edit_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					loaded_card=new Gson().fromJson(card_edittext.getText().toString(),new TypeToken<LinkedHashMap<String, String>>(){}.getType());
					_Writeback_card_info();
				}catch(RuntimeException ex){
					SketchwareUtil.showMessage(getApplicationContext(), "The saving failed. Maybe you submitted a malformed map?");
				}
			}
		});
	}
	
	private void initializeLogic() {
		card_select_spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Loaded_card_names));
		card_entry_view.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, Loaded_card_entries));
		value_filter_string = "{}";
		value_filter_map = new HashMap<>();
		tag_filter_string = "{}";
		tag_filter_map = new HashMap<>();
		_Load_card_names();
	}
	
	public void _Load_card_names() {
		empty_map = new HashMap<>();
		ArrayList<HashMap<String, Object>> Cards_to_load=_Load_cards(value_filter_map, tag_filter_map);
		Loaded_card_names.clear();
		for(HashMap<String, Object> card:Cards_to_load){
			Loaded_card_names.add(card.getOrDefault("name","Unnamed card").toString());
		}
		((ArrayAdapter)card_select_spinner.getAdapter()).notifyDataSetChanged();
		if (Loaded_card_names.contains(selected_name)) {
			card_select_spinner.setSelection((int)(Loaded_card_names.indexOf(selected_name)));
		}
		if ((card_select_spinner.getSelectedItemPosition() > -1) && (card_select_spinner.getSelectedItemPosition() < Loaded_card_names.size())) {
			_Load_card_info(Loaded_card_names.get((int)(card_select_spinner.getSelectedItemPosition())));
		} else {
			_Load_card_info("");
		}
	}
	
	
	public ArrayList<HashMap<String, Object>> _Load_cards(final HashMap<String, Object> _value_filter, final HashMap<String, Object> _tag_filter) {
		empty_map = new Gson().fromJson("{}", new TypeToken<HashMap<String, Object>>(){}.getType());
		ArrayList<HashMap<String, Object>> results=new ArrayList<>();
		Map<String, ?> all_card=cards_filesaver.getAll();
		for (Map.Entry<String, ?> entry : all_card.entrySet()){
			
			HashMap<String, Object> card=new Gson().fromJson(entry.getValue().toString(),new TypeToken<HashMap<String, Object>>(){}.getType());
			
			HashMap<String, String> tag=new HashMap<>();
			if(card.containsKey("tag")){
				tag=new Gson().fromJson(card.get("tag").toString(),new TypeToken<HashMap<String, String>>(){}.getType());}
			
			//check if tags match
			if(card.entrySet().containsAll(_value_filter.entrySet())&&tag.entrySet().containsAll(_tag_filter.entrySet())){
				results.add(card);
			}
		}
		
		Comparator<HashMap<String,Object>> byName = (m1, m2) -> m1.getOrDefault("name","zzz").toString().compareTo(m2.getOrDefault("name","zzz").toString());
		results.sort(byName);
		return results;
		
	}
	
	
	public void _Load_card_info(final String _name) {
		selected_name = _name;
		String placeholder="{}";
		if (_name.equals("")) {
			placeholder = "";
			loaded_card=new LinkedHashMap<String, String>();
		} else {
			if (cards_filesaver.contains(_name)) {
				placeholder = cards_filesaver.getString(_name, "");
				loaded_card=new Gson().fromJson(placeholder,new TypeToken<LinkedHashMap<String, String>>(){}.getType());
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "\"".concat(_name.concat("\" not found in card database!")));
				loaded_card=new LinkedHashMap<String, String>();
			}
		}
		card_edittext.setText(placeholder);
		Loaded_card_keys.clear();
		Loaded_card_entries.clear();
		for(Map.Entry<String,String>entry: loaded_card.entrySet()){
			Loaded_card_keys.add(entry.getKey());
			Loaded_card_entries.add(entry.getKey()+":"+entry.getValue());
		}
		((BaseAdapter)card_entry_view.getAdapter()).notifyDataSetChanged();
	}
	
	
	public void _Writeback_card_info() {
		String result=new Gson().toJson(loaded_card);
		cards_filesaver.edit().putString(selected_name, result).commit();
		_Load_card_info(selected_name);
	}
	
}