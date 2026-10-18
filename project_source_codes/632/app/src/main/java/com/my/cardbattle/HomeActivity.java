package com.my.cardbattle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class HomeActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> battle = new HashMap<>();
	private String campaign_name = "";
	private HashMap<String, Object> campaign = new HashMap<>();
	private HashMap<String, Object> cards = new HashMap<>();
	private HashMap<String, Object> card_temp = new HashMap<>();
	private HashMap<String, Object> entity_placeholder = new HashMap<>();
	private double stage_loop_counter = 0;
	private HashMap<String, Object> stage_temp = new HashMap<>();
	private String state = "";
	private HashMap<String, Object> battle_temp = new HashMap<>();
	private HashMap<String, Object> player = new HashMap<>();
	private boolean overwrite_battle = false;
	private HashMap<String, Object> campaign_from_disk = new HashMap<>();
	private ListView loot_view;
	private AlertDialog loot_dialog_instance;
	private HashMap<String, Object> loot_map = new HashMap<>();
	private String string_placeholder = "";
	private HashMap<String, Object> raw_loot_map = new HashMap<>();
	private HashMap<String, Object> tag_temp = new HashMap<>();
	private Random randomizer = new Random();
	private String campaign_mode = "";
	private boolean loot_is_shop = false;
	private String curr_room_id = "";
	private String ui_mode = "";
	private String difficulty = "";
	private String draw_type = "";
	
	private ArrayList<HashMap<String, Object>> deck = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> enemies_generated = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> stages_generated = new ArrayList<>();
	private ArrayList<String> next_room_strings = new ArrayList<>();
	private ArrayList<Double> next_room_pos = new ArrayList<>();
	private ArrayList<String> loots = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> loots_map = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> raw_loots = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_placeholder = new ArrayList<>();
	
	private LinearLayout linear2;
	private TextView campaign_text;
	private LinearLayout classic_layout;
	private LinearLayout web_layout;
	private LinearLayout linear3;
	private Button settings_btn;
	private Button reset_btn;
	private Button logout_btn;
	private TextView textview1;
	private TextView textview2;
	private LinearLayout next_room_layout;
	private Button campaign_btn;
	private LinearLayout linear1;
	private Button export_btn;
	private Button export_camp_btn;
	private Button card_editor_btn;
	private TextView next_room_text;
	private ListView next_room_view;
	private Button play_btn;
	private TextView textview3;
	private WebView map_webview;
	
	private Intent intent = new Intent();
	private SharedPreferences battle_filesaver;
	private SharedPreferences campaign_filesaver;
	private SharedPreferences cards_filesaver;
	private TimerTask ui_refresh_timer;
	private AlertDialog.Builder loot_dialog;
	private SharedPreferences settings_filesaver;
	private AlertDialog.Builder credits_dialog;
	private AlertDialog.Builder remove_card_dialog;
	private AlertDialog.Builder visualizer_dialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear2 = findViewById(R.id.linear2);
		campaign_text = findViewById(R.id.campaign_text);
		classic_layout = findViewById(R.id.classic_layout);
		web_layout = findViewById(R.id.web_layout);
		linear3 = findViewById(R.id.linear3);
		settings_btn = findViewById(R.id.settings_btn);
		reset_btn = findViewById(R.id.reset_btn);
		logout_btn = findViewById(R.id.logout_btn);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		next_room_layout = findViewById(R.id.next_room_layout);
		campaign_btn = findViewById(R.id.campaign_btn);
		linear1 = findViewById(R.id.linear1);
		export_btn = findViewById(R.id.export_btn);
		export_camp_btn = findViewById(R.id.export_camp_btn);
		card_editor_btn = findViewById(R.id.card_editor_btn);
		next_room_text = findViewById(R.id.next_room_text);
		next_room_view = findViewById(R.id.next_room_view);
		play_btn = findViewById(R.id.play_btn);
		textview3 = findViewById(R.id.textview3);
		map_webview = findViewById(R.id.map_webview);
		map_webview.getSettings().setJavaScriptEnabled(true);
		map_webview.getSettings().setSupportZoom(true);
		battle_filesaver = getSharedPreferences("battle", Activity.MODE_PRIVATE);
		campaign_filesaver = getSharedPreferences("campaign", Activity.MODE_PRIVATE);
		cards_filesaver = getSharedPreferences("cards", Activity.MODE_PRIVATE);
		loot_dialog = new AlertDialog.Builder(this);
		settings_filesaver = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		credits_dialog = new AlertDialog.Builder(this);
		remove_card_dialog = new AlertDialog.Builder(this);
		visualizer_dialog = new AlertDialog.Builder(this);
		
		campaign_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				try{
					visualizer_dialog.setTitle("Room layout");
					final WebView webview2 = new WebView(HomeActivity.this);
					
					visualizer_dialog.setView(webview2);
					DisplayMetrics dm = getResources().getDisplayMetrics();
					int webHeight = (int)(dm.heightPixels * 0.8);   // tune as needed
					
					LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					webHeight);           // concrete pixels, not MATCH_PARENT
					webview2.setLayoutParams(lpar);
					
					_Setup_webview_map(webview2);
					visualizer_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					visualizer_dialog.setNegativeButton("Copy html to clipboard", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", getMapHtml()));
						}
					});
					AlertDialog dialog = visualizer_dialog.create(); 
					// if visualizer_dialog is an AlertDialog.Builder 
					dialog.setOnShowListener(d -> {
						View custom = dialog.findViewById(android.R.id.custom);
						if (custom != null) {
							ViewGroup.LayoutParams lp = custom.getLayoutParams();
							lp.height = webHeight;
							custom.setLayoutParams(lp);
						}
					});
					dialog.show();
					
				}
				catch(RuntimeException ex){
					SketchwareUtil.showMessage(getApplicationContext(), "The map visualization failed :( maybe the campaign is malformed or we just cannot support it right now");
					throw ex;
				}
				/*
final WebView webview1= new WebView(HomeActivity.this);

LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
webview1.setLayoutParams(lpar);
visualizer_dialog.setView(webview1);
//data == html data which you want to load

List<Map<String, Object>> stages_temp=new ArrayList<>(stages_generated);
 try{
String data=RoomHtmlArt.mapToHtml(stages_temp,null,curr_room_id);
 webview1.getSettings().setJavaScriptEnabled(true);
webview1.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);

//something to make the map scrollable
webview1.getSettings().setBuiltInZoomControls(true); webview1.getSettings().setDisplayZoomControls(false); webview1.getSettings().setUseWideViewPort(true);
visualizer_dialog.create().show();
String toast= "webview size = " + webview2.getWidth() + "x" + webview2.getHeight()
        + "  parent=" + webview2.getParent().getClass().getSimpleName()
        + " parentSize=" + ((View)webview2.getParent()).getWidth()
        + "x" + ((View)webview2.getParent()).getHeight();
((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", toast));
*/
			}
		});
		
		settings_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		reset_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				battle_filesaver.edit().clear().commit();
				Map<String, ?> all=cards_filesaver.getAll(); 
				cards.clear();
				cards.putAll(all);
				_New_campaign();
				SketchwareUtil.showMessage(getApplicationContext(), "Saved game and the current campaign has been reset, and the current card collection is reloaded");
				/*
battle = new Gson().fromJson("{\"discard\":\"[]\",\"library\":\"[{\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Cleave\\\",\\\"damage_enemies\\\":\\\"12\\\",\\\"desc\\\":\\\"Deal 12 damage to all enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"name\\\":\\\"Useless card in library\\\",\\\"desc\\\":\\\"This card is for testing library functionality\\\"},{\\\"exhaust_on_play\\\":\\\"1\\\",\\\"cost\\\":\\\"3\\\",\\\"name\\\":\\\"Useless card in library that you can get rid of\\\",\\\"desc\\\":\\\"This card is for testing exhaust functionality\\\"},{\\\"exhaust_on_play\\\":\\\"1\\\",\\\"cost\\\":\\\"3\\\",\\\"name\\\":\\\"Useless card in library that you can get rid of\\\",\\\"desc\\\":\\\"This card is for testing exhaust functionality\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Strike\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"damage\\\":\\\"10\\\",\\\"cost\\\":\\\"3\\\",\\\"name\\\":\\\"Double strike\\\",\\\"damage_count\\\":\\\"2\\\",\\\"desc\\\":\\\"Deal 10 damage to an enemy twice\\\",\\\"target\\\":\\\"enemy\\\"},{\\\"block_self\\\":\\\"8\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Defend\\\",\\\"desc\\\":\\\"Gain 8 block\\\",\\\"target\\\":\\\"self\\\"},{\\\"block_self\\\":\\\"8\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Defend\\\",\\\"desc\\\":\\\"Gain 8 block\\\",\\\"target\\\":\\\"self\\\"},{\\\"block_self\\\":\\\"8\\\",\\\"cost\\\":\\\"2\\\",\\\"name\\\":\\\"Defend\\\",\\\"desc\\\":\\\"Gain 8 block\\\",\\\"target\\\":\\\"self\\\"}]\",\"entities\":\"[{\\\"max_health\\\":\\\"150\\\",\\\"name\\\":\\\"Player\\\",\\\"health\\\":\\\"150\\\",\\\"group\\\":\\\"player\\\"},{\\\"max_health\\\":\\\"100\\\",\\\"name\\\":\\\"Enemy 1\\\",\\\"health\\\":\\\"100\\\",\\\"action\\\":\\\"[{\\\\\\\"attack\\\\\\\":\\\\\\\"7\\\\\\\"},{\\\\\\\"attack\\\\\\\":\\\\\\\"11\\\\\\\"}]\\\",\\\"action_pos\\\":\\\"0\\\",\\\"group\\\":\\\"enemy\\\"},{\\\"max_health\\\":\\\"100\\\",\\\"name\\\":\\\"Enemy 2\\\",\\\"health\\\":\\\"100\\\",\\\"action\\\":\\\"[{\\\\\\\"attack\\\\\\\":\\\\\\\"7\\\\\\\"},{\\\\\\\"attack\\\\\\\":\\\\\\\"7\\\\\\\",\\\\\\\"block_self\\\\\\\":\\\\\\\"7\\\\\\\"},{\\\\\\\"block_self\\\\\\\":\\\\\\\"13\\\\\\\"}]\\\",\\\"block\\\":\\\"7\\\",\\\"action_pos\\\":\\\"0\\\",\\\"group\\\":\\\"enemy\\\"},{\\\"name\\\":\\\"Random idle enemy\\\",\\\"health\\\":\\\"333\\\",\\\"group\\\":\\\"enemy\\\"}]\",\"entity_action_pos\":\"0\",\"player_pos\":\"0\",\"hand\":\"[{\\\"damage\\\":\\\"3\\\",\\\"cost\\\":\\\"3\\\",\\\"name\\\":\\\"Holy 3\\\",\\\"desc\\\":\\\"3 is a holy number.\\\"},{\\\"damage\\\":\\\"788\\\",\\\"cost\\\":\\\"788\\\",\\\"name\\\":\\\"Tribute to a bird\\\",\\\"desc\\\":\\\"Give me 788\\\",\\\"target\\\":\\\"self\\\"},{\\\"name\\\":\\\"This card has no cost and can\\\\u0027t be played\\\",\\\"desc\\\":\\\"This card is here to annoy you\\\"},{\\\"exhaust_on_play\\\":\\\"1\\\",\\\"give_ap\\\":\\\"1\\\",\\\"cost\\\":\\\"0\\\",\\\"name\\\":\\\"Adrenaline\\\",\\\"give_card\\\":\\\"2\\\",\\\"desc\\\":\\\"Gain 1 AP and draw 2 cards. Exhaust.\\\",\\\"target\\\":\\\"self\\\"},{\\\"give_ap\\\":\\\"1\\\",\\\"cost\\\":\\\"0\\\",\\\"name\\\":\\\"Energy surge\\\",\\\"desc\\\":\\\"Gain 1 AP \\\",\\\"target\\\":\\\"self\\\"}]\",\"ap\":\"6\",\"player\":\"{\\\"max_health\\\":\\\"150\\\",\\\"name\\\":\\\"Player\\\",\\\"health\\\":\\\"150\\\",\\\"group\\\":\\\"player\\\"}\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
SharedPreferences.Editor editor = battle_filesaver.edit();

// 1. Wipe all existing keys
editor.clear();

// 2. Iterate
for (Map.Entry<String, Object> entry : battle.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        editor.putString(key, (String) value);
}

editor.apply();
        
*/
			}
		});
		
		logout_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				settings_filesaver.edit().remove("account").commit();
				intent.setClass(getApplicationContext(), AccountActivity.class);
				SketchwareUtil.showMessage(getApplicationContext(), "Logged out from ".concat(campaign_name));
				startActivity(intent);
				finish();
			}
		});
		
		textview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				credits_dialog.create().show();
			}
		});
		
		textview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				credits_dialog.create().show();
			}
		});
		
		campaign_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Continue_campaign();
			}
		});
		
		export_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (true) {
					if (battle_filesaver.contains("Placeholder save name")) {
						((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", battle_filesaver.getString("Placeholder save name", "")));
						SketchwareUtil.showMessage(getApplicationContext(), "Copied to clipboard");
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "No stage found");
					}
				} else {
					Map<String, ?> all=battle_filesaver.getAll(); 
					battle.putAll(all);
					((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", new Gson().toJson(battle)));
					SketchwareUtil.showMessage(getApplicationContext(), "Copied to clipboard");
				}
			}
		});
		
		export_camp_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (campaign_filesaver.contains(campaign_name)) {
					((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", campaign_filesaver.getString(campaign_name, "")));
					SketchwareUtil.showMessage(getApplicationContext(), "Copied to clipboard");
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "No campaign found");
				}
			}
		});
		
		card_editor_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), EditorActivity.class);
				startActivity(intent);
			}
		});
		
		next_room_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				_Enter_room_pos(next_room_pos.get((int)(_position)).doubleValue());
			}
		});
		
		play_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), BattleActivity.class);
				intent.removeExtra("campaign");
				startActivity(intent);
			}
		});
		
		map_webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				
				super.onPageFinished(_param1, _param2);
			}
		});
	}
	
	private void initializeLogic() {
		if (!settings_filesaver.contains("new")) {
			settings_filesaver.edit().putString("new", "1").commit();
			settings_filesaver.edit().putString("account", "Player").commit();
		}
		if (settings_filesaver.contains("account")) {
			campaign_name = settings_filesaver.getString("account", "");
			Map<String, ?> all=cards_filesaver.getAll(); 
			cards.putAll(all);
			if (campaign_filesaver.contains(campaign_name)) {
				campaign = new Gson().fromJson(campaign_filesaver.getString(campaign_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
				if (campaign.containsKey("deck")) {
					deck = new Gson().fromJson(campaign.get("deck").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				}
				if (campaign.containsKey("rooms")) {
					stages_generated = new Gson().fromJson(campaign.get("rooms").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				}
				if (campaign.containsKey("player")) {
					player = new Gson().fromJson(campaign.get("player").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				}
			} else {
				_New_campaign();
			}
			next_room_view.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, next_room_strings));
			ui_refresh_timer = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							_Refresh_UI();
						}
					});
				}
			};
			_timer.scheduleAtFixedRate(ui_refresh_timer, (int)(0), (int)(250));
			credits_dialog.setTitle("Credits");
			credits_dialog.setMessage("Thanks for playing this game! This game is clearly inspired by certain card games (and their shoddy clones).\nAlso thanks to the people around me for proposing card names and developing the Mewtype programming language for writing card scripts (although I am still more accustomed to my own Jumpscript, which is also named by them).\nDisclaimer: since I don't know much about html, the map visualizer is mainly produced by deepseek.\nThis credit message is last updated before the release of version 1.6. I am supposed to upload the Sketchware project source code to github for this version. You can find my profile at\nhttps://github.com/ThinkingFace42.");
			credits_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					
				}
			});
			_Setup_webview_map(map_webview);
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "Not signed in!");
			intent.setClass(getApplicationContext(), AccountActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	public void _Save_campaign() {
		if (campaign_filesaver.contains(campaign_name)) {
			campaign_from_disk = new Gson().fromJson(campaign_filesaver.getString(campaign_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			if (!overwrite_battle && campaign_from_disk.containsKey("battle")) {
				campaign.put("battle", campaign_from_disk.get("battle").toString());
				if (campaign_from_disk.containsKey("player")) {
					campaign.put("player", campaign_from_disk.get("player").toString());
				}
				if (campaign_from_disk.containsKey("state")) {
					campaign.put("state", campaign_from_disk.get("state").toString());
				}
			}
		}
		campaign.put("deck", new Gson().toJson(deck));
		campaign.put("rooms", new Gson().toJson(stages_generated));
		campaign.put("player", new Gson().toJson(player));
		campaign_filesaver.edit().putString(campaign_name, new Gson().toJson(campaign)).commit();
		overwrite_battle = false;
		_Refresh_UI();
	}
	
	
	public HashMap<String, Object> _getCardByName(final String _Name) {
		if (cards_filesaver.contains(_Name)) {
			card_temp = new Gson().fromJson(cards_filesaver.getString(_Name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "\"".concat(_Name.concat("\" not found in card database!")));
			card_temp = new HashMap<>();
			card_temp.put("name", _Name);
		}
		return card_temp;
	}
	
	
	public void _New_campaign() {
		if (campaign.containsKey("campaign_type")) {
			campaign_mode = campaign.get("campaign_type").toString();
		} else {
			campaign_mode = "1.6 campaign";
		}
		if (campaign.containsKey("difficulty")) {
			difficulty = campaign.get("difficulty").toString();
		} else {
			difficulty = "Normal";
		}
		if (campaign.containsKey("draw_type")) {
			draw_type = campaign.get("draw_type").toString();
		} else {
			draw_type = "Draw 5, discard at turn end";
		}
		if (campaign_mode.equals("1.5.1 campaign") || campaign_mode.equals("1.6 campaign")) {
			_New_campaign_151();
		} else {
			campaign = new HashMap<>();
			campaign.put("campaign_type", campaign_mode);
			campaign.put("difficulty", difficulty);
			campaign.put("draw_type", draw_type);
			campaign.put("current_room_pos", "0");
			campaign.put("state", "pending");
			player = new Gson().fromJson("{\"name\":\"Player\",\"group\":\"player\",\"health\":\"150\",\"max_health\":\"150\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			player.put("name", campaign_name);
			if (draw_type.equals("Draw 2, retain at turn end")) {
				player.put("start_turn_hand_size", "7");
				player.put("start_turn_max_draw", "2");
				player.put("end_turn_hand_size", "7");
			}
			deck.clear();
			for(int _repeat12 = 0; _repeat12 < (int)(7); _repeat12++) {
				deck.add(_getCardByName("Strike"));
			}
			for(int _repeat16 = 0; _repeat16 < (int)(3); _repeat16++) {
				deck.add(_getCardByName("Defend"));
			}
			stages_generated.clear();
			stage_loop_counter = 1;
			for(int _repeat23 = 0; _repeat23 < (int)(15); _repeat23++) {
				stage_temp = new HashMap<>();
				stage_temp.put("name", "Stage ".concat(String.valueOf((long)(stage_loop_counter))));
				stage_temp.put("type", "monster");
				_Generate_enemy("idk", stage_loop_counter * difficultyMult(difficulty));
				stage_temp.put("enemies", new Gson().toJson(enemies_generated));
				stage_temp.put("id", String.valueOf((long)(stage_loop_counter)));
				stage_temp.put("next_id", "[\"".concat(String.valueOf((long)(stage_loop_counter + 1)).concat("\",\"".concat(String.valueOf((long)(stage_loop_counter + 2))).concat("\"]"))));
				raw_loots.clear();
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("type", "card");
					raw_loots.add(_item);
				}
				raw_loots.get((int)0).put("tag", "{\"rarity\":\"common\"}");
				raw_loots.get((int)0).put("repeat", "3");
				stage_temp.put("loots", new Gson().toJson(_generate_loots(raw_loots)));
				stages_generated.add(stage_temp);
				stage_loop_counter++;
			}
			overwrite_battle = true;
			_Save_campaign();
		}
		_Setup_webview_map(map_webview);
	}
	double difficultyMult(String diff){
		switch(diff){
			case "Trivial": return 0.5;
			case "Very easy": return 0.7;
			case "Easy": return 0.85;
			case "Normal":return 1;
			case "Hard": return 1.15;
			case "Harder": return 1.3;
			case "Very hard": return 1.5;
			case "Hardcore": return 1.75;
			case "Extreme": return 2;
			case "Insane": return 2.5;
			case "Terrible": return 3;
			case "Mindblowing": return 5;
			case "Apocalypse": return 10;
			case "Unplayable": return 10000;
			case "Infinite": return Double.POSITIVE_INFINITY;
			case "Abnormal": return 0;
			default: return 1;
		}
	}
	{
	}
	
	
	public void _Generate_enemy(final String _type, final double _level) {
		enemies_generated.clear();
		String enemy_str="";
		switch(_type){
			case "Rat Pack":
			enemy_str = "[{\"max_health\":\"5\",\"name\":\"Giant Rat 1\",\"health\":\"5\",\"action\":\"[{\\\"attack\\\":\\\"1\\\"},{\\\"attack\\\":\\\"1.5\\\"}]\",\"group\":\"enemy\"},{\"max_health\":\"5\",\"name\":\"Giant Rat 2\",\"health\":\"5\",\"action\":\"[{\\\"attack\\\":\\\"1\\\"},{\\\"attack\\\":\\\"1.5\\\"}]\",\"group\":\"enemy\"},{\"max_health\":\"5\",\"name\":\"Giant Rat 3\",\"health\":\"5\",\"action\":\"[{\\\"attack\\\":\\\"1\\\"},{\\\"attack\\\":\\\"1.5\\\"}]\",\"group\":\"enemy\"}]";
			enemies_generated.addAll(_Scale_enemy(enemy_str,_level,0.8));
			break;
			case "Lone Warrior":
			enemy_str = "[{\"max_health\":\"14\",\"name\":\"Warrior\",\"health\":\"14\",\"action\":\"[{\\\"attack\\\":\\\"2.5\\\"},{\\\"attack\\\":\\\"3\\\",\\\"block_self\\\":\\\"2\\\"},{\\\"attack\\\":\\\"1\\\"}]\",\"group\":\"enemy\"}]";
			enemies_generated.addAll(_Scale_enemy(enemy_str,_level,0.8));
			break;
			case "Shield Guard":
			enemy_str = "[{\"max_health\":\"22\",\"name\":\"Guard\",\"health\":\"22\",\"action\":\"[{\\\"attack\\\":\\\"1.3\\\",\\\"block_self\\\":\\\"1\\\"},{\\\"attack\\\":\\\"1.7\\\"},{\\\"attack\\\":\\\"1.7\\\",\\\"block_self\\\":\\\"2\\\"},{\\\"attack\\\":\\\"1\\\"}]\",\"group\":\"enemy\"}]";
			enemies_generated.addAll(_Scale_enemy(enemy_str,_level,0.8));
			break;
			default:
			enemy_str = "[{\"max_health\":\"20\",\"name\":\"Enemy 1\",\"health\":\"20\",\"action\":\"[{\\\"attack\\\":\\\"1\\\"},{\\\"attack\\\":\\\"1.5\\\"}]\",\"group\":\"enemy\"},{\"max_health\":\"20\",\"name\":\"Enemy 2\",\"health\":\"20\",\"action\":\"[{\\\"attack\\\":\\\"1\\\"},{\\\"block_self\\\":\\\"1\\\",\\\"attack\\\":\\\"1\\\"},{\\\"block_self\\\":\\\"2\\\"}]\",\"block\":\"1\",\"group\":\"enemy\"},{\"max_health\":\"1\",\"name\":\"Enemy 3\",\"health\":\"1\",\"block\":\"333\",\"nonscaling\":\"1\",\"group\":\"enemy\"}]";
			enemies_generated.addAll(_Scale_enemy(enemy_str,_level,1));
		}
	}
	
	
	public void _JSON_numlist(final String _input, final ArrayList<Double> _num) {
		_num.clear();
		_num.addAll(new Gson().fromJson(_input.toString(),new TypeToken<ArrayList<Integer>>(){}.getType()));
	}
	
	
	public void _Refresh_UI() {
		boolean state_different_from_disk=false;
		if (settings_filesaver.contains("ui_mode")) {
			ui_mode = settings_filesaver.getString("ui_mode", "");
		} else {
			ui_mode = "webview";
		}
		if (ui_mode.equals("classic")) {
			classic_layout.setVisibility(View.VISIBLE);
		} else {
			classic_layout.setVisibility(View.GONE);
		}
		if (ui_mode.equals("webview")) {
			web_layout.setVisibility(View.VISIBLE);
		} else {
			web_layout.setVisibility(View.GONE);
		}
		if (campaign_filesaver.contains(campaign_name)) {
			campaign_from_disk = new Gson().fromJson(campaign_filesaver.getString(campaign_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			if (!overwrite_battle && campaign_from_disk.containsKey("battle")) {
				campaign.put("battle", campaign_from_disk.get("battle").toString());
				if (campaign_from_disk.containsKey("player")) {
					campaign.put("player", campaign_from_disk.get("player").toString());
				}
				if (campaign_from_disk.containsKey("state")) {
					if (!campaign.get("state").toString().equals(campaign_from_disk.get("state").toString())) {
						campaign.put("state", campaign_from_disk.get("state").toString());
						state_different_from_disk=true;
					}
				}
			}
		}
		if (campaign.containsKey("deck")) {
			deck = new Gson().fromJson(campaign.get("deck").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		if (campaign.containsKey("rooms")) {
			stages_generated = new Gson().fromJson(campaign.get("rooms").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		if (campaign.containsKey("player")) {
			player = new Gson().fromJson(campaign.get("player").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
		}
		if (!(stages_generated.size() > 0)) {
			campaign_text.setText("No rooms in current campaign");
			return;
		}
		if (!campaign.containsKey("current_room_pos")) {
			campaign.put("current_room_pos", "0");
		}
		if (!campaign.containsKey("money")) {
			campaign.put("money", "0");
		}
		if (Double.parseDouble(campaign.get("current_room_pos").toString()) > stages_generated.size()) {
			campaign_text.setText("Current room position out of bound");
			return;
		}
		stage_temp = stages_generated.get((int)Double.parseDouble(campaign.get("current_room_pos").toString()));
		if (stage_temp.containsKey("name")) {
			campaign_text.setText("Current room: ".concat(stage_temp.get("name").toString()));
		} else {
			campaign_text.setText("Current room: unnamed");
		}
		if (stage_temp.containsKey("id")) {
			curr_room_id = stage_temp.get("id").toString();
			String js = "setCurrentRoom(" +
			JSONObject.quote(curr_room_id) + "," +
			JSONObject.quote(campaign.getOrDefault("state","").toString()) + ");";
			map_webview.evaluateJavascript(js, null);
		} else {
			curr_room_id = null;
		}
		if (stage_temp.containsKey("type")) {
			campaign_text.setText(campaign_text.getText().toString().concat("(".concat(stage_temp.get("type").toString().concat(")"))));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("(".concat("Unknown type".concat(")"))));
		}
		if (campaign.containsKey("state")) {
			campaign_text.setText(campaign_text.getText().toString().concat("\nCurrent room state: ".concat(campaign.get("state").toString())));
			if (campaign.get("state").toString().equals("completed")) {
				next_room_layout.setVisibility(View.VISIBLE);
				ArrayList<String> old_strings=new ArrayList<>(next_room_strings);
				if (stage_temp.containsKey("next_id")) {
					_load_rooms_strs_pos(stage_temp.get("next_id").toString());
				} else {
					_load_rooms_strs_pos("[]");
				}
				if(! old_strings.equals(next_room_strings))
				{
					((BaseAdapter)next_room_view.getAdapter()).notifyDataSetChanged();
				}
			} else {
				next_room_layout.setVisibility(View.GONE);
			}
			if ((campaign.get("state").toString().equals("in progress") || (campaign.get("state").toString().equals("failed") || campaign.get("state").toString().equals("looting"))) && campaign.containsKey("battle")) {
				HashMap<String, Object> battle_var;
				battle_var = new Gson().fromJson(campaign.get("battle").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				if (battle_var.containsKey("round_count")) {
					campaign_text.setText(campaign_text.getText().toString().concat("(Round ".concat(battle_var.get("round_count").toString().concat(")"))));
				} else {
					campaign_text.setText(campaign_text.getText().toString().concat("(Round ".concat("0".concat(")"))));
				}
			} else {
				
			}
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nCurrent room state: ".concat("unknown")));
			next_room_layout.setVisibility(View.GONE);
		}
		if (player.containsKey("health")) {
			if (player.containsKey("max_health")) {
				campaign_text.setText(campaign_text.getText().toString().concat("\nHealth: ".concat(player.get("health").toString().concat("/".concat(player.get("max_health").toString())))));
			} else {
				campaign_text.setText(campaign_text.getText().toString().concat("\nHealth: ".concat(player.get("health").toString())));
			}
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nHealth: ".concat("unknown")));
		}
		if (campaign.containsKey("rooms_entered")) {
			campaign_text.setText(campaign_text.getText().toString().concat("\nRooms entered: ".concat(campaign.get("rooms_entered").toString())));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nRooms entered: ".concat("0")));
		}
		if (campaign.containsKey("previous_rooms_round_count")) {
			campaign_text.setText(campaign_text.getText().toString().concat("\nTurns taken in previous rooms: ".concat(campaign.get("previous_rooms_round_count").toString())));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nTurns taken in previous rooms: ".concat("0")));
		}
		if (player.containsKey("damage_blocked")) {
			campaign_text.setText(campaign_text.getText().toString().concat("\nDamage blocked: ".concat(player.get("damage_blocked").toString())));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nDamage blocked: ".concat("0")));
		}
		if (campaign.containsKey("deck")) {
			HashMap<String, Object> card_occurance=_getValueOccuranceByKey(deck, "name");
			TreeMap<String, String> result=new TreeMap<>();
			
			for (Map.Entry<String, Object> entry : card_occurance.entrySet()) {
				result.put(entry.getKey(),String.valueOf(entry.getValue()));
			}
			string_placeholder=result.toString();
			campaign_text.setText(campaign_text.getText().toString().concat("\nDeck: ".concat(string_placeholder)));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nUnknown deck"));
		}
		if (campaign.containsKey("money")) {
			campaign_text.setText(campaign_text.getText().toString().concat("\nMoney: $".concat(campaign.get("money").toString())));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("\nMoney: $".concat("0")));
		}
		campaign_text.setText(campaign_text.getText().toString().concat("\nCampaign params: "));
		if (campaign.containsKey("campaign_type")) {
			campaign_text.setText(campaign_text.getText().toString().concat(campaign.get("campaign_type").toString().concat(", ")));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("Unknown type".concat(", ")));
		}
		if (campaign.containsKey("difficulty")) {
			campaign_text.setText(campaign_text.getText().toString().concat(campaign.get("difficulty").toString().concat(", ")));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("Normal (?)".concat(", ")));
		}
		if (campaign.containsKey("draw_type")) {
			campaign_text.setText(campaign_text.getText().toString().concat(campaign.get("draw_type").toString().concat("")));
		} else {
			campaign_text.setText(campaign_text.getText().toString().concat("Unknown draw type".concat("")));
		}
		campaign_text.setText(campaign_text.getText().toString().concat("\nClick here to see map..."));
		if (state_different_from_disk) {
			_Continue_campaign();
		}
	}
	
	
	public void _load_rooms_strs_pos(final String _ids) {
		next_room_strings.clear();
		next_room_pos.clear();
		ArrayList<String> id_list=new Gson().fromJson(_ids.toString(),new TypeToken<ArrayList<String>>(){}.getType());
		for(String id:id_list){
			//scan stages_generated for first matching room
			for(int pos=0;pos<stages_generated.size();pos++){
				HashMap<String,Object> room= stages_generated.get(pos);
				if(room.containsKey("id")){
					String room_id=room.get("id").toString();
					if(room_id.equals(id)){
						//found
						next_room_pos.add((double)pos);
						
						String desc="";
						if (room.containsKey("name")) {
							desc="Next room: ".concat(room.get("name").toString());
						} else {
							desc="Next room: unnamed";
						}
						if (room.containsKey("type")) {
							desc=(desc.concat("(".concat(room.get("type").toString().concat(")"))));
						} else {
							desc=(desc.concat("(".concat("Unknown type".concat(")"))));
						}
						
						next_room_strings.add(desc);
						
					}
				}
			}
		}
	}
	
	
	public void _Take_loot(final double _pos) {
		boolean done=true;
		if ((_pos > -1) && (_pos < loots_map.size())) {
			loot_map = loots_map.get((int)_pos);
			double price=loot_map.containsKey("money_price")?Double.parseDouble(loot_map.get("money_price").toString()):0;
			double money=campaign.containsKey("money")?Double.parseDouble(campaign.get("money").toString()):0;
			if (money < price) {
				done=false;
				SketchwareUtil.showMessage(getApplicationContext(), "Can't afford this item!!");
			} else {
				if (loot_map.containsKey("type")) {
					if (loot_map.get("type").toString().equals("card")) {
						if (loot_map.containsKey("content")) {
							card_temp = new Gson().fromJson(loot_map.get("content").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
							deck.add(card_temp);
						} else {
							SketchwareUtil.showMessage(getApplicationContext(), "The looted card has no content!!");
						}
					}
					if (loot_map.get("type").toString().equals("health")) {
						if (player.containsKey("health")) {
							double amount=0;
							if (loot_map.containsKey("amount")) {
								amount = Double.parseDouble(loot_map.get("amount").toString());
							} else {
								if (loot_map.containsKey("fraction")) {
									if (player.containsKey("max_health")) {
										amount = Double.parseDouble(player.get("max_health").toString()) * Double.parseDouble(loot_map.get("fraction").toString());
									}
								} else {
									SketchwareUtil.showMessage(getApplicationContext(), "The health loot has no amount!!");
								}
							}
							if (player.containsKey("max_health") && (amount > (Double.parseDouble(player.get("max_health").toString()) - Double.parseDouble(player.get("health").toString())))) {
								player.put("health", player.get("max_health").toString());
							} else {
								player.put("health", String.valueOf((long)(Double.parseDouble(player.get("health").toString()) + amount)));
							}
						}
					}
					if (loot_map.get("type").toString().equals("max_health")) {
						if (player.containsKey("health")) {
							double amount=0;
							if (loot_map.containsKey("amount")) {
								amount = Double.parseDouble(loot_map.get("amount").toString());
							} else {
								if (loot_map.containsKey("fraction")) {
									if (player.containsKey("max_health")) {
										amount = Double.parseDouble(player.get("max_health").toString()) * Double.parseDouble(loot_map.get("fraction").toString());
									}
								} else {
									SketchwareUtil.showMessage(getApplicationContext(), "The max health loot has no amount!!");
								}
							}
							if (player.containsKey("max_health")) {
								player.put("max_health", String.valueOf((long)(Double.parseDouble(player.get("max_health").toString()) + amount)));
							}
							player.put("health", String.valueOf((long)(Double.parseDouble(player.get("health").toString()) + amount)));
						}
					}
					if (loot_map.get("type").toString().equals("remove_card")) {
						final Spinner spinner= new Spinner(HomeActivity.this);
						LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						
						spinner.setLayoutParams(lpar);
						
						remove_card_dialog.setView(spinner);
						
						ArrayList<String>cards=new ArrayList<>();
						for(HashMap<String, Object> card:deck){
							cards.add(_card_to_string(card, false));}
						spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, cards));
						remove_card_dialog.setTitle("Select card...");
						remove_card_dialog.setMessage("Choose a card to remove...");
						remove_card_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								int pos=spinner.getSelectedItemPosition();
								if(pos>=0){
									String dialog_result=spinner.getSelectedItem().toString();
									deck.remove(pos);
								}
								else{
									SketchwareUtil.showMessage(getApplicationContext(), "No card selected!");
								}
								_Save_loot();
							}
						});
						remove_card_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						remove_card_dialog.create().show();
						done=false;
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Unknown loot type!!");
				}
			}
			if (done) {
				campaign.put("money", String.valueOf((long)(money - price)));
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "No loot selected");
		}
		if (done) {
			_Save_loot();
		}
	}
	
	
	public ArrayList<HashMap<String, Object>> _getCardsByTag(final HashMap<String, Object> _tag) {
		ArrayList<HashMap<String, Object>> results=new ArrayList<>();
		for (Map.Entry<String, Object> entry: cards.entrySet()) {
			
			HashMap<String, Object> card=new Gson().fromJson(entry.getValue().toString(),new TypeToken<HashMap<String, Object>>(){}.getType());
			
			if(card.containsKey("tag")){
				HashMap<String, String> tag=new Gson().fromJson(card.get("tag").toString(),new TypeToken<HashMap<String, String>>(){}.getType());
				
				//check if tags match
				if(tag.entrySet().containsAll(_tag.entrySet())){
					results.add(card);
				}
			}
		}
		return results;
	}
	
	
	public ArrayList<HashMap<String, Object>> _generate_loots(final ArrayList<HashMap<String, Object>> _raw_loots) {
		ArrayList<HashMap<String, Object>> loot_results=new ArrayList<>();
		ArrayList<String> generated_card_names=new ArrayList<>();
		for(int i=0;i<_raw_loots.size();i++){
			raw_loot_map=_raw_loots.get(i);
			int repeat=1;
			if(raw_loot_map.containsKey("repeat")){
				repeat=Integer.parseInt(raw_loot_map.get("repeat").toString());
			}
			for(int j=0;j<repeat;j++){
				loot_map = new HashMap<>();
				if (raw_loot_map.containsKey("type")) {
					loot_map.put("type", raw_loot_map.get("type").toString());
					if (raw_loot_map.get("type").toString().equals("card")) {
						if (raw_loot_map.containsKey("content")) {
							loot_map.put("content", raw_loot_map.get("content").toString());
							HashMap<String, String> content=new Gson().fromJson(raw_loot_map.get("content").toString(),new TypeToken<HashMap<String, String>>(){}.getType());
							
							if (content.containsKey ("name")){
								String name=content.get("name");
								if(!generated_card_names.contains(name)){generated_card_names.add(name);}
							}
							
							loot_results.add(loot_map);
						} else {
							if (raw_loot_map.containsKey("tag")) {
								tag_temp = new Gson().fromJson(raw_loot_map.get("tag").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
								ArrayList<HashMap<String, Object>> results=_getCardsByTag(tag_temp);
								ArrayList<HashMap<String, Object>> filtered_results=new ArrayList<>();
								
								for(HashMap<String, Object> card:results){
									if(!(card.containsKey("name")&&generated_card_names.contains(card.get("name").toString()))){
										filtered_results.add(card);
									}
								}
								if(filtered_results.isEmpty())filtered_results=results;
								if(!filtered_results.isEmpty()){
									HashMap<String, Object> card = filtered_results.get(randomizer.nextInt(filtered_results.size())); 
									loot_map.put("content", new Gson().toJson(card));
									if (card.containsKey ("name")){
										String name=card.get("name").toString();
										if(!generated_card_names.contains(name)){generated_card_names.add(name);}
									}
									
								}
							} else {
								
							}
						}
					}
					if (raw_loot_map.get("type").toString().equals("health")) {
						loot_map=raw_loot_map;
					}
					if (raw_loot_map.get("type").toString().equals("max_health")) {
						loot_map=raw_loot_map;
					}
					if (raw_loot_map.get("type").toString().equals("remove_card")) {
						loot_map=raw_loot_map;
					}
				} else {
					continue;
				}
				if (raw_loot_map.containsKey("money_price")) {
					loot_map.put("money_price", raw_loot_map.get("money_price").toString());
				}
				loot_results.add(loot_map);
			}
		}
		return loot_results;
	}
	
	
	public HashMap<String, Object> _getValueOccuranceByKey(final ArrayList<HashMap<String, Object>> _list, final String _key) {
		HashMap<String, Integer> valueCounts = new HashMap<>(); 
		
		for (Map<String, Object> map : _list) { 
			if (map.containsKey(_key)) { 
				String value = String.valueOf(map.get(_key)); 
				// If value exists, increment count by 1; otherwise, initialize at 1
				valueCounts.merge(value, 1, Integer::sum); 
			} 
		}
		
		HashMap<String, Object> result=new HashMap<>(valueCounts);
		return result;
		
	}
	
	
	public void _New_campaign_151() {
		campaign = new HashMap<>();
		campaign.put("campaign_type", campaign_mode);
		campaign.put("difficulty", difficulty);
		campaign.put("draw_type", draw_type);
		campaign.put("current_room_pos", "0");
		campaign.put("state", "completed");
		player = new Gson().fromJson("{\"name\":\"Player\",\"group\":\"player\",\"health\":\"200\",\"max_health\":\"200\",\"fatigue_multiplier\":\"2\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		player.put("name", campaign_name);
		deck.clear();
		if (draw_type.equals("Draw 2, retain at turn end")) {
			player.put("start_turn_hand_size", "7");
			player.put("start_turn_max_draw", "2");
			player.put("end_turn_hand_size", "7");
			for(int _repeat319 = 0; _repeat319 < (int)(7); _repeat319++) {
				deck.add(_getCardByName("Strike"));
			}
			for(int _repeat322 = 0; _repeat322 < (int)(3); _repeat322++) {
				deck.add(_getCardByName("Defend"));
			}
		} else {
			for(int _repeat35 = 0; _repeat35 < (int)(5); _repeat35++) {
				deck.add(_getCardByName("Strike"));
			}
			for(int _repeat38 = 0; _repeat38 < (int)(2); _repeat38++) {
				deck.add(_getCardByName("Defend"));
			}
		}
		stages_generated.clear();
		stage_temp = new Gson().fromJson("{\"name\":\"Starting room\",\"id\":\"00\",\"type\":\"corridor\",\"next_id\":\"[\\\"10\\\",\\\"11\\\",\\\"12\\\"]\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		stages_generated.add(stage_temp);
		stage_loop_counter = 1;
		for(int _repeat43 = 0; _repeat43 < (int)(30); _repeat43++) {
			int sublevel_count=3;
			for(int sublevel_num=0;sublevel_num<sublevel_count;sublevel_num++){
				stage_temp = new HashMap<>();
				stage_temp.put("id", String.valueOf((long)(stage_loop_counter)).concat(String.valueOf((long)(sublevel_num))));
				HashMap<String, Integer> room_weights=new HashMap<>();
				double mod6=stage_loop_counter%6;
				room_weights.put("monster",4);
				if(mod6%2==1){room_weights.put("campfire",3);
					if(stage_loop_counter>6)room_weights.put("shop",2);}
				int total=0;
				for(Map.Entry<String, Integer> entry:room_weights.entrySet()){
					total+=entry.getValue();
				}
				String room_type="unknown";
				if(total>0){
					int r= randomizer.nextInt(total);
					for(Map.Entry<String, Integer> entry:room_weights.entrySet()){
						r-=entry.getValue();
						if(r<0){room_type=entry.getKey();break;}}}
				stage_temp.put("name", "Room at level ".concat(String.valueOf((long)(stage_loop_counter))));
				raw_loots.clear();
				switch(room_type){
					case "monster":{
						stage_temp.put("type", "monster");
						String[] enemy_types={"Rat Pack","Lone Warrior","Shield Guard"};
						String enemy_type = enemy_types[randomizer.nextInt(enemy_types.length)]; 
						
						switch(enemy_type){
							case "Rat Pack":
							stage_temp.put("name","Rat nest ⚔️");break;
							case "Lone Warrior":
							stage_temp.put("name","Warrior patrol ⚔️");break;
							case "Shield Guard":
							stage_temp.put("name","Guarded room ⚔️");break;
							default:
							stage_temp.put("name","Mysterious encounter ⚔️");break;
						}
						_Generate_enemy(enemy_type, (2 + (stage_loop_counter * 0.6d)) * difficultyMult(difficulty));
						stage_temp.put("enemies", new Gson().toJson(enemies_generated));
						if ((stage_loop_counter % 6) == 0) {
							{
								HashMap<String, Object> _item = new HashMap<>();
								_item.put("type", "card");
								raw_loots.add(_item);
							}
							raw_loots.get((int)0).put("tag", "{\"rarity\":\"rare\"}");
							raw_loots.get((int)0).put("repeat", "2");
							{
								HashMap<String, Object> _item = new HashMap<>();
								_item.put("type", "card");
								raw_loots.add(_item);
							}
							raw_loots.get((int)1).put("tag", "{\"rarity\":\"uncommon\"}");
						} else {
							if (((stage_loop_counter % 2) == 0) || (stage_loop_counter > 18)) {
								{
									HashMap<String, Object> _item = new HashMap<>();
									_item.put("type", "card");
									raw_loots.add(_item);
								}
								raw_loots.get((int)0).put("tag", "{\"rarity\":\"common\"}");
								raw_loots.get((int)0).put("repeat", "2");
								{
									HashMap<String, Object> _item = new HashMap<>();
									_item.put("type", "card");
									raw_loots.add(_item);
								}
								raw_loots.get((int)1).put("tag", "{\"rarity\":\"uncommon\"}");
							} else {
								{
									HashMap<String, Object> _item = new HashMap<>();
									_item.put("type", "card");
									raw_loots.add(_item);
								}
								raw_loots.get((int)0).put("tag", "{\"rarity\":\"common\"}");
								raw_loots.get((int)0).put("repeat", "3");
							}
						}
						if ((stage_loop_counter > 12) && ((stage_loop_counter % 2) == 0)) {
							{
								HashMap<String, Object> _item = new HashMap<>();
								_item.put("type", "remove_card");
								raw_loots.add(_item);
							}
						}
						stage_temp.put("loots", new Gson().toJson(_generate_loots(raw_loots)));
						stage_temp.put("money_loot", String.valueOf((long)(2 + (stage_loop_counter / 6))));
					}
					break;
					case "campfire":{
						stage_temp.put("name", "Campfire site 🏕️");
						stage_temp.put("type", "loot");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "health");
							raw_loots.add(_item);
						}
						raw_loots.get((int)0).put("fraction", "0.25");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "max_health");
							raw_loots.add(_item);
						}
						raw_loots.get((int)1).put("amount", "25");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "remove_card");
							raw_loots.add(_item);
						}
						stage_temp.put("loots", new Gson().toJson(_generate_loots(raw_loots)));
					}
					break;
					case "shop":{
						stage_temp.put("name", "Trading post ⚖️");
						stage_temp.put("type", "loot");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "card");
							raw_loots.add(_item);
						}
						raw_loots.get((int)0).put("tag", "{\"rarity\":\"basic\"}");
						raw_loots.get((int)0).put("money_price", "2");
						raw_loots.get((int)0).put("repeat", "2");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "card");
							raw_loots.add(_item);
						}
						raw_loots.get((int)1).put("tag", "{\"rarity\":\"common\"}");
						raw_loots.get((int)1).put("money_price", "5");
						raw_loots.get((int)1).put("repeat", "3");
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("type", "card");
							raw_loots.add(_item);
						}
						raw_loots.get((int)2).put("tag", "{\"rarity\":\"uncommon\"}");
						raw_loots.get((int)2).put("money_price", "9");
						raw_loots.get((int)2).put("repeat", "2");
						stage_temp.put("loots", new Gson().toJson(_generate_loots(raw_loots)));
						stage_temp.put("shop", "1");
					}
					break;
					default:
					
					SketchwareUtil.showMessage(getApplicationContext(), "Unknown room type when generating stages!");
				}
				ArrayList<String> next_id=new ArrayList<>();
				
				next_id.add(String.valueOf((int)(stage_loop_counter+1)+String.valueOf((int)(sublevel_num))));
				
				if(randomizer.nextInt(2)==0){
					next_id.add(String.valueOf((int)(stage_loop_counter+1)+String.valueOf((int)(sublevel_num+1)%sublevel_count)));}
				stage_temp.put("next_id", new Gson().toJson(next_id));
				stages_generated.add(stage_temp);
			} //end the sublevel for loop
			stage_loop_counter++;
		}
		overwrite_battle = true;
		_Save_campaign();
	}
	
	
	public String _card_to_string(final HashMap<String, Object> _card, final boolean _multiline) {
		if (_card.containsKey("name")) {
			string_placeholder = _card.get("name").toString();
		} else {
			string_placeholder = "Unnamed card";
		}
		if (_card.containsKey("cost")) {
			string_placeholder = string_placeholder.concat("(".concat(_card.get("cost").toString().concat(")")));
		} else {
			
		}
		if(!_multiline) return string_placeholder;
		string_placeholder = string_placeholder.concat("\n");
		if (_card.containsKey("desc")) {
			string_placeholder = string_placeholder.concat(_card.get("desc").toString());
		} else {
			string_placeholder = string_placeholder.concat("(No description)");
		}
		return string_placeholder;
	}
	
	
	public void _Save_loot() {
		if (!(stages_generated.size() > 0)) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: no room exists.");
			return;
		}
		if (!campaign.containsKey("current_room_pos")) {
			campaign.put("current_room_pos", "0");
		}
		if (Double.parseDouble(campaign.get("current_room_pos").toString()) > stages_generated.size()) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: current room position out of bounds.");
			return;
		}
		stage_temp = stages_generated.get((int)Double.parseDouble(campaign.get("current_room_pos").toString()));
		if (stage_temp.containsKey("money_loot")) {
			if (campaign.containsKey("money")) {
				campaign.put("money", String.valueOf((long)(Double.parseDouble(stage_temp.get("money_loot").toString()) + Double.parseDouble(campaign.get("money").toString()))));
			} else {
				campaign.put("money", stage_temp.get("money_loot").toString());
			}
		}
		campaign.put("state", "completed");
		overwrite_battle = true;
		_Save_campaign();
	}
	
	
	public void _Setup_loot_select(final double _max_count) {
		loot_view=new ListView(this);
		boolean has_price=false;
		loots.clear();
		for(int i=0;i<loots_map.size();i++){
			loot_map=loots_map.get(i);
			String desc="Unknown loot";
			if (loot_map.containsKey("type")) {
				switch(loot_map.get("type").toString()){
					case "card":
					if (loot_map.containsKey("content")) {
						card_temp = new Gson().fromJson(loot_map.get("content").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
						desc = _card_to_string(card_temp, true);
					} else {
						
					}
					break;
					case "health":
					{
						desc="Unknown health restoration";
						if(loot_map.containsKey("amount"))
						desc="Restore "+loot_map.get("amount").toString()+" health";
						else if(loot_map.containsKey("fraction"))
						desc="Restore "+loot_map.get("fraction").toString()+" fraction of health";
						
					}
					break;
					case "max_health":
					{
						desc="Unknown max health increase";
						if(loot_map.containsKey("amount"))
						desc="Increase max health by "+loot_map.get("amount").toString()+"";
						else if(loot_map.containsKey("fraction"))
						desc="Increase max health by a fraction of  "+loot_map.get("fraction").toString()+"";
						
					}
					break;
					case "remove_card":
					desc="Remove a card from your deck";
					break;
					default:
				}
			} else {
				
			}
			if (loot_map.containsKey("money_price")) {
				desc+="\n$"+loot_map.get("money_price").toString();
				has_price=true;
			}
			loots.add(desc);
		}
		ArrayList<String> display_loots=new ArrayList<>(loots);
		loot_view.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, display_loots));
		loot_dialog.setView(loot_view);
		if (loots.size() > 0) {
			loot_dialog.setTitle("Select loot...");
		} else {
			loot_dialog.setTitle("No loot available...");
		}
		if (has_price) {
			loot_dialog.setMessage("You have $".concat(campaign.getOrDefault("money","0").toString()));
		} else {
			if (_max_count < 0) {
				loot_dialog.setMessage("Choose any number of items");
			} else {
				loot_dialog.setMessage("Choose up to ".concat(String.valueOf((long)(_max_count)).concat(" item(s)")));
			}
		}
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int maxHeight = (int)(dm.heightPixels * 0.75);   // tune as needed
		
		if (_max_count == 1) {
			loot_dialog.setPositiveButton("Skip loot", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_Take_loot(-1);
				}
			});
			loot_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					
				}
			});
			loot_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
					final int _position = _param3;
					if(loot_dialog_instance != null){
						loot_dialog_instance.dismiss();
						loot_dialog_instance=null;
					}
					_Take_loot(_position);
				}
			});
		} else {
			ArrayList<Integer> chosen=new ArrayList<>();//options you chose already
			loot_dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if(chosen.size()>0){
						for(int c:chosen){
							_Take_loot(c);
						}
					}
					else{
						_Take_loot(-1);
					}
				}
			});
			loot_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					
				}
			});
			loot_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
					final int _position = _param3;
					Integer p=_position;
					if(chosen.contains(p)){
						chosen.remove(p);
					}
					else{
						if(_max_count<0||_max_count>chosen.size()){
							chosen.add(p);
						}
					}
					display_loots.set(p, loots.get(p)+(chosen.contains(p)?"\nSELECTED":""));
					((BaseAdapter)loot_view.getAdapter()).notifyDataSetChanged();
					
					View custom = loot_view;
					if (custom != null) {
						if(custom.getHeight()>maxHeight){
							ViewGroup.LayoutParams lp = custom.getLayoutParams();
							lp.height = maxHeight;
							custom.setLayoutParams(lp);
						}
					}
				}
			});
			
		}
		((BaseAdapter)loot_view.getAdapter()).notifyDataSetChanged();
		loot_dialog_instance=loot_dialog.create();
		
		loot_dialog_instance.setOnShowListener(d -> {
			View custom = loot_dialog_instance.findViewById(android.R.id.custom);
			if (custom != null) {
				if(custom.getHeight()>maxHeight){
					ViewGroup.LayoutParams lp = custom.getLayoutParams();
					lp.height = maxHeight;
					custom.setLayoutParams(lp);
				}
			}
		});
		
		loot_dialog_instance.show();
		
		
	}
	
	
	public void _On_click_room(final String _id) {
		if (!(stages_generated.size() > 0)) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: no room exists, how did you click on a room??");
			return;
		}
		if (!campaign.containsKey("current_room_pos")) {
			campaign.put("current_room_pos", "0");
		}
		int curr_pos=(int)Double.parseDouble(campaign.get("current_room_pos").toString());
		if (curr_pos > stages_generated.size()) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: current room position out of bounds.");
			return;
		}
		stage_temp = stages_generated.get((int)Double.parseDouble(campaign.get("current_room_pos").toString()));
		Map<String, Integer> id_to_pos=new HashMap<>();
		int target_pos=0;
		//scan stages_generated for first matching room
		for(int pos=0;pos<stages_generated.size();pos++){
			HashMap<String,Object> room= stages_generated.get(pos);
			if(room.containsKey("id")){
				String room_id=room.get("id").toString();
				if(!id_to_pos.containsKey(room_id))
				id_to_pos.put(room_id,pos);
			}
		}
		if(!id_to_pos.containsKey(_id)){
			SketchwareUtil.showMessage(getApplicationContext(),"Error, selected room not found!!");
			return;
		}
		target_pos=id_to_pos.get(_id);
		if (target_pos == curr_pos) {
			_Continue_campaign();
		} else {
			ArrayList<String> id_list=new Gson().fromJson(stage_temp.getOrDefault("next_id","[]").toString(),new TypeToken<ArrayList<String>>(){}.getType());
			if(id_list.contains(_id)){
				//is a valid next room
				_Enter_room_pos(target_pos);
				curr_pos=(int)Double.parseDouble(campaign.get("current_room_pos").toString());
				if (target_pos == curr_pos) {
					_Continue_campaign();
				}
			}
		}
	}
	
	
	public void _Continue_campaign() {
		if (!(stages_generated.size() > 0)) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: no room exists.");
			return;
		}
		if (!campaign.containsKey("current_room_pos")) {
			campaign.put("current_room_pos", "0");
		}
		if (Double.parseDouble(campaign.get("current_room_pos").toString()) > stages_generated.size()) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: current room position out of bounds.");
			return;
		}
		stage_temp = stages_generated.get((int)Double.parseDouble(campaign.get("current_room_pos").toString()));
		if (!stage_temp.containsKey("type")) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: current room has no types.");
			return;
		}
		switch(stage_temp.get("type").toString()){
			
			case "monster": //create a stage and enter
			if (campaign.containsKey("state")) {
				if (campaign.get("state").toString().equals("completed")) {
					if (ui_mode.equals("classic")) {
						SketchwareUtil.showMessage(getApplicationContext(), "Please select the next room using the UI above");
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "Please select the next room to visit");
					}
				}
				if (campaign.get("state").toString().equals("failed")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please reset the campaign using the button on top of screen");
				}
				if (campaign.get("state").toString().equals("looting")) {
					if (stage_temp.containsKey("loots")) {
						loots_map = new Gson().fromJson(stage_temp.get("loots").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					} else {
						loots_map.clear();
					}
					_Setup_loot_select(1);
				}
				if (campaign.get("state").toString().equals("in progress")) {
					intent.setClass(getApplicationContext(), BattleActivity.class);
					intent.putExtra("campaign", campaign_name);
					startActivity(intent);
				}
				if (campaign.get("state").toString().equals("pending")) {
					battle_temp = new HashMap<>();
					if (stage_temp.containsKey("enemies")) {
						enemies_generated = new Gson().fromJson(stage_temp.get("enemies").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						enemies_generated.add((int)0, player);
						battle_temp.put("entities", new Gson().toJson(enemies_generated));
					}
					battle_temp.put("cards", new Gson().toJson(deck));
					battle_temp.put("ap", "6");
					battle_temp.put("player_pos", "0");
					battle_temp.put("game_phase", "start");
					overwrite_battle = true;
					campaign.put("battle", new Gson().toJson(battle_temp));
					campaign.put("state", "in progress");
					intent.setClass(getApplicationContext(), BattleActivity.class);
					intent.putExtra("campaign", campaign_name);
					startActivity(intent);
				}
			}
			break;
			case "loot": //looting only
			if (campaign.containsKey("state")) {
				if (campaign.get("state").toString().equals("completed")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please select the next room using the UI above");
				}
				if (campaign.get("state").toString().equals("failed")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please reset the campaign using the button below");
				}
				if (campaign.get("state").toString().equals("pending")) {
					if (stage_temp.containsKey("loots")) {
						loots_map = new Gson().fromJson(stage_temp.get("loots").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					} else {
						loots_map.clear();
					}
					if (stage_temp.containsKey("shop")) {
						_Setup_loot_select(-1);
					} else {
						_Setup_loot_select(1);
					}
				}
			}
			break;
			case "corridor": //do nothing
			if (campaign.containsKey("state")) {
				if (campaign.get("state").toString().equals("completed")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Please select the next room using the UI above");
				}
				if (campaign.get("state").toString().equals("pending")) {
					campaign.put("state", "completed");
					overwrite_battle = true;
					_Save_campaign();
				}
			}
			break;
			default:
			SketchwareUtil.showMessage(getApplicationContext(), "Error: current room has a unknown type.");
			return;
		}
		_Save_campaign();
	}
	
	
	public void _Enter_room_pos(final double _pos) {
		if (campaign.containsKey("state") && campaign.get("state").toString().equals("completed")) {
			campaign.put("current_room_pos", String.valueOf((long)(_pos)));
			campaign.put("state", "pending");
			overwrite_battle = true;
			if (campaign.containsKey("rooms_entered")) {
				campaign.put("rooms_entered", String.valueOf((long)(Double.parseDouble(campaign.get("rooms_entered").toString()) + 1)));
			} else {
				campaign.put("rooms_entered", "1");
			}
			if (campaign.containsKey("battle")) {
				HashMap<String, Object> battle_var;
				battle_var = new Gson().fromJson(campaign.get("battle").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				if (battle_var.containsKey("round_count")) {
					if (campaign.containsKey("previous_rooms_round_count")) {
						campaign.put("previous_rooms_round_count", String.valueOf((long)(Double.parseDouble(campaign.get("previous_rooms_round_count").toString()) + Double.parseDouble(battle_var.get("round_count").toString()))));
					} else {
						campaign.put("previous_rooms_round_count", battle_var.get("round_count").toString());
					}
				}
			} else {
				
			}
			//clean up player
			player.keySet().removeIf( str -> (str.endsWith("_duration")||str.equals("block")));
			campaign.remove("battle");
			_Save_campaign();
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "The current room is not finished, cannot enter next room yet");
			_Refresh_UI();
		}
	}
	
	
	public void _Setup_webview_map(final WebView _webview) {
		WebSettings s = _webview.getSettings();
		s.setJavaScriptEnabled(true);
		
		// The bridge. Note: only add this if you control the loaded content.
		_webview.addJavascriptInterface(new Object() {
			@JavascriptInterface
			public void onRoomSelected(final String id) {
				runOnUiThread(() -> {
					// Update your app's "current room" state here.
					_On_click_room(id);
					// ...save to prefs, notify adapter, whatever...
				});
			}
		}, "Android");
		
		List<Map<String, Object>> stages_temp = new ArrayList<>(stages_generated);
		String data=getMapHtml();
		_webview.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		
		_Refresh_UI();
	}
	String getMapHtml(){
		List<Map<String, Object>> stages_temp = new ArrayList<>(stages_generated);
		String data=RoomHtmlArt.mapToHtml(stages_temp,null,curr_room_id,campaign.getOrDefault("state","").toString());
		return data;
	}
	{
	}
	
	
	public ArrayList<HashMap<String, Object>> _Scale_enemy(final String _enemies, final double _level, final double _action_exponent) {
		ArrayList<HashMap<String, Object>> temp_enemies;
		temp_enemies = new Gson().fromJson(_enemies, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		String[] scale={"health","max_health","block"};
		String[] action_scale={"attack","block_self"};
		double action_level=Math.pow(_level,_action_exponent);
		for(HashMap<String,Object> enemy:temp_enemies){
			if(enemy.containsKey("nonscaling")){
				enemy.remove("nonscaling");
				continue;
			}
			//scale attributes
			for(String atr:scale){
				if(enemy.containsKey(atr)){
					double val=Double.parseDouble(enemy.get(atr).toString())*_level;
					enemy.put(atr,String.valueOf((int)val));
				}
			}
			//scale actions
			if(enemy.containsKey("action")){
				ArrayList<HashMap<String,String>>actions=new Gson().fromJson(enemy.get("action").toString(),new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType());
				for(HashMap<String,String> action:actions){
					for(String act:action_scale){
						if(action.containsKey(act)){
							double val=Double.parseDouble(action.get(act).toString())*action_level;
							action.put(act,String.valueOf((int)val));
						}
					}
				}
				enemy.put("action",new Gson().toJson(actions));
			}
		}
		return temp_enemies;
	}
	
}