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
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class BattleActivity extends Activity {
	
	private HashMap<String, Object> map_placeholder = new HashMap<>();
	private String string_placeholder = "";
	private double selected_card_pos = 0;
	private HashMap<String, Object> player = new HashMap<>();
	private HashMap<String, Object> empty_map = new HashMap<>();
	private String ui_mode = "";
	private double ap = 0;
	public Random randomizer = new Random(3);
	private boolean player_dead = false;
	private double entity_action_pos = 0;
	private HashMap<String, Object> apply_card_map = new HashMap<>();
	private double int_placeholder = 0;
	private HashMap<String, Object> battle_temp = new HashMap<>();
	private HashMap<String, Object> campaign_temp = new HashMap<>();
	private String campaign_name = "";
	private boolean campaign = false;
	private boolean player_win = false;
	private String game_phase = "";
	private String dialog_result = "";
	private HashMap<String, Object> card_temp = new HashMap<>();
	private HashMap<String, Object> modifiers_temp = new HashMap<>();
	private SketchwareMewEnvironment mewenvironment = new SketchwareMewEnvironment();
	private double round_count = 0;
	private HashMap<String, Object> durations_map = new HashMap<>();
	private double fatigue_level = 0;
	
	private ArrayList<HashMap<String, Object>> entity_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> hand_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> actions_placeholder = new ArrayList<>();
	private ArrayList<String> Logs = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> stack_target_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> stack_choices_list = new ArrayList<>();
	private ArrayList<Double> hand_id_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> card_list = new ArrayList<>();
	private ArrayList<Double> library_id_list = new ArrayList<>();
	private ArrayList<Double> discard_id_list = new ArrayList<>();
	private ArrayList<Double> stack_id_list = new ArrayList<>();
	private ArrayList<Double> field_id_list = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private ListView entity_view;
	private LinearLayout linear2;
	private TextView card_desc;
	private ListView card_view;
	private TextView library_label;
	private TextView discard_label;
	private TextView action_point_text;
	private Button next_turn_btn;
	
	private Calendar calendar = Calendar.getInstance();
	private AlertDialog.Builder log_dialog;
	private SharedPreferences battle_filesaver;
	private SharedPreferences campaign_filesaver;
	private SharedPreferences cards_filesaver;
	private AlertDialog.Builder script_input;
	private AlertDialog.Builder choice_dialog;
	private AlertDialog.Builder card_zone_dialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.battle);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear3 = findViewById(R.id.linear3);
		entity_view = findViewById(R.id.entity_view);
		linear2 = findViewById(R.id.linear2);
		card_desc = findViewById(R.id.card_desc);
		card_view = findViewById(R.id.card_view);
		library_label = findViewById(R.id.library_label);
		discard_label = findViewById(R.id.discard_label);
		action_point_text = findViewById(R.id.action_point_text);
		next_turn_btn = findViewById(R.id.next_turn_btn);
		log_dialog = new AlertDialog.Builder(this);
		battle_filesaver = getSharedPreferences("battle", Activity.MODE_PRIVATE);
		campaign_filesaver = getSharedPreferences("campaign", Activity.MODE_PRIVATE);
		cards_filesaver = getSharedPreferences("cards", Activity.MODE_PRIVATE);
		script_input = new AlertDialog.Builder(this);
		choice_dialog = new AlertDialog.Builder(this);
		card_zone_dialog = new AlertDialog.Builder(this);
		
		entity_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (ui_mode.equals("target")) {
					map_placeholder = entity_list.get((int)_position);
					_Try_put_id_on_stack(hand_id_list.get((int)(selected_card_pos)).doubleValue(), map_placeholder);
					if (ui_mode.equals("target")) {
						selected_card_pos = -1;
						ui_mode = "";
					}
				}
				_Refresh_views();
			}
		});
		
		card_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (ui_mode.equals("") || (ui_mode.equals("target") || ui_mode.equals(""))) {
					if (_position == selected_card_pos) {
						selected_card_pos = -1;
						ui_mode = "";
					} else {
						selected_card_pos = _position;
						ui_mode = "target";
					}
				}
				_Refresh_views();
			}
		});
		
		library_label.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				_Generate_script();
				return true;
			}
		});
		
		library_label.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!campaign) {
					final EditText editttext1= new EditText(BattleActivity.this);
					
					LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					
					editttext1.setLines(6);
					editttext1.setLayoutParams(lpar);
					editttext1.setText("ADD num 3 30;\nADD numm 300 num;\nASSIGN_STRING holy  is a holy number.;\nTO_STRING var numm;\nCONCAT holy var holy;\nTOAST holy;");
					script_input.setView(editttext1);
					script_input.setTitle("Script runner");
					script_input.setMessage("Enter some script to try out here. Notice the actor is the player, while target and card are set to empty map for now.");
					script_input.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							dialog_result=editttext1.getText().toString();
							HashMap<String, Object> empty1=new HashMap<>();
							HashMap<String, Object> empty2=new HashMap<>();
							HashMap<String, Object> empty3=new HashMap<>();
							_Run_script(player, empty1, empty2, dialog_result, empty3);
							_Check_game_state();
							_Refresh_views();
						}
					});
					script_input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					script_input.create().show();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "This way of running scripts is disabled in campaign mode");
				}
			}
		});
		
		discard_label.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ArrayList<HashMap<String, Object>> discard_cards=new ArrayList<>();
				_id_to_cards(discard_id_list, discard_cards);
				String result="";
				for(HashMap<String,Object> d:discard_cards){
					result+=d.getOrDefault("name","Unknown card").toString()+"\n";
				}
				if(result.equals(""))result="(No cards)";
				card_zone_dialog.setTitle("Cards in discard");
				card_zone_dialog.setMessage(result);
				card_zone_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				card_zone_dialog.create().show();
			}
		});
		
		action_point_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ArrayList<HashMap<String, Object>> field_cards=new ArrayList<>();
				_id_to_cards(field_id_list, field_cards);
				String result="";
				for(HashMap<String,Object> d:field_cards){
					result+=d.getOrDefault("name","Unknown card").toString();
					result+="("+d.getOrDefault("remaining_field_time","0").toString()+")";
					result+="\n";
				}
				if(result.equals(""))result="(No cards)";
				card_zone_dialog.setTitle("Cards on field");
				card_zone_dialog.setMessage(result);
				card_zone_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				card_zone_dialog.create().show();
			}
		});
		
		next_turn_btn.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				log_dialog.setTitle("Game logs");
				string_placeholder="";
				for(String log:Logs){
					string_placeholder += "\n"+log;
				}
				log_dialog.setMessage(string_placeholder);
				log_dialog.setPositiveButton("Save game", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						_Save_named("Placeholder save name", false);
					}
				});
				log_dialog.setNegativeButton("Load game", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						if (campaign) {
							SketchwareUtil.showMessage(getApplicationContext(), "Can't load other saves when in campaign mode");
						} else {
							_Load_named("Placeholder save name", false);
						}
					}
				});
				log_dialog.create().show();
				return true;
			}
		});
		
		next_turn_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (ui_mode.equals("end battle")) {
					finish();
				} else {
					_Try_resolve_stacktop(true);
				}
			}
		});
	}
	
	private void initializeLogic() {
		durations_map = new Gson().fromJson("{\"vulnerable_duration\":\"🤕\",\"block_duration\":\"🏰\",\"weak_duration\":\"😫\",\"strength_duration\":\"💪\",\"stunned_duration\":\"😵‍💫\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		entity_view.setAdapter(new Entity_viewAdapter(entity_list));
		entity_view.setScrollbarFadingEnabled(false);
		card_view.setAdapter(new Card_viewAdapter(hand_list));
		selected_card_pos = -3;
		if (getIntent().hasExtra("campaign")) {
			campaign = true;
			campaign_name = getIntent().getStringExtra("campaign");
			_Load_named(campaign_name, true);
		} else {
			_Default_battle();
		}
		_Refresh_views();
		_Log("Game init complete");
		
		/*ArrayList<Integer> numarr=new ArrayList<Integer>();
ArrayList arr=numarr;
arr.add("3 is a holy number.");
Integer unholy=numarr.get(0)+3;*/
	}
	
	public void _Refresh_views() {
		_id_to_cards(hand_id_list, hand_list);
		((BaseAdapter)entity_view.getAdapter()).notifyDataSetChanged();
		((BaseAdapter)card_view.getAdapter()).notifyDataSetChanged();
		if ((stack_id_list.size() == 0) && ui_mode.equals("stack")) {
			ui_mode = "";
		}
		if ((stack_id_list.size() > 0) && (ui_mode.equals("") || ui_mode.equals("target"))) {
			ui_mode = "stack";
		}
		if ((selected_card_pos > -1) && (selected_card_pos < hand_list.size())) {
			map_placeholder = hand_list.get((int)selected_card_pos);
			if (map_placeholder.containsKey("name")) {
				string_placeholder = map_placeholder.get("name").toString();
			} else {
				string_placeholder = "Unnamed card";
			}
			if (map_placeholder.containsKey("final_cost")) {
				string_placeholder = string_placeholder.concat("(".concat(map_placeholder.get("final_cost").toString().concat(")")));
			} else {
				
			}
			string_placeholder = string_placeholder.concat("\n");
			if (map_placeholder.containsKey("desc")) {
				string_placeholder = string_placeholder.concat(map_placeholder.get("desc").toString());
			} else {
				string_placeholder = string_placeholder.concat("(No description)");
			}
			card_desc.setText(string_placeholder);
		} else {
			card_desc.setText("Select a card for more info...");
		}
		action_point_text.setText("Action point:".concat(String.valueOf((long)(ap))));
		library_label.setText("Library size:".concat(String.valueOf((long)(library_id_list.size()))));
		discard_label.setText("Discard size:".concat(String.valueOf((long)(discard_id_list.size()))));
		next_turn_btn.setText("Unknown action");
		if (ui_mode.equals("target") || ui_mode.equals("")) {
			next_turn_btn.setText("Next turn");
		}
		if (ui_mode.equals("end battle")) {
			next_turn_btn.setText("End battle");
		}
		if (ui_mode.equals("stack")) {
			next_turn_btn.setText("Resolve stack(".concat(String.valueOf((long)(stack_id_list.size())).concat(")")));
		}
	}
	
	
	public void _Attack(final HashMap<String, Object> _attacker, final HashMap<String, Object> _defender, final double _amount, final HashMap<String, Object> _modifier) {
		_Attack_generic(_attacker, _defender, _amount, _modifier);
	}
	
	void _Attack_generic(Map<String,Object> _attacker, Map<String,Object> _defender, double _amount, Map<String,Object> _modifier){
		// count number of attacks
		int attack_count= (int)(_modifier.containsKey("count")? Double.parseDouble(_modifier.get("count").toString()) : 1d);
		
		// log the attack
		String attacker_name= (_attacker.containsKey("name")? _attacker.get("name").toString() : "Some entity");
		String defender_name= (_defender.containsKey("name")? _defender.get("name").toString() : "Some entity");
		_Log(attacker_name+" wants to attack "+defender_name+" for "+String.valueOf(_amount)+" for "+String.valueOf(attack_count)+" time(s)");
		for(int i=0; i<attack_count; i++){
			if (_defender.containsKey("health")) {
				double final_amount=_amount;
				if (_attacker.containsKey("weak_duration")) {
					final_amount*=0.7;
				}
				if (_attacker.containsKey("strength_duration")) {
					final_amount*=1.4;
				}
				if (_defender.containsKey("vulnerable_duration")) {
					final_amount*=1.4;
				}
				if (_defender.containsKey("block")) {
					if (final_amount < Double.parseDouble(_defender.get("block").toString())) {
						_defender.put("damage_blocked",String.valueOf((int)(Double.parseDouble(_defender.getOrDefault("damage_blocked","0").toString())+final_amount)));
						_defender.put("block", String.valueOf((long)(Double.parseDouble(_defender.get("block").toString()) - final_amount)));
					} else {
						_defender.put("damage_blocked",String.valueOf((int)(Double.parseDouble(_defender.getOrDefault("damage_blocked","0").toString())+Double.parseDouble(_defender.getOrDefault("block","0").toString()))));
						_defender.put("health", String.valueOf((long)(Double.parseDouble(_defender.get("block").toString()) + (Double.parseDouble(_defender.get("health").toString()) - final_amount))));
						_defender.remove("block");
					}
				} else {
					_defender.put("health", String.valueOf((long)(Double.parseDouble(_defender.get("health").toString()) - final_amount)));
				}
				//inflicts effect after damage
				for(String duration:durations_map.keySet()){
					if (_modifier.containsKey(duration)) {
						_Inflict_effect_generic(_defender,duration,String.valueOf(_modifier.get(duration)));
					} else {
						
					}
				}
			}
		}
	}
	
	
	public void _Draw_card() {
		if (library_id_list.size() > 0) {
			Double card=library_id_list.get(0);
			_Draw_card(card);
		} else {
			if (discard_id_list.size() > 0) {
				fatigue_level++;
				if (player.containsKey("fatigue_multiplier")) {
					player.put("health", String.valueOf((long)(Double.parseDouble(player.get("health").toString()) - (fatigue_level * Double.parseDouble(player.get("fatigue_multiplier").toString())))));
				}
				_Shuffle_into_generic(discard_id_list,library_id_list);
				Double card=library_id_list.get(0);
				_Draw_card(card);
			}
		}
	}
	void _Draw_card(double id){
		if(library_id_list.contains(id)){
			library_id_list.remove(id);
			hand_id_list.add(id);
		}
		else{
			_Log("card with id "+String.valueOf(id)+" not in library when _Draw_card is called!");
		}
	}
	void _Move_card(Double _card_id, List<Double> _target_list){
		library_id_list.remove(_card_id);
		hand_id_list.remove(_card_id);
		discard_id_list.remove(_card_id);
		stack_id_list.remove(_card_id);
		_target_list.add(Double.valueOf(_card_id));
	}
	void _Move_card(double card_id, List<Double> target_list, List<Double> source){
		if(card_id<0||card_id>=card_list.size()){
			_Log("Attempting to move nonexistent card ids, aborted.");
			return;
		}
		if(source==null){
			_Move_card(card_id, target_list);
		}
		else{
			if(source.contains(card_id)){
				source.remove(card_id);
				target_list.add(card_id);
			}
			else{
				_Log("Warning: _Move_card target card id not in right zone to start with!");
			}
		}
	}
	{
	}
	
	
	public void _Delete_by_object_equal(final HashMap<String, Object> _map, final ArrayList<HashMap<String, Object>> _list) {
		_list.removeIf( p -> (p==_map));
	}
	
	
	public void _End_turn() {
		_Refresh_views();
		_Start_turn();
	}
	
	
	public void _Start_turn() {
		round_count++;
		entity_action_pos = -1;
		_Pass_action();
	}
	
	
	public void _Shuffle_into(final ArrayList<HashMap<String, Object>> _source, final ArrayList<HashMap<String, Object>> _target) {
		//clears target, put its contents into source then shuffle source
		if(_source != _target){
			_target.addAll(_source);
			_source.clear();
		}
		
		Collections.shuffle(_target, randomizer);
	}
	public <T> void _Shuffle_into_generic(ArrayList<T>_source, ArrayList<T>_target){
		//clears target, put its contents into source then shuffle source
		if(_source != _target){
			_target.addAll(_source);
			_source.clear();
		}
		
		Collections.shuffle(_target, randomizer);
	}
	{
	}
	
	
	public void _Check_any_death() {
		ArrayList<HashMap<String, Object>> entity_snapshot= new ArrayList<>(entity_list);
		for(HashMap<String, Object> entity: entity_snapshot){
			if(_is_dead(entity)){
				_Delete_by_object_equal(entity, entity_list);
				if(entity==player) {player_dead=true;
					_Log("Player died.");}
				else{
					_Log("Something died.");}
			}
		}
		if (campaign && (!player_dead && _Check_win())) {
			player_win = true;
			_Save_named(campaign_name, true);
			ui_mode = "end battle";
		}
		if (campaign && player_dead) {
			_Save_named(campaign_name, true);
			ui_mode = "end battle";
		}
	}
	
	
	public void _Take_action(final HashMap<String, Object> _entity) {
		//general actions
		boolean stunned=_entity.containsKey("stunned_duration");
		if (_entity.containsKey("block")) {
			if (!_entity.containsKey("block_duration")) {
				_entity.remove("block");
			} else {
				
			}
		}
		if(_entity==player){
			
			_Log("Player starting turn ".concat(String.valueOf((long)(round_count)).concat("...")));
			if (stunned) {
				//Discard hand when stunned
				ArrayList<Double> hand_snapshot= new ArrayList<>(hand_id_list);
				for(int i=0;i<hand_snapshot.size();i++){
					_Discard_by_id(hand_snapshot.get(i), false);
				}
				ap = 0;
			} else {
				//Draw cards for starting turn
				double sths=player.containsKey("start_turn_hand_size")?Double.parseDouble(player.get("start_turn_hand_size").toString()):5d;
				double stmd=player.containsKey("start_turn_max_draw")?Double.parseDouble(player.get("start_turn_max_draw").toString()):5d;
				
				double draw=sths-hand_id_list.size();
				if(draw>stmd)draw=stmd;
				
				for(int i=0;i<draw;i++) _Draw_card();
				ap = 6;
			}
			_Apply_field_cards_turn_start();
			if(_entity.containsKey("turn_start_script")){
				String language=_entity.getOrDefault("script_language","").toString();
				_Run_script_type(_entity, new HashMap<String, Object>(), new HashMap<String, Object>(), _entity.get("turn_start_script").toString(), language, new HashMap<String,Object>());
			}
			_Check_game_state();
			_Refresh_views();
			if (campaign) {
				_Save_named(campaign_name, true);
			}
		}
		else{
			if(_entity.containsKey("turn_start_script")){
				String language=_entity.getOrDefault("script_language","").toString();
				_Run_script_type(_entity, new HashMap<String, Object>(), new HashMap<String, Object>(), _entity.get("turn_start_script").toString(), language, new HashMap<String,Object>());
			}
			HashMap<String, Object> action_placeholder=_get_upcoming_action(_entity);
			if (stunned) {
				//update action pos
				if(_entity.containsKey("action_pos")){
					_entity.put("action_pos","0");
				}
			} else {
				for (Map.Entry<String, ?> action : action_placeholder.entrySet()) {
					String entity_name= (_entity.containsKey("name")? _entity.get("name").toString() : "Some entity");
					_Log(entity_name+" wants to take action "+action.toString());
					String val=action.getValue().toString();
					int num_val=(int)Double.parseDouble(val);
					switch(action.getKey()){
						case "attack":
						{
							for(HashMap<String, Object> tar: _get_targets(_entity,"enemy")){
								_Attack(_entity,tar,num_val, new HashMap<String,Object>());
							}
							break;
						}
						case "block_self":
						{
							for(HashMap<String, Object> tar: _get_targets(_entity,"self")){
								_Apply_block(_entity,tar,num_val, new HashMap<String,Object>());
							}
							break;
						}
						case "block_ally":
						{
							for(HashMap<String, Object> tar: _get_targets(_entity,"ally")){
								_Apply_block(_entity,tar,num_val, new HashMap<String,Object>());
							}
							break;
						}
						//add more below
					}
				}
				//update action pos
				if(_entity.containsKey("action_pos")){
					int new_action_pos=Integer.parseInt(_entity.get("action_pos").toString())+1;
					if(new_action_pos>=actions_placeholder.size()){
						_entity.put("action_pos","0");
					}
					else{
						_entity.put("action_pos",String.valueOf(new_action_pos));
					}
				}
			}
			_Pass_action();
		}
		
	}
	
	
	public void _Pass_action() {
		if (!(entity_action_pos < 0) && (entity_action_pos < entity_list.size())) {
			HashMap<String, Object> _entity;
			_entity = entity_list.get((int)entity_action_pos);
			for(String duration:durations_map.keySet()){
				if (_entity.containsKey(duration)) {
					if (Double.parseDouble(_entity.get(duration).toString()) < 2) {
						_entity.remove(duration);
					} else {
						_entity.put(duration, String.valueOf((long)(Double.parseDouble(_entity.get(duration).toString()) - 1)));
					}
				} else {
					
				}
			}
		}
		_Check_game_state();
		entity_action_pos++;
		if (entity_action_pos < entity_list.size()) {
			map_placeholder = entity_list.get((int)entity_action_pos);
			if(!_is_dead(map_placeholder) || player==map_placeholder){
				_Take_action(map_placeholder);
			}else{
				_Pass_action();
			}
		} else {
			if (entity_list.size() > 0) {
				if (player_dead) {
					SketchwareUtil.showMessage(getApplicationContext(), "Warning: player dead");
					_Refresh_views();
				} else {
					_End_turn();
				}
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Warning: no entities availble when Pass_action is called");
				_Refresh_views();
			}
		}
	}
	
	
	public void _Player_pass_action() {
		_Apply_field_cards_turn_end();
		//discard hand to end turn hand size
		double et_hand_size=player.containsKey("end_turn_hand_size")?Double.parseDouble(player.get("end_turn_hand_size").toString()):0d;
		
		ArrayList<Double> hand_snapshot= new ArrayList<>(hand_id_list);
		for(int i=0;i<hand_snapshot.size()-et_hand_size;i++){
			_Discard_by_id(hand_snapshot.get(i), false);
		}
		//cost change for turn expires
		//field time gets removed
		for(HashMap<String,Object> card:card_list){
			card.remove("cost_change_for_turn");
		}
		_Pass_action();
	}
	
	
	public boolean _is_dead(final HashMap<String, Object> _entity) {
		return _entity.containsKey("health") && Double.parseDouble(_entity.get("health").toString())<=0;
	}
	
	
	public HashMap<String, Object> _get_upcoming_action(final HashMap<String, Object> _entity) {
		if (_entity.containsKey("action")) {
			if (!_entity.containsKey("action_pos")) {
				_entity.put("action_pos", "0");
			}
			actions_placeholder = new Gson().fromJson(_entity.get("action").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			if (actions_placeholder.size() > 0) {
				int action_pos=Integer.parseInt(_entity.get("action_pos").toString());
				if(action_pos>=actions_placeholder.size()){
					action_pos=0;
					_entity.put("action_pos","0");
				}
				return actions_placeholder.get(action_pos);
			} else {
				return new HashMap<String, Object>();
			}
		} else {
			return new HashMap<String, Object>();
		}
	}
	
	
	public void _Log(final String _info) {
		calendar = Calendar.getInstance();
		Logs.add(new SimpleDateFormat("[HH:mm:ss]").format(calendar.getTime()).concat(_info));
	}
	
	
	public ArrayList<HashMap<String, Object>> _get_targets(final HashMap<String, Object> _entity, final String _mode) {
		String group=(_entity.containsKey("group")?_entity.get("group").toString():"No group");
		ArrayList<HashMap<String, Object>> targets=new ArrayList<>();
		for(HashMap<String, Object> tar:entity_list){
			String tar_group=(tar.containsKey("group")?tar.get("group").toString():null);
			boolean is_self=(tar==_entity);
			boolean is_ally=is_self ||(group.equals(tar_group));
			switch(_mode){
				case "self":
				if(is_self) targets.add(tar);
				break;
				case "ally":
				if(is_ally) targets.add(tar);
				break;
				case "enemy":
				if(!is_ally) targets.add(tar);
				break;
				
			}
		}
		ArrayList<HashMap<String, Object>> real_targets=new ArrayList<>();
		for(HashMap<String, Object> tar: targets){
			boolean found=false;
			for(HashMap<String, Object> tar2: real_targets){
				if(tar==tar2){
					found=true;break;
				}
			}
			if(!found)real_targets.add(tar);
		}
		return real_targets;
	}
	
	
	public boolean _isInt(final String _input) {
		return (_input.matches("-?[0123456789]{1,9}"));
	}
	
	
	public void _Apply_block(final HashMap<String, Object> _source, final HashMap<String, Object> _target, final double _amount, final HashMap<String, Object> _modifiers) {
		_Apply_block_generic(_source, _target, _amount, _modifiers);
	}
	void _Apply_block_generic(Map<String,Object> _source, Map<String,Object> _target, double _amount, Map<String,Object> _modifiers){
		if (_target.containsKey("health")) {
			double new_block;
			double block=0;
			if (_target.containsKey("block")) {
				block = Double.parseDouble(_target.get("block").toString());
				new_block = block + _amount;
			} else {
				new_block = _amount;
			}
			if (_target.containsKey("max_health")) {
				if (new_block > Double.parseDouble(_target.get("max_health").toString())) {
					double max_health=Double.parseDouble(_target.get("max_health").toString());
					new_block=(max_health>block?max_health:block);
				}
			}
			_target.put("block", String.valueOf((long)(new_block)));
			if (_modifiers.containsKey("block_duration")) {
				if (!_target.containsKey("block_duration") || (Double.parseDouble(_modifiers.get("block_duration").toString()) > Double.parseDouble(_target.get("block_duration").toString()))) {
					_target.put("block_duration", _modifiers.get("block_duration").toString());
				}
			} else {
				
			}
		}
	}
	
	
	public void _Save_named(final String _name, final boolean _campaign) {
		if (stack_id_list.size() > 0) {
			SketchwareUtil.showMessage(getApplicationContext(), "Saving with nonempty stack is not supported currently :(");
			return;
		}
		battle_temp = new HashMap<>();
		battle_temp.put("entities", new Gson().toJson(entity_list));
		battle_temp.put("cards", new Gson().toJson(card_list));
		battle_temp.put("hand", _Numlist_to_json(hand_id_list));
		battle_temp.put("discard", _Numlist_to_json(discard_id_list));
		battle_temp.put("library", _Numlist_to_json(library_id_list));
		battle_temp.put("field", _Numlist_to_json(field_id_list));
		//logs not saved :(
		if (!game_phase.equals("")) {
			battle_temp.put("game_phase", game_phase);
		}
		battle_temp.put("ap", String.valueOf((long)(ap)));
		battle_temp.put("round_count", String.valueOf((long)(round_count)));
		battle_temp.put("fatigue_level", String.valueOf((long)(fatigue_level)));
		battle_temp.put("entity_action_pos", String.valueOf((long)(entity_action_pos)));
		battle_temp.put("player", new Gson().toJson(player));
		for(int_placeholder=0;int_placeholder<entity_list.size();int_placeholder++){
			if(entity_list.get((int)int_placeholder)==player){
				break;
			}
		}
		if (int_placeholder < entity_list.size()) {
			battle_temp.put("player_pos", String.valueOf((long)(int_placeholder)));
		} else {
			battle_temp.put("player_pos", "-1");
		}
		if (_campaign) {
			if (campaign_filesaver.contains(_name)) {
				campaign_temp = new Gson().fromJson(campaign_filesaver.getString(_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
				if (player_win) {
					campaign_temp.put("battle", new Gson().toJson(battle_temp));
					campaign_temp.put("player", new Gson().toJson(player));
					campaign_temp.put("state", "looting");
					campaign_filesaver.edit().putString(_name, new Gson().toJson(campaign_temp)).commit();
					SketchwareUtil.showMessage(getApplicationContext(), "Campaign win saved");
				} else {
					if (player_dead) {
						campaign_temp.put("battle", new Gson().toJson(battle_temp));
						campaign_temp.put("player", new Gson().toJson(player));
						campaign_temp.put("state", "failed");
						campaign_filesaver.edit().putString(_name, new Gson().toJson(campaign_temp)).commit();
						SketchwareUtil.showMessage(getApplicationContext(), "Campaign defeat saved");
					} else {
						campaign_temp.put("battle", new Gson().toJson(battle_temp));
						campaign_temp.put("player", new Gson().toJson(player));
						campaign_temp.put("state", "in progress");
						campaign_filesaver.edit().putString(_name, new Gson().toJson(campaign_temp)).commit();
						_Log("Campaign battle saved");
					}
				}
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error, no campaign save files named ".concat(_name.concat("!!")));
				return;
			}
		} else {
			if (battle_filesaver.contains(_name)) {
				battle_filesaver.edit().putString(_name, new Gson().toJson(battle_temp)).commit();
				SketchwareUtil.showMessage(getApplicationContext(), "Game saved as ".concat(_name.concat(" (old save overwritten)")));
			} else {
				battle_filesaver.edit().putString(_name, new Gson().toJson(battle_temp)).commit();
				SketchwareUtil.showMessage(getApplicationContext(), "Game saved as ".concat(_name));
			}
		}
	}
	
	
	public void _Load_named(final String _name, final boolean _campaign) {
		if (_campaign) {
			if (campaign_filesaver.contains(_name)) {
				campaign_temp = new Gson().fromJson(campaign_filesaver.getString(_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
				if (campaign_temp.containsKey("battle")) {
					battle_temp = new Gson().fromJson(campaign_temp.get("battle").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error, campaign has no battle active!".concat(""));
					return;
				}
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error, no campaign save files named ".concat(_name.concat("!!")));
				return;
			}
		} else {
			if (battle_filesaver.contains(_name)) {
				battle_temp = new Gson().fromJson(battle_filesaver.getString(_name, ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error, no save files named ".concat(_name.concat("!!")));
				return;
			}
		}
		if (battle_temp.containsKey("entities")) {
			entity_list = new Gson().fromJson(battle_temp.get("entities").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		} else {
			entity_list.clear();
		}
		if (battle_temp.containsKey("cards")) {
			card_list = new Gson().fromJson(battle_temp.get("cards").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		} else {
			card_list.clear();
		}
		if (battle_temp.containsKey("hand")) {
			_JSON_numlist(battle_temp.get("hand").toString(), hand_id_list);
		} else {
			hand_id_list.clear();
		}
		if (battle_temp.containsKey("discard")) {
			_JSON_numlist(battle_temp.get("discard").toString(), discard_id_list);
		} else {
			discard_id_list.clear();
		}
		if (battle_temp.containsKey("library")) {
			_JSON_numlist(battle_temp.get("library").toString(), library_id_list);
		} else {
			library_id_list.clear();
		}
		if (battle_temp.containsKey("field")) {
			_JSON_numlist(battle_temp.get("field").toString(), field_id_list);
		} else {
			field_id_list.clear();
		}
		if (battle_temp.containsKey("game_phase")) {
			game_phase = battle_temp.get("game_phase").toString();
		} else {
			game_phase = "";
		}
		if (battle_temp.containsKey("ap")) {
			ap = Integer.parseInt(battle_temp.get("ap").toString());
		} else {
			ap = 0;
		}
		if (battle_temp.containsKey("round_count")) {
			round_count = Integer.parseInt(battle_temp.get("round_count").toString());
		} else {
			round_count = 0;
		}
		if (battle_temp.containsKey("fatigue_level")) {
			fatigue_level = Integer.parseInt(battle_temp.get("fatigue_level").toString());
		} else {
			fatigue_level = 0;
		}
		if (battle_temp.containsKey("entity_action_pos")) {
			entity_action_pos = Integer.parseInt(battle_temp.get("entity_action_pos").toString());
		}
		if (battle_temp.containsKey("player_pos") && (Integer.parseInt(battle_temp.get("player_pos").toString()) > -1)) {
			player = entity_list.get((int)Integer.parseInt(battle_temp.get("player_pos").toString()));
			player_dead = false;
		} else {
			if (battle_temp.containsKey("player")) {
				player = new Gson().fromJson(battle_temp.get("player").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
			} else {
				player = new HashMap<>();
			}
			player_dead = true;
		}
		stack_id_list.clear();
		_Update_cards();
		_Log("Game loaded.");
		entity_view.setAdapter(new Entity_viewAdapter(entity_list));
		entity_view.setScrollbarFadingEnabled(false);
		card_view.setAdapter(new Card_viewAdapter(hand_list));
		_Refresh_views();
		SketchwareUtil.showMessage(getApplicationContext(), "Game loaded");
		_Check_start_phase();
	}
	
	
	public void _Default_battle() {
		selected_card_pos = -1;
		ap = 6;
		empty_map = new HashMap<>();
		player = new Gson().fromJson("{\"name\":\"Player\",\"group\":\"player\",\"health\":\"150\",\"max_health\":\"150\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		entity_list.add(player);
		map_placeholder = new Gson().fromJson("{\"name\":\"Enemy 1\",\"group\":\"enemy\",\"health\":\"100\",\"max_health\":\"100\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		map_placeholder.put("action", "[{\"attack\":\"7\"},{\"attack\":\"11\"}]");
		entity_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Enemy 2\",\"group\":\"enemy\",\"health\":\"100\",\"max_health\":\"100\",\"block\":\"29\",\"block_duration\":\"3\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		map_placeholder.put("action", "[{\"attack\":\"7\"},{\"attack\":\"7\",\"block_self\":\"7\"},{\"block_self\":\"13\"}]");
		entity_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Random idle enemy\",\"group\":\"enemy\",\"health\":\"333\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		entity_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Holy 3\",\"cost\":\"3\",\"desc\":\"3 is a holy number.\",\"damage\":\"3\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Tribute to a bird\",\"cost\":\"788\", \"desc\":\"Give me 788\",\"damage\":\"788\",\"target\":\"self\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Script runner\",\"cost\":\"0\", \"desc\":\"Run script in a textbox. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a script\\\",\\\"choice_name\\\":\\\"script\\\",\\\"type\\\":\\\"text\\\",\\\"num_lines\\\":\\\"6\\\"}]\",\"run_text_choice\":\"1\",\"on_play_destination\":\"hand\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Card conjurer\",\"cost\":\"0\", \"desc\":\"Conjure a named card to hand. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a card name\\\",\\\"choice_name\\\":\\\"card_name\\\",\\\"type\\\":\\\"text\\\"}]\",\"add_text_choice_to_hand\":\"1\",\"on_play_destination\":\"hand\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Script runner 2\",\"cost\":\"0\", \"desc\":\"Run Kyrre's script in a textbox. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a script\\\",\\\"choice_name\\\":\\\"script\\\",\\\"type\\\":\\\"text\\\",\\\"num_lines\\\":\\\"6\\\"}]\",\"run_text_choice\":\"1\",\"script_language\":\"mewtype\",\"on_play_destination\":\"hand\",\"tag\":\"{rarity=debug}\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Card selecter 2\",\"cost\":\"0\", \"desc\":\"Select some cards from your hand and toast some stuff based on their rarity. Return this card to your hand.\",\"choices\":\"[{\\\"desc\\\":\\\"Choose a card\\\",\\\"choice_name\\\":\\\"card_choice\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"hand\\\",\\\"max_choice\\\":\\\"1\\\"}]\",\"on_play_script\":\"\nMAP_FROM_STRING card_choice card_choice;\nMAP_UNPACK card_choice;\nNUM_LIST_FROM_STRING idlist chosen_cards_id;\nLIST_LENGTH l idlist;\nLOG_ENVIRONMENT;\nIF EQ l 0;\nASSIGN_STRING zero 0;\nTOAST zero;\nELSE;\nLIST_GET id idlist 0 DOUBLE;\nLIST_GET card CARDS id MAP;\nASSIGN_STRING tag tag;\nMAP_GET cardtag card tag;\nMAP_FROM_STRING tagmap cardtag;\nASSIGN_STRING rarity rarity;\nASSIGN_STRING debug debug;\nMAP_GET result tagmap rarity;\nIF EQ result debug;\nASSIGN_STRING ten 10;\nTOAST ten;\nELSE;\nASSIGN_STRING five 5;\nTOAST five;\nEND_IF;\nEND_IF;\",\"on_play_destination\":\"hand\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Card peeker\",\"cost\":\"0\", \"desc\":\"Look at top 4 cards of your library. Return this card to your hand.\",\"choices\":\"[{\\\"desc\\\":\\\"Look at the top 4 cards of your library...\\\",\\\"choice_name\\\":\\\"card_choice\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"library\\\",\\\"source_count\\\":\\\"4\\\",\\\"max_choice\\\":\\\"0\\\"}]\",\"on_play_destination\":\"hand\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Card selecter\",\"cost\":\"0\", \"desc\":\"Select some cards from your hand and discard them. Return this card to your hand.\",\"choices\":\"[{\\\"desc\\\":\\\"Choose a card\\\",\\\"choice_name\\\":\\\"card_in_hand\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"hand\\\"}]\",\"discard_card_choice\":\"1\",\"on_play_destination\":\"hand\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Name tag\",\"cost\":\"2\",\"desc\":\"Rename an entity to a string of your choice\", \"on_play_script\":\"ASSIGN_STRING name name;\nMAP_PUT TARGET name entity_name;\", \"choices\":\"[{\\\"desc\\\":\\\"Enter the new name\\\",\\\"choice_name\\\":\\\"entity_name\\\",\\\"type\\\":\\\"text\\\"}]\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Fancy strike\",\"cost\":\"2\",\"desc\":\"Deal 10 damage to an enemy by script\", \"on_play_script\":\"ATTACK 10\", \"target\":\"enemy\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		map_placeholder = new Gson().fromJson("{\"name\":\"Toast echo\",\"cost\":\"0\", \"desc\":\"Toasts your text input\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a message\\\",\\\"choice_name\\\":\\\"message\\\",\\\"type\\\":\\\"text\\\"}]\",\"on_play_script\":\"TOAST message\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
		card_list.add(map_placeholder);
		for(int _repeat30 = 0; _repeat30 < (int)(1); _repeat30++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Cleave\",\"cost\":\"2\",\"desc\":\"Deal 12 damage to all enemy\", \"damage_enemies\":\"12\", \"target\":\"enemy\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat33 = 0; _repeat33 < (int)(1); _repeat33++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"This card has no cost and can't be played\",\"desc\":\"This card is here to annoy you\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat36 = 0; _repeat36 < (int)(1); _repeat36++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Adrenaline\",\"desc\":\"Gain 1 AP and draw 2 cards. Exhaust.\",\"give_card\":\"2\",\"give_ap\":\"1\",\"on_play_destination\":\"exhaust\",\"cost\":\"0\",\"target\":\"self\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat39 = 0; _repeat39 < (int)(1); _repeat39++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Energy surge\",\"desc\":\"Gain 1 AP \",\"give_ap\":\"1\",\"cost\":\"0\",\"target\":\"self\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat42 = 0; _repeat42 < (int)(1); _repeat42++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Useless card in library\",\"desc\":\"This card is for testing library functionality\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat45 = 0; _repeat45 < (int)(2); _repeat45++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Useless card in library that you can get rid of\",\"desc\":\"This card is for testing exhaust functionality\",\"cost\":\"3\",\"on_play_destination\":\"exhaust\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat48 = 0; _repeat48 < (int)(6); _repeat48++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Strike\",\"cost\":\"2\",\"desc\":\"Deal 10 damage to an enemy\", \"damage\":\"10\", \"target\":\"enemy\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat51 = 0; _repeat51 < (int)(1); _repeat51++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Double strike\",\"cost\":\"3\",\"desc\":\"Deal 10 damage to an enemy twice\", \"damage\":\"10\",\"damage_count\":\"2\",\"target\":\"enemy\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(int _repeat54 = 0; _repeat54 < (int)(3); _repeat54++) {
			map_placeholder = new Gson().fromJson("{\"name\":\"Defend\",\"cost\":\"2\",\"desc\":\"Gain 8 block\", \"block_self\":\"8\", \"target\":\"self\"}", new TypeToken<HashMap<String, Object>>(){}.getType());
			card_list.add(map_placeholder);
		}
		for(Double i=0d;i<card_list.size();i++){
			library_id_list.add(i);
		}
		for(int i=0;i<6;i++){
			_Draw_card();
		}
		_Update_cards();
	}
	
	
	public boolean _Check_win() {
		return _get_targets(player, "enemy").isEmpty();
	}
	
	
	public void _Check_start_phase() {
		if (game_phase.equals("start")) {
			game_phase = "";
			for(Double i=0d;i<card_list.size();i++){
				library_id_list.add(i);
			}
			_Shuffle_into_generic(library_id_list, library_id_list);
			//Draw 5 cards at start no matter what
			for(int i=0;i<5;i++) _Draw_card();
			_Start_turn();
			_Log("Game start phase done.");
		}
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
	
	
	public void _Try_resolve_stacktop(final boolean _can_pass) {
		if (stack_id_list.size() == 0) {
			if (_can_pass) {
				_Player_pass_action();
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Stack is already empty when calling try_resolve_stacktop");
			}
		} else {
			int index=stack_id_list.size()-1;
			double card_id=stack_id_list.get(index);
			HashMap<String, Object> stack_top= card_list.get((int)card_id);
			//check if every choice is done already
			ArrayList<HashMap<String, String>> choices_needed=new ArrayList<>();
			if(stack_top.containsKey("choices")){
				choices_needed=new Gson().fromJson(stack_top.get("choices").toString(),new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType());}
			HashMap<String, Object> choices=stack_choices_list.get(index);
			HashMap<String, Integer> choices_name_to_pos=new HashMap<>();
			HashSet<String> choices_left=new HashSet<>();
			for(int k=0;k<choices_needed.size();k++){
				HashMap<String,String> c=choices_needed.get(k);
				if(c.containsKey("choice_name")){
					String choice_name=c.get("choice_name");
					choices_name_to_pos.put(choice_name, k);
					choices_left.add(choice_name);
				}
			}
			choices_left.removeAll(choices.keySet());
			
			if(choices_left.isEmpty()){
				stack_id_list.remove(index); 
				HashMap<String, Object> target=stack_target_list.remove(index);
				stack_choices_list.remove(index);
				_Apply_card_id_with_choices(card_id, target, choices);
			}
			else{
				String choice_name = choices_left.iterator().next();
				HashMap<String, String> choice_map=choices_needed.get(choices_name_to_pos.get(choice_name));
				String choice="Unknown";
				if(choice_map.containsKey("type"))choice=choice_map.get("type");
				switch(choice){
					case "text":
					{
						final EditText editttext1= new EditText(BattleActivity.this);
						
						LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						
						editttext1.setLines(choice_map.containsKey("num_lines")?Integer.parseInt(choice_map.get("num_lines")):1);
						editttext1.setLayoutParams(lpar);
						choice_dialog.setView(editttext1);
						choice_dialog.setTitle("Text choice");
						choice_dialog.setMessage(choice_map.containsKey("desc")?choice_map.get("desc"):"Enter some text here. No more information given.");
						
						choice_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								dialog_result=editttext1.getText().toString();
								choices.put(choice_name, dialog_result);
								_Try_resolve_stacktop(false);
							}
						});
						
						choice_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
							}
						});
						choice_dialog.create().show();
					}
					break;
					
					case "card":
					{
						
						final ListView lv= new ListView(BattleActivity.this);
						ArrayList<Integer> chosen_cards=new ArrayList<>();//options you chose already
						ArrayList<HashMap<String, Object>> card_choices=new ArrayList<>();//options to choozs from
						int max_choices=
						choice_map.containsKey("max_choice")?Integer.parseInt(choice_map.get("max_choice")):-1;
						int source_count=
						choice_map.containsKey("source_count")?Integer.parseInt (choice_map.get("source_count")):Integer.MAX_VALUE;
						lv.setAdapter(new Card_viewAdapter(card_choices){
							@Override
							public View getView(final int _position, View _v, ViewGroup _container) {
								LayoutInflater _inflater = getLayoutInflater();
								View _view = _v;
								if (_view == null) {
									_view = _inflater.inflate(R.layout.card, null);
								}
								
								final LinearLayout linear1 = _view.findViewById(R.id.linear1);
								final TextView textview1 = _view.findViewById(R.id.textview1);
								map_placeholder = _data.get((int)_position);
								Integer p=Integer.valueOf(_position);
								int chosen_pos=chosen_cards.indexOf(p);
								String name_prefix=(chosen_pos>=0)?(String.valueOf(1+chosen_pos)+"."):"";
								/*if (_position == 3) {
			textview1.setTypeface(Typeface.DEFAULT, 1);
		} else {
			textview1.setTypeface(Typeface.DEFAULT, 0);
		}*/
								if (map_placeholder.containsKey("name")) {
									string_placeholder = name_prefix+map_placeholder.get("name").toString();
								} else {
									string_placeholder = name_prefix+"Unnamed card";
								}
								if (map_placeholder.containsKey("final_cost")) {
									string_placeholder = string_placeholder.concat("(".concat(map_placeholder.get("final_cost").toString().concat(")")));
								} else {
									
								}
								textview1.setText(string_placeholder);
								
								return _view;
							}
						});
						
						lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
								final int _position = _param3;
								Integer p=_position;
								if(chosen_cards.contains(p)){
									chosen_cards.remove(p);
								}
								else{
									if(max_choices<0||max_choices>chosen_cards.size()){
										chosen_cards.add(p);
									}
								}
								((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
							}
						});
						
						LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
						
						lv.setLayoutParams(lpar);
						choice_dialog.setView(lv);
						choice_dialog.setTitle("Card choice");
						choice_dialog.setMessage(choice_map.getOrDefault("desc","Choose some card here...")+(max_choices>=0?"\nMax choice count:"+String.valueOf(max_choices):""));
						
						if(choice_map.containsKey("source")){
							switch(choice_map.get("source")){
								case "hand":
								_id_to_cards(hand_id_list, card_choices);break;
								case "library":
								_id_to_cards(library_id_list, card_choices);break;
								default:
								SketchwareUtil.showMessage(getApplicationContext(),"Unknown source for card choice!");
							}
						}else{
							SketchwareUtil.showMessage(getApplicationContext(),"No source given for card choice!");
						}
						
						if(card_choices.size()>source_count){
							card_choices.subList(source_count, card_choices.size()).clear();
						}
						choice_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
								ArrayList<Double> choices_id=new ArrayList<>();
								for(HashMap<String,Object>card:card_choices){
									choices_id.add(_card_to_id(card,true));
								}
								HashMap<String,String> card_choice_result=new HashMap<>();
								card_choice_result.put("card_choices",_Numlist_to_json(choices_id));
								
								ArrayList<Double> chosen_cards_double=new ArrayList<>();
								ArrayList<Double> chosen_cards_id_double=new ArrayList<>();
								for(Integer i:chosen_cards){
									chosen_cards_double.add(i.doubleValue());
									chosen_cards_id_double.add(choices_id.get(i));
								}
								
								card_choice_result.put("chosen_cards",_Numlist_to_json(chosen_cards_double));
								card_choice_result.put("chosen_cards_id",_Numlist_to_json(chosen_cards_id_double));
								
								choices.put(choice_name, new Gson().toJson(card_choice_result));
								
								
								_Try_resolve_stacktop(false);
							}
						});
						
						choice_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
							}
						});
						choice_dialog.create().show();
					}
					break;
					
					default:
					SketchwareUtil.showMessage(getApplicationContext(),"Unimplemented choice needed:"+choice);
				}
			}
		}
	}
	
	
	public void _Generate_script() {
		class Encoder{
			String encode(String str){
				String out="";
				for(char c:str.toCharArray()){
					if('A'<=c&&c<='Z'){
						out+=c+" ";
					}
					else if(c==' '){
						out+="WS ";
					}
					else if(c=='_'){
						out+="US ";
					}
					else if(c=='\n'){
						out+="NL ";
					}
					else if(c==';'){
						out+="SEMICOLON ";
					}
					else{
						out+="Unimplemented! ";
					}
				}
				return out;
			}
		}
		String output="";
		for(char c = 'A'; c <= 'Z'; c++) { 
			output=output+"ASSIGN_STRING "+c+" "+c+";\n";
		}
		output+="ASSIGN_STRING WS  ;\n";
		output+="ASSIGN_STRING US _;\n";
		output+="ASSIGN_STRING NL \n;\n";
		output+="ASSIGN_STRING MID TO_STRING RES ;\n";
		output+="ASSIGN_STRING LASTA MAIN SEMICOLON NL MID MAIN LASTA SEMICOLON NL LASTB SEMICOLON;\n";
		output+="ASSIGN_STRING LASTB LOG RES;\n";
		output+="ASSIGN_STRING MAIN ";
		String main=(new Encoder()).encode(output);
		output+=main +";\n";
		output+="TO_STRING RES "+main+"MAIN SEMICOLON NL MID MAIN LASTA SEMICOLON NL LASTB SEMICOLON;\n";
		output+="LOG RES;";
		string_placeholder=output;
		
		((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", string_placeholder));
		SketchwareUtil.showMessage(getApplicationContext(), "Quine script copied");
	}
	
	
	public void _JSON_numlist(final String _input, final ArrayList<Double> _num) {
		_num.clear();
		_num.addAll(new Gson().fromJson(_input.toString(),new TypeToken<ArrayList<Double>>(){}.getType()));
	}
	
	
	public void _id_to_cards(final ArrayList<Double> _id, final ArrayList<HashMap<String, Object>> _cards) {
		_cards.clear();
		for(double id:_id){
			_cards.add(card_list.get((int)id));
		}
	}
	
	
	public void _Discard_by_id(final double _id, final boolean _can_trigger) {
		if(hand_id_list.remove(_id)){
			if(!discard_id_list.contains(_id)){
				discard_id_list.add(_id);
			}
		}
	}
	
	
	public String _Numlist_to_json(final ArrayList<Double> _numlist) {
		return new Gson().toJson(_numlist);
	}
	
	
	public void _Try_put_id_on_stack(final double _card_id, final HashMap<String, Object> _target) {
		card_temp = card_list.get((int)_card_id);
		if (card_temp.containsKey("target")) {
			if (card_temp.get("target").toString().equals("self")) {
				if(! (_target == player)){
					SketchwareUtil.showMessage(getApplicationContext(), "This card can only target yourself");
					return;}
			}
			if (card_temp.get("target").toString().equals("enemy")) {
				if (_target.containsKey("group") && (player.containsKey("group") && _target.get("group").toString().equals(player.get("group").toString()))) {
					SketchwareUtil.showMessage(getApplicationContext(), "This card can only target an enemy");
					return;
				}
			}
		}
		if (card_temp.containsKey("final_cost")) {
			if (Double.parseDouble(card_temp.get("final_cost").toString()) > ap) {
				SketchwareUtil.showMessage(getApplicationContext(), "Not enough AP to play this card");
				return;
			} else {
				
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "This card has no AP cost and can't be played");
			return;
		}
		ap = ap - Double.parseDouble(card_temp.get("final_cost").toString());
		library_id_list.remove(_card_id);
		hand_id_list.remove(_card_id);
		discard_id_list.remove(_card_id);
		stack_id_list.remove(_card_id);
		stack_id_list.add(Double.valueOf(_card_id));
		stack_target_list.add(_target);
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("placeholder", "3");
			stack_choices_list.add(_item);
		}
		_Try_resolve_stacktop(false);
	}
	
	
	public void _Apply_card_id_with_choices(final double _card_id, final HashMap<String, Object> _target, final HashMap<String, Object> _choices) {
		HashMap<String, Object> _card=card_list.get((int)_card_id);
		apply_card_map = new HashMap<>();
		if (_card.containsKey("damage") || _card.containsKey("damage_enemies")) {
			if (_card.containsKey("attack_modifiers")) {
				apply_card_map = new Gson().fromJson(_card.get("attack_modifiers").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
			}
			if (_card.containsKey("damage_count")) {
				apply_card_map.put("count", _card.get("damage_count").toString());
			}
			if (_card.containsKey("damage_enemies")) {
				for(HashMap<String, Object> tar: _get_targets(player,"enemy")){
					_Attack(player,tar,Double.parseDouble((String) _card.get("damage_enemies")), apply_card_map);
				}
			}
			if (_card.containsKey("damage")) {
				_Attack(player, _target, Double.parseDouble(_card.get("damage").toString()), apply_card_map);
			}
		}
		if (_card.containsKey("block_self")) {
			if (_card.containsKey("block_modifiers")) {
				modifiers_temp = new Gson().fromJson(_card.get("block_modifiers").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				_Apply_block(player, player, Double.parseDouble(_card.get("block_self").toString()), modifiers_temp);
			} else {
				_Apply_block(player, player, Double.parseDouble(_card.get("block_self").toString()), empty_map);
			}
		}
		if (_card.containsKey("lose_health_self")) {
			player.put("health", String.valueOf((long)(Double.parseDouble(player.get("health").toString()) - Double.parseDouble(_card.get("lose_health_self").toString()))));
		}
		if (_card.containsKey("give_ap")) {
			ap = ap + Double.parseDouble(_card.get("give_ap").toString());
		}
		if (_card.containsKey("give_card")) {
			for(int _repeat64 = 0; _repeat64 < (int)(Double.parseDouble(_card.get("give_card").toString())); _repeat64++) {
				_Draw_card();
			}
		}
		if (_card.containsKey("add_text_choice_to_hand")) {
			if (_choices.containsKey("card_name")) {
				hand_id_list.add(Double.valueOf(_card_to_id(_getCardByName(_choices.get("card_name").toString()), true)));
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with add_text_choice_to_hand without making a choice in card_name!");
			}
		}
		if (_card.containsKey("run_text_choice")) {
			if (_choices.containsKey("script")) {
				String language=_card.getOrDefault("script_language","").toString();
				_Run_script_type(player, _target, _card, _choices.get("script").toString(), language, _choices);
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with run_text_choice without making a choice in script!");
			}
		}
		if (_card.containsKey("discard_card_choice")) {
			if (_choices.containsKey("card_in_hand")) {
				apply_card_map = new Gson().fromJson(_choices.get("card_in_hand").toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
				ArrayList<Double> cards=new ArrayList<>();
				ArrayList<Double> choices=new ArrayList<>();
				ArrayList<Double> choices_id=new ArrayList<>();
				if (apply_card_map.containsKey("card_choices")) {
					_JSON_numlist(apply_card_map.get("card_choices").toString(), cards);
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with discard_card_choice with missing card_choices in card_in_hand!");
				}
				if (apply_card_map.containsKey("chosen_cards")) {
					_JSON_numlist(apply_card_map.get("chosen_cards").toString(), choices);
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with discard_card_choice with missing chosen_cards in card_in_hand!");
				}
				if (apply_card_map.containsKey("chosen_cards_id")) {
					_JSON_numlist(apply_card_map.get("chosen_cards_id").toString(), choices_id);
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with discard_card_choice with missing chosen_cards_id in card_in_hand!");
				}
				for(double d:choices_id){
					_Discard_by_id(d,true);
				}
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: you managed to resolve a card with discard_card_choice without making a choice in card_in_hand!");
			}
		}
		if (_card.containsKey("on_play_script")) {
			String language=_card.getOrDefault("script_language","").toString();
			_Run_script_type(player, _target, _card, _card.get("on_play_script").toString(), language, _choices);
		}
		if (_card.containsKey("cost_change_until_play")) {
			_card.remove("cost_change_until_play");
		}
		if (_card.containsKey("cost_change_for_turn")) {
			_card.remove("cost_change_for_turn");
		}
		if (_card.containsKey("on_play_destination")) {
			if (_card.get("on_play_destination").toString().equals("hand")) {
				hand_id_list.add(Double.valueOf(_card_id));
			}
			if (_card.get("on_play_destination").toString().equals("discard")) {
				discard_id_list.add(Double.valueOf(_card_id));
			}
			if (_card.get("on_play_destination").toString().equals("field")) {
				if (_card.containsKey("max_field_time")) {
					_card.put("remaining_field_time", _card.get("max_field_time").toString());
				}
				field_id_list.add(Double.valueOf(_card_id));
			}
		} else {
			discard_id_list.add(Double.valueOf(_card_id));
		}
		_Check_game_state();
		_Refresh_views();
	}
	
	
	public double _card_to_id(final HashMap<String, Object> _card, final boolean _register) {
		for(int i=0;i<card_list.size();i++){
			HashMap<String, Object> card=card_list.get(i);
			if(card==_card){
				return i;
			}
		}
		if(_register){
			card_list.add(_card);
			return card_list.size()-1;}
		else{return -1;}
	}
	
	
	public void _Run_script(final HashMap<String, Object> _actor, final HashMap<String, Object> _target, final HashMap<String, Object> _card, final String _script, final HashMap<String, Object> _var) {
		(new Interpreter(_actor, _target, _card, _script, _var)).run();
	}
	class Interpreter{
		//environment variables
		Map<String,Object> actor;
		Map<String,Object> target;
		Map<String, Object> card; //may be null if non-card
		ArrayList<String> lines;
		int line_pos=0; //possible future feature: stack of line_pos to allow jal/return
		int steps_runned=0;
		//limit the runtime to ~20ms (that's the number from older version though)
		//I can't think of legit use case that requires over 10000 steps for now
		final int steps_limit=10000;
		//map for local variables
		HashMap<String, Object> vars=new HashMap<>();
		//where each line will jump to
		Boolean setup=false;
		HashMap<Integer, Integer> jump_targets=new HashMap<>();
		
		//check if a string represents an int
		//AI generated hand check version instead of regex for performance
		boolean isInt(String s) {
			final int len = s.length();
			if (len == 0) return false;
			int i = 0;
			if (s.charAt(0) == '-') {
				if (len == 1) return false;
				if (len > 10) return false;   // '-' and 9 more digits
				i = 1;
			} else {
				if (len > 9) return false;
			}
			for (; i < len; i++) {
				char c = s.charAt(i);
				if (c < '0' || c > '9') return false;
			}
			return true;
		}
		
		boolean isFloat(String s) {
			final int len = s.length();
			if (len == 0) return false;
			int i = 0;
			if (s.charAt(0) == '-') {
				if (len == 1) return false;
				i = 1;
			}
			// integral part: 1-9 places
			final int intStart = i;
			while (i < len) {
				char c = s.charAt(i);
				if (c < '0' || c > '9') break;
				i++;
			}
			final int intDigits = i - intStart;
			if (intDigits < 1 || intDigits > 9) return false;
			if (i == len) return true;              // pure integer OK
			if (s.charAt(i) != '.') return false;   // decimal point only
			i++;                                    // skip '.'
			while (i < len) {                       // decimal part, 0+ digits
				char c = s.charAt(i);
				if (c < '0' || c > '9') return false;
				i++;
			}
			return true;
		}
		
		// replacement for replaceFirst("^\\s+", "") =====
		String stripLeading(String s) {
			final int n = s.length();
			int i = 0;
			while (i < n) {
				char c = s.charAt(i);
				if (c == ' ' || c == '\t' || c == '\n'
				|| c == '\r' || c == '\f' || c == '\u000B') {
					i++;
				} else {
					break;
				}
			}
			return (i == 0) ? s : s.substring(i);   // return original when no leading 0
		}
		
		
		//resolve a string to a variable
		Object resolve_var(String name){
			//1a. integer literals
			if(isInt(name)){
				//SketchwareUtil.showMessage(getApplicationContext(),name+" is int");
				return (Double)(Double.parseDouble(name));}
			//1b. float literals
			if(isFloat(name))return Double.parseDouble(name);
			//2. special vars (always uppercase)
			switch(name){
				//version info
				case "LAST_UPDATED": //I try my best to update this every time I release it
				return "20260915";
				//constants
				case "SEMICOLON": //the symbol you cannot get with ASSIGN_STRING
				return ";";
				case "EMPTY_STRING": //used as placeholder value in MAP_GET when the key does not exist
				return "";
				case "NEW_LINE": //you can get it with ASSIGN_STRING but the readability is bad
				return "\n";
				//maps
				case "ACTOR":
				return new StringMapView(actor);
				case "TARGET":
				return new StringMapView(target);
				case "CARD":
				return new StringMapView(card);
				//game state attributes
				case "ACTOR_NAME":
				return actor!=null? (actor.containsKey("name")?actor.get("name").toString():"An unnamed actor"): "No actor";
				case "ACTOR_BLOCK":
				return actor!=null? (actor.containsKey("block")?Double.parseDouble(actor.get("block").toString()):0d): 0d;
				case "AP":
				return ap;
				//game state lists
				case "ENTITIES":
				return new TypedListView(entity_list, ElementType.MAP);
				case "CARDS": case "ALL_CARDS":
				return new TypedListView(card_list, ElementType.MAP);
				case "HAND_ID_LIST":
				return new TypedListView(hand_id_list, ElementType.DOUBLE);
				case "LIBRARY_ID_LIST":
				return new TypedListView(library_id_list, ElementType.DOUBLE);
				case "DISCARD_ID_LIST":
				return new TypedListView(discard_id_list, ElementType.DOUBLE);
			}
			
			//3. local vars
			if(vars.containsKey(name))return vars.get(name);
			
			//4. fail
			_Log("Error, "+name+" unresolved in script");
			return null;
		}
		
		final Class<?> numeric=Double.class;
		
		class VariableTypeException extends RuntimeException {
			public VariableTypeException(String message) {
				super(message);
			}
		}
		
		void type_check(Object var, Class<?> target, String command){
			if(!target.isInstance(var)){
				String err_msg="Script error: some variable is not of type "+target.toString()+" in "+command+"! ";
				if(var!=null){
					throw new VariableTypeException(err_msg+"The variable is of type "+var.getClass().getName());
				}
				else{
					throw new VariableTypeException(err_msg+"The variable is null!!");
				}
			}
		}
		
		void type_check(Object var, Class<?> target){
			type_check(var, target, "some command");
		}
		
		//typechecker for lists (AI generated)
		private TypedListView requireTypedList(String name, ElementType expected, String cmd) {
			Object v = resolve_var(name);
			if (!(v instanceof TypedListView)) //null always fail
			throw new VariableTypeException(name + " is not a list in " + cmd);
			TypedListView list = (TypedListView) v;
			if (list.getType() != expected)
			throw new VariableTypeException(
			"List " + name + " is " + list.getType() + ", expected " + expected + " in " + cmd);
			return list;
		}
		
		class ArgsCountException extends RuntimeException {
			public ArgsCountException(String message) {
				super(message);
			}
		}
		
		void args_size_check(ArrayList<String> args,int lower, int upper, String command){
			if(args.size()-1>=lower && args.size()-1<=upper){
				return;
			}
			else{
				String num_part="";
				if(lower==upper){
					num_part="exactly "+String.valueOf(lower);
				}
				else if(upper==Integer.MAX_VALUE){
					num_part="at least "+String.valueOf(lower);
				}
				else{
					num_part=String.valueOf(lower)+"-"+String.valueOf(upper);
				}
				throw new ArgsCountException(command+" should receive "+num_part+" arg(s)!!");
			}
		}
		
		//auxiliary data types
		public class StringMapView extends AbstractMap<String, String> {
			//AbstractMap does some boilerplate for us
			private final Map<String, Object> backing;
			//CAUTION: AI generated, not proofread throughoutly
			//WARNING: equals/hashcode can emit IllegalStateException and fail to be symmetric
			
			public StringMapView(Map<String, Object> backing) {
				this.backing = Objects.requireNonNull(backing, "backing");
			}
			
			public Map<String, Object> unwrap() {
				return backing;
			}
			// ---------- read ----------
			@Override public String get(Object key) {
				return requireString(key, backing.get(key));
			}
			@Override public boolean containsKey(Object key) {
				return backing.containsKey(key);
			}
			@Override public int size() {
				return backing.size();
			}
			
			// ---------- write ----------
			@Override public String put(String key, String value) {
				return requireString(key, backing.put(key, value));
			}
			@Override public String remove(Object key) {
				return requireString(key, backing.remove(key));
			}
			@Override public void clear() {
				backing.clear();
			}
			@Override
			public void putAll(Map<? extends String, ? extends String> m) {
				for (Entry<? extends String, ? extends String> e : m.entrySet())
				backing.put(e.getKey(), e.getValue());
			}
			
			// ---------- EntrySet View----------
			//used for equals and hashcode
			@Override
			public Set<Entry<String, String>> entrySet() {
				return new AbstractSet<Entry<String, String>>() {
					@Override public Iterator<Entry<String, String>> iterator() {
						Iterator<Entry<String, Object>> it = backing.entrySet().iterator();
						return new Iterator<Entry<String, String>>() {
							@Override public boolean hasNext() { return it.hasNext(); }
							@Override public Entry<String, String> next() {
								Entry<String, Object> e = it.next();
								return new Entry<String, String>() {
									@Override public String getKey() { return e.getKey(); }
									@Override public String getValue() {
										return requireString(e.getKey(), e.getValue());
									}
									@Override public String setValue(String v) {
										return requireString(e.getKey(), e.setValue(v));
									}
									@Override public boolean equals(Object o) {
										if (!(o instanceof Entry)) return false;
										Entry<?,?> x = (Entry<?,?>) o;
										return eq(getKey(), x.getKey()) && eq(getValue(), x.getValue());
									}
									@Override public int hashCode() {
										return (getKey()==null?0:getKey().hashCode())
										^ (getValue()==null?0:getValue().hashCode());
									}
									private boolean eq(Object a, Object b) {
										return a == null ? b == null : a.equals(b);
									}
								};
							}
							@Override public void remove() { it.remove(); }
						};
					}
					@Override public int size() { return backing.size(); }
				};
			}
			
			// ---------- toString → JSON ----------
			@Override
			public String toString() {
				// Convert everything to string first just in case
				Map<String, String> snapshot = new LinkedHashMap<>();
				for (Entry<String, Object> e : backing.entrySet()) {
					snapshot.put(e.getKey(), String.valueOf(e.getValue()));
				}
				return new Gson().toJson(snapshot);
			}
			
			// ---------- type checking ----------
			private String requireString(Object key, Object v) {
				if (v == null) return null;
				if (v instanceof String) return (String) v;
				throw new IllegalStateException(
				"Key '" + key + "' has non-String value: " + v.getClass().getName());
			}
		}
		
		public class TypedListView extends AbstractList<Object> {
			//CAUTION: AI generated, not proofread throughoutly
			
			private final List<Object> backing;
			private final ElementType type;   // final, unmodifiable after construction
			
			public TypedListView(List<?> backing, ElementType type) {
				this.type = Objects.requireNonNull(type);
				this.backing = (List<Object>) Objects.requireNonNull(backing, "backing");
				
				// safety chck: check if the type of the first element match what we supply
				if (!backing.isEmpty()) {
					Object first = backing.get(0);
					if (first != null && !type.acceptsRaw(first)) {
						throw new IllegalArgumentException(
						"Backing list element type " + first.getClass().getName() +
						" is incompatible with ElementType." + type);
					}
				}
			}
			
			public ElementType getType() {
				return type;
			}
			
			// ========== List interface ==========
			
			@Override public int size() { return backing.size(); }
			
			@Override
			public Object get(int index) {
				return adapt(backing.get(index));
			}
			
			@Override
			public Object set(int index, Object element) {
				Object encoded = encode(element);
				Object oldRaw = backing.set(index, encoded);
				return adapt(oldRaw);
			}
			
			@Override
			public void add(int index, Object element) {
				backing.add(index, encode(element));
			}
			
			@Override
			public Object remove(int index) {
				return adapt(backing.remove(index));
			}
			
			@Override
			public void clear() {
				backing.clear();
			}
			
			// ========== adapters ==========
			
			/** adapting: stuff to script classes */
			private Object adapt(Object raw) {
				if (raw == null) return null;
				switch (type) {
					case STRING:
					if (raw instanceof String) return raw;
					throw typeError("String", raw);
					
					case DOUBLE:
					if (raw instanceof Double) return raw;
					if (raw instanceof Number) return ((Number) raw).doubleValue();
					if (raw instanceof String) { //being nice here
						try {
							return Double.parseDouble((String) raw);
						} catch (NumberFormatException e) {
							throw typeError("String parseable as Double", raw);
						}
					}
					throw typeError("Double", raw);
					
					case MAP:
					if (raw instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, Object> m = (Map<String, Object>) raw;
						return new StringMapView(m);
					}
					throw typeError("Map", raw);
					
					default:
					throw new IllegalStateException("Unknown ElementType: " + type);
				}
			}
			
			/** encoding: script to raw */
			private Object encode(Object element) {
				if (element == null) return null;
				switch (type) {
					case STRING:
					if (element instanceof String) return element;
					throw typeError("String", element);
					
					case DOUBLE:
					if (element instanceof Double) return element;
					if (element instanceof Number) return ((Number) element).doubleValue();
					throw typeError("Double", element);
					
					case MAP:
					if (element instanceof StringMapView) {
						return ((StringMapView) element).unwrap();
					}
					if (element instanceof Map) return element;
					throw typeError("Map or StringMapView", element);
					
					default:
					throw new IllegalStateException("Unknown ElementType: " + type);
				}
			}
			
			//like unwrap but only works for double list and still wrapped
			public List<Double> asDoubleList() {
				if (type != ElementType.DOUBLE)
				throw new IllegalStateException("List is not of type DOUBLE");
				return new AbstractList<Double>() {
					@Override public Double get(int i) {
						return (Double) TypedListView.this.get(i);   // adapt parses for us
					}
					@Override public int size() {
						return TypedListView.this.size();
					}
					@Override public Double set(int i, Double v) {
						return (Double) TypedListView.this.set(i, v); // encode converts to string for us
					}
					@Override public void add(int i, Double v) {
						TypedListView.this.add(i, v);
					}
					@Override public Double remove(int i) {
						return (Double) TypedListView.this.remove(i);
					}
					
				};
			}
			
			private IllegalStateException typeError(String expected, Object actual) {
				return new IllegalStateException(
				"TypedListView<" + type + "> expected " + expected +
				" but got " + (actual == null ? "null" : actual.getClass().getName()));
			}
		}
		
		
		Interpreter(HashMap<String, Object> _actor, HashMap<String, Object> _target, HashMap<String, Object> _card, String _script){
			//parse script into lines
			actor=_actor;
			target=_target;
			card=_card;
			String[] raw_lines=_script.split(";");
			lines=new ArrayList<>();
			for(String s:raw_lines){
				s=stripLeading(s);
				lines.add(s);
			}
			record_jumps();
		}
		
		Interpreter(HashMap<String, Object> _actor, HashMap<String, Object> _target, HashMap<String, Object> _card, String _script, HashMap<String, Object> _vars){
			this(_actor, _target, _card, _script);
			vars.putAll(_vars);
		}
		
		void record_jumps(){
			HashMap<Integer, String> ntl=new HashMap<>(); //jump source line number to label
			HashMap<String, Integer> ltn=new HashMap<>(); //label to jump target line number
			TreeMap<Integer, String> keywords=new TreeMap<>(); //stack for keywords eg if, while
			
			int line_pos=0;
			for(line_pos=0;line_pos<lines.size();line_pos++){
				String line=lines.get(line_pos);
				if(line.startsWith("ASSIGN_STRING ")){continue;}
				//do ordinary parsing
				String[] raw_args=line.trim().split(" ");
				ArrayList<String> args=new ArrayList<>();
				for(String s:raw_args){
					if(s.length()>0) args.add(s);
				}
				//0 args is NOP
				if(args.size()>0){
					//record stuff
					switch(args.get(0)){
						case "JUMP": case "JUMP_IF":
						{
							if(args.size()<2){
								_Log("Record jump fail, JUMP and related commands should receive at least 1 arg!!");
								return;
							}
							String str1=args.get(1);
							if(isInt(str1)){ //hard jumps tolerated but discouraged: I reserve the right to have empty lines silently dropped
								int int1=Integer.parseInt(str1);
								jump_targets.put(line_pos, int1);
							} 
							else{ //create jump dict
								ntl.put(line_pos, str1);
							}
							break;
						}
						case "LABEL":
						{
							if(args.size()!=2){
								_Log("Record jump fail, LABEL should receive exactly 1 args!!");
								return;
							}
							String str1=args.get(1);
							//create jump dict
							ltn.put(str1, line_pos);
							break;
						}
						case "IF":
						{
							keywords.put(line_pos, "IF");
							break;
						}
						case "ELSE":
						{
							if(keywords.isEmpty()){
								_Log("Record jump fail, ELSE used on empty keyword stack");
								return;
							}
							Map.Entry<Integer, String> last=keywords.pollLastEntry();
							if(!last.getValue().equals("IF")){
								_Log("Record jump fail, ELSE not matched with IF");
								return;
							}
							jump_targets.put(last.getKey(),line_pos+1); //jump to the next line after ELSE
							keywords.put(line_pos, "ELSE");
							break;
						}
						case "END_IF":
						{
							if(keywords.isEmpty()){
								_Log("Record jump fail, END_IF used on empty keyword stack");
								return;
							}
							Map.Entry<Integer, String> last=keywords.pollLastEntry();
							if(last.getValue().equals("IF")){
								jump_targets.put(last.getKey(),line_pos+1); //jump to next line after END_IF
							}
							else if(last.getValue().equals("ELSE")){
								jump_targets.put(last.getKey(),line_pos+1); //jump to next line after END_IF
							}
							else{
								_Log("Record jump fail, END_IF not matched with IF or ELSE");
								return;
							}
							break;
						}
						case "WHILE":
						{
							keywords.put(line_pos, "WHILE");
							break;
						}
						case "END_WHILE":
						{
							if(keywords.isEmpty()){
								_Log("Record jump fail, END_WHILE used on empty keyword stack");
								return;
							}
							Map.Entry<Integer, String> last=keywords.pollLastEntry();
							if(last.getValue().equals("WHILE")){
								jump_targets.put(last.getKey(),line_pos+1); //jump to the next line so doesn't trigger looping back
								jump_targets.put(line_pos, last.getKey()); //jump back to the loop check
							}
							else{
								_Log("Record jump fail, END_WHILE not matched with while");
								return;
							}
							break;
						}
					}
				}
			}
			if(! keywords.isEmpty()){
				_Log("Record jump fail, keyword stack not empty at end of script");
				return;
			}
			for (Map.Entry<Integer, String> entry : ntl.entrySet()) {
				if(ltn.containsKey(entry.getValue())){
					jump_targets.put(entry.getKey(),ltn.get(entry.getValue()));
				}
				else{
					_Log("During record jump, "+entry.getValue()+" label not found!");
					return;
				}
			}
			setup=true;
		}
		
		Boolean eval_bool(String op, Object var1, Object var2){
			//returns null when error occurs
			switch(op){
				case "EQ": case "EQUAL": //equal, no type check
				{
					if(var1==null){
						_Log("Error: var1 is null in EQ eval_bool");
						return null;
					}
					if(var2==null){
						_Log("Error: var2 is null in EQ eval_bool");
						return null;
					}
					return(var1.equals(var2));
				}
				case "NEQ": case "NOT_EQUAL": //not equal, no type check
				{
					if(var1==null){
						_Log("Error: var1 is null in NEQ eval_bool");
						return null;
					}
					if(var2==null){
						_Log("Error: var2 is null in NEQ eval_bool");
						return null;
					}
					return(! var1.equals(var2));
				}
				case "GEQ":  //>=, double only
				{
					type_check(var1, numeric, "eval_bool");
					type_check(var2, numeric, "eval_bool");
					return ((Double)var1>= (Double)var2);
				}
				case "LEQ":  //<=, double only
				{
					type_check(var1, numeric, "eval_bool");
					type_check(var2, numeric, "eval_bool");
					return ((Double)var1<= (Double)var2);
				}
				case "LT":  //<, double only
				{
					type_check(var1, numeric, "eval_bool");
					type_check(var2, numeric, "eval_bool");
					return ((Double)var1< (Double)var2);
				}
				case "GT":  //>, double only
				{
					type_check(var1, numeric, "eval_bool");
					type_check(var2, numeric, "eval_bool");
					return ((Double)var1> (Double)var2);
				}
				default:
				_Log("Unknown comparison invoked:"+op+"!");
				return null;
			}
		}
		
		void run(){
			if(!setup){
				_Log("Setup did not complete, script aborted");
				return;
			}
			//terminate when no more lines
			try{
				while(line_pos>=0 && line_pos<lines.size()){
					String line=lines.get(line_pos);
					//trim starting whitespaces is done during initialization
					//line=line.replaceFirst("^\\s+", "");
					//handle assign string
					if(line.startsWith("ASSIGN_STRING ")){
						int second_space=line.indexOf(" ", 14);
						if(second_space<0){
							_Log("ASSIGN_STRING space parse fail!");
							return;
						}
						String var_name=line.substring(14,second_space);
						String string_value=line.substring(second_space+1);
						vars.put(var_name,string_value);
					}
					else{
						//do ordinary parsing
						String[] raw_args=line.trim().split(" ");
						ArrayList<String> args=new ArrayList<>();
						for(String s:raw_args){
							if(s.length()>0) args.add(s);
						}
						//0 args is NOP
						if(args.size()>0){
							//do stuff
							switch(args.get(0)){
								//io, debug commands
								case "TOAST":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, String.class, args.get(0));
									SketchwareUtil.showMessage(getApplicationContext(),(String)var1);
									break;
								}
								case "LOG":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, String.class, args.get(0));
									_Log("Script output:"+(String)var1);
									break;
								}
								case "LOG_ENVIRONMENT":
								{
									args_size_check(args,0,0,args.get(0));
									_Log("Script environment:"+vars.toString());
									break;
								}
								//general commands
								case "ASSIGN": //assign var1 to be var2, no type check
								{
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									vars.put(args.get(1), var2);
									break;
								}
								case "EVAL_BOOL": case "EVAL_BOOLEAN":
								{
									args_size_check(args,4,4,args.get(0));
									//use jump dict
									Object var3=resolve_var(args.get(3));
									Object var4=resolve_var(args.get(4));
									Boolean result= eval_bool(args.get(2),var3,var4);
									if(result==null){
										_Log("Eval bool failed in "+args.get(0));
										return;
									}
									vars.put(args.get(1), result?1:0);
									break;
								}
								case "SET_TARGET":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, StringMapView.class, args.get(0));
									target = ((StringMapView) var1).unwrap();
									break;
								}
								//string commands
								//possible future feature: substring (never used that somehow)
								case "TO_STRING": //convert anything to String, concat when multiple inputs
								{
									args_size_check(args,1,Integer.MAX_VALUE,args.get(0));
									String combined="";
									for(int arg_pos=2; arg_pos<args.size(); arg_pos++){
										Object var=resolve_var(args.get(arg_pos));
										if(Double.class.isInstance(var)){
											if(((Double)var)%1==0){
												combined += new DecimalFormat("0").format((double)var);
											}
											else{
												combined += new DecimalFormat("0.00").format((double)var);
											}
										}
										else{
											combined += String.valueOf(var);
										}
									}
									vars.put(args.get(1), combined);
									break;
								}
								case "CONCAT": //concat two strings together
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, String.class, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, String.class, args.get(0));
									vars.put(args.get(1), (String)var2 + (String) var3);
									break;
								}
								//math manipulation commands
								case "STRING_TO_NUM": case "STRING_TO_DOUBLE":
								{
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, String.class, args.get(0));
									String str=(String)var2;
									if(!isFloat(str)) throw new IllegalArgumentException("The string "+str+" does not represent a double in "+args.get(0)+"!");
									vars.put(args.get(1),Double.parseDouble(str));
									break;
								}
								case "ADD":
								{ //possible future feature: unlimited arg count like TO_STRING
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), (Double)var2 + (Double) var3);
									break;
								}
								case "SUB": case "SUBTRACT":
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), (Double)var2 - (Double) var3);
									break;
								}
								case "MUL": case "MULTIPLY":
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), (Double)var2 * (Double) var3);
									break;
								}
								case "DIV": case "DIVIDE":
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), (Double)var2 / (Double) var3);
									break;
								}
								case "MOD": case "REM": case "REMINDER": 
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), (Double)var2 % (Double) var3);
									break;
								}
								case "CLAMP": //clamps middle to between lower and upper bounds
								{
									args_size_check(args,4,4,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									Object var4=resolve_var(args.get(4));
									type_check(var4, numeric, args.get(0));
									Double int2=(Double)var2;
									Double int3=(Double)var3;
									Double int4=(Double)var4;
									Double result=((int3<int2)?int2:(int3>int4?int4:int3));
									vars.put(args.get(1), result);
									break;
								}
								case "MAX": //max among args, return -max double with 0 arg (-infinity makes more sense though, may be changed in future)
								{
									args_size_check(args,1,Integer.MAX_VALUE,args.get(0));
									Double max=-Double.MAX_VALUE;
									for(int arg_pos=2; arg_pos<args.size(); arg_pos++){
										Object var=resolve_var(args.get(arg_pos));
										type_check(var, numeric, args.get(0));
										Double dvar=(Double)var;
										if(dvar>max)max=dvar;
									}
									vars.put(args.get(1), max);
									break;
								}
								case "MIN": //min among args, return max double with 0 arg
								{
									args_size_check(args,1,Integer.MAX_VALUE,args.get(0));
									Double min=Double.MAX_VALUE;
									for(int arg_pos=2; arg_pos<args.size(); arg_pos++){
										Object var=resolve_var(args.get(arg_pos));
										type_check(var, numeric, args.get(0));
										Double dvar=(Double)var;
										if(dvar<min)min=dvar;
									}
									vars.put(args.get(1), min);
									break;
								}
								case "FLOOR":
								{
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									vars.put(args.get(1), Math.floor((Double)var2));
									break;
								}
								//map and list commands
								case "MAP_CREATE":
								{
									args_size_check(args,1,1,args.get(0));
									vars.put(args.get(1), new StringMapView(new HashMap<>()));
									break;
								}
								case "MAP_COPY":
								{
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, StringMapView.class, args.get(0));
									vars.put(args.get(1), new StringMapView(new HashMap<>(((StringMapView)var2).unwrap())));
									break;
								}
								case "MAP_FROM_STRING":
								{ //format: result string
									//empty strings become empty map for purpose of this command
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, String.class, args.get(0));
									HashMap<String,Object> map=new HashMap<>();
									if(!var2.equals("")){
										map = new Gson().fromJson(var2.toString(),new TypeToken<HashMap<String,String>>(){}.getType());
									}
									StringMapView smv=new StringMapView(map);
									vars.put(args.get(1), smv);
									break;
								}
								case "MAP_PUT":
								{
									args_size_check(args,3,3,args.get(0));
									Object var1=resolve_var(args.get(1));
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									type_check(var1, StringMapView.class, args.get(0));
									type_check(var2, String.class, args.get(0));
									type_check(var3, String.class, args.get(0));
									((StringMapView) var1).put((String)var2, (String)var3);
									break;
								}
								case "MAP_PUT_DOUBLE":
								{
									args_size_check(args,3,3,args.get(0));
									Object var1=resolve_var(args.get(1));
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									type_check(var1, StringMapView.class, args.get(0));
									type_check(var2, String.class, args.get(0));
									type_check(var3, Double.class, args.get(0));
									((StringMapView) var1).put((String)var2, String.valueOf((Double)var3));
									break;
								}
								case "MAP_ADD_DOUBLE":
								{
									args_size_check(args,3,3,args.get(0));
									Object var1=resolve_var(args.get(1));
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									type_check(var1, StringMapView.class, args.get(0));
									type_check(var2, String.class, args.get(0));
									type_check(var3, Double.class, args.get(0));
									double original=0;
									StringMapView smv1=(StringMapView) var1;
									String str2=(String)var2;
									if(smv1.containsKey(str2)){
										String ostr=smv1.get(str2);
										if(!isFloat(ostr)) throw new IllegalArgumentException("The map key "+str2+" does not give a double value in "+args.get(0)+"!");
										original=Double.parseDouble(ostr);
									}
									smv1.put(str2, String.valueOf(original+(Double)var3));
									break;
								}
								case "MAP_GET":
								{ //format: result map key [default]
									args_size_check(args,3,4,args.get(0));
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									String default_val="";
									type_check(var2, StringMapView.class, args.get(0));
									type_check(var3, String.class, args.get(0));
									if(args.size()==5){
										Object var4=resolve_var(args.get(4));
										type_check(var4, String.class, args.get(0));
										default_val=String.valueOf(var4);
									}
									vars.put(args.get(1), ((StringMapView)var2).getOrDefault((String)var3, default_val));
									break;
								}
								case "MAP_REMOVE":
								{
									args_size_check(args,2,2,args.get(0));
									Object var1=resolve_var(args.get(1));
									Object var2=resolve_var(args.get(2));
									type_check(var1, StringMapView.class, args.get(0));
									type_check(var2, String.class, args.get(0));
									((StringMapView) var1).remove((String)var2);
									break;
								}
								case "MAP_UNPACK":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, StringMapView.class, args.get(0));
									StringMapView smv=(StringMapView)var1;
									vars.putAll(smv);
									break;
								}
								case "LIST_CREATE":
								{
									args_size_check(args,2,2,args.get(0));
									try{
										vars.put(args.get(1), new TypedListView(new ArrayList<>(),ElementType.fromString(args.get(2))));
									}
									catch(IllegalArgumentException ex){
										_Log("Datatype "+args.get(2)+" is not recognized for "+args.get(0)+"!");
										throw ex;
									}
									break;
								}
								case "LIST_ADD":
								{ //third arg optional, for type check, format: list val [type]
									args_size_check(args,2,3,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var1;
									Object var2=resolve_var(args.get(2));
									if(args.size()==4){
										try{
											if(tlv.getType()!=ElementType.fromString(args.get(3))){
												throw new VariableTypeException("The list is not of type "+args.get(3)+" in "+args.get(0)+"!");
											}
										}
										catch(IllegalArgumentException ex){
											_Log("Datatype "+args.get(3)+" is not recognized for "+args.get(0)+"!");
											throw ex;
										}
									}
									tlv.add(var2);
									break;
								}
								case "LIST_INSERT":
								{ //fourth arg optional, for type check, format: list val pos [type]
									args_size_check(args,3,4,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var1;
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									if(args.size()==5){
										try{
											if(tlv.getType()!=ElementType.fromString(args.get(4))){
												throw new VariableTypeException("The list is not of type "+args.get(4)+" in "+args.get(0)+"!");
											}
										}
										catch(IllegalArgumentException ex){
											_Log("Datatype "+args.get(4)+" is not recognized for "+args.get(0)+"!");
											throw ex;
										}
									}
									tlv.add(((Double)var3).intValue(),var2);
									break;
								}
								case "LIST_SET":
								{ //fourth arg optional, for type check, format: list val pos [type]
									args_size_check(args,3,4,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var1;
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									if(args.size()==5){
										try{
											if(tlv.getType()!=ElementType.fromString(args.get(4))){
												throw new VariableTypeException("The list is not of type "+args.get(4)+" in "+args.get(0)+"!");
											}
										}
										catch(IllegalArgumentException ex){
											_Log("Datatype "+args.get(4)+" is not recognized for "+args.get(0)+"!");
											throw ex;
										}
									}
									tlv.set(((Double)var3).intValue(),var2);
									break;
								}
								case "LIST_GET":
								{ //fourth arg optional, for type check, format: ret list pos [type]
									args_size_check(args,3,4,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var2;
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									if(args.size()==5){
										try{
											if(tlv.getType()!=ElementType.fromString(args.get(4))){
												throw new VariableTypeException("The list is not of type "+args.get(4)+" in "+args.get(0)+"!");
											}
										}
										catch(IllegalArgumentException ex){
											_Log("Datatype "+args.get(4)+" is not recognized for "+args.get(0)+"!");
											throw ex;
										}
									}
									vars.put(args.get(1), tlv.get(((Double)var3).intValue()));
									break;
								}
								case "LIST_REMOVE":
								{ // list pos
									args_size_check(args,2,2,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var1;
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									tlv.remove(((Double)var2).intValue());
									break;
								}
								case "LIST_SIZE": case "LIST_LENGTH":
								{ //format: ret list
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, TypedListView.class, args.get(0));
									TypedListView tlv=(TypedListView)var2;
									vars.put(args.get(1), Double.valueOf(tlv.size()));
									break;
								}
								case "NUM_LIST_FROM_STRING":
								{ //format: result string
									//empty string becomes empty list
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, String.class, args.get(0));
									ArrayList<Double> ds=new ArrayList<>();
									if(!var2.equals("")){
										ds=new Gson().fromJson(var2.toString(),new TypeToken<ArrayList<Double>>(){}.getType());
									}
									TypedListView tlv=new TypedListView(ds, ElementType.DOUBLE);
									vars.put(args.get(1), tlv);
									break;
								}
								case "MAP_LIST_FROM_STRING":
								{ //format: result string
									//empty string becomes empty list
									args_size_check(args,2,2,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, String.class, args.get(0));
									ArrayList<HashMap<String, String>> ms=new ArrayList<>();
									if(!var2.equals("")){
										ms=new Gson().fromJson(var2.toString(),new TypeToken<ArrayList<HashMap<String,String>>>(){}.getType());
									}
									TypedListView tlv=new TypedListView(ms, ElementType.MAP);
									vars.put(args.get(1), tlv);
									break;
								}
								//logic commands
								case "COMPARE": //compare two numbers
								{
									args_size_check(args,3,3,args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									Object var3=resolve_var(args.get(3));
									type_check(var3, numeric, args.get(0));
									vars.put(args.get(1), Double.valueOf(Double.compare((Double)var2 , (Double) var3)));
									break;
								}
								case "JUMP": //jump to a line number or label
								{
									args_size_check(args,1,1,args.get(0));
									//use jump dict
									if(jump_targets.containsKey(line_pos)){
										line_pos=jump_targets.get(line_pos)-1;
									}
									else{
										_Log("Error: "+args.get(0)+" target line not saved in memory.");
									}
									break;
								}
								case "JUMP_IF":  //jump if boolean expression satisfied
								{
									args_size_check(args,4,4,args.get(0));
									//use jump dict
									Object var3=resolve_var(args.get(3));
									Object var4=resolve_var(args.get(4));
									Boolean result= eval_bool(args.get(2),var3,var4);
									if(result==null){
										_Log("Eval bool failed in "+args.get(0));
										return;
									}
									else if(!result){
										break;
									}
									if(jump_targets.containsKey(line_pos)){
										line_pos=jump_targets.get(line_pos)-1;
									}
									else{
										_Log("Error: "+args.get(0)+" target line not saved in memory.");
									}
									break;
								}
								case "IF":  //basically jump to else/end if when not satisfied
								case "WHILE": //same, jump to line immediately after end_while
								{
									args_size_check(args,3,3,args.get(0));
									//use jump dict
									Object var2=resolve_var(args.get(2));
									Object var3=resolve_var(args.get(3));
									Boolean result= eval_bool(args.get(1),var2,var3);
									if(result==null){
										_Log("Eval bool failed in "+args.get(0));
										return;
									}
									else if(result){
										break;
									}
									if(jump_targets.containsKey(line_pos)){
										line_pos=jump_targets.get(line_pos)-1;
									}
									else{
										_Log("Error: "+args.get(0)+" target line not saved in memory.");
									}
									break;
								}
								case "ELSE":  //jump to end if
								case "END_WHILE": //jump to while
								{
									args_size_check(args,0,0,args.get(0));
									//use jump dict
									if(jump_targets.containsKey(line_pos)){
										line_pos=jump_targets.get(line_pos)-1;
									}
									else{
										_Log("Error: "+args.get(0)+" target line not saved in memory.");
									}
									break;
								}
								//logic labels (NOP)
								case "LABEL": case "END_IF":
								break;
								//game state interaction commands
								case "ATTACK":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									_Attack_generic(actor, target, Math.floor((Double)var1), new HashMap<String, Object>());
									break;
								}
								case "BLOCK_SELF":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									_Apply_block_generic(actor, actor, Math.floor((Double)var1), new HashMap<String, Object>());
									break;
								}
								case "INFLICT_EFFECT":
								{
									args_size_check(args,2,2,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, String.class, args.get(0));
									Object var2=resolve_var(args.get(2));
									type_check(var2, numeric, args.get(0));
									_Inflict_effect_generic(target, (String)var1, String.valueOf(var2));
									break;
								}
								case "SET_AP":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									ap = Math.floor((Double)var1);
									break;
								}
								case "DISCARD_CARD_ID": case "DISCARD_BY_ID":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									_Discard_by_id((Double)var1, true);
									break;
								}
								case "DRAW_CARD_ID": case "DRAW_BY_ID":
								{
									args_size_check(args,1,1,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									_Draw_card((Double)var1);
									break;
								}
								case "MOVE_CARD_ID": case "MOVE_BY_ID":
								{   //syntax: id target [source]
									//WARNING: on draw/discard effects are not triggered, use DRAW or DISCARD commands instead
									args_size_check(args,2,3,args.get(0));
									Object var1=resolve_var(args.get(1));
									type_check(var1, numeric, args.get(0));
									TypedListView tlv=requireTypedList(args.get(2), ElementType.DOUBLE, args.get(0));
									List<Double> target=tlv.asDoubleList();
									List<Double> source=null; //special value for not checking source
									if(args.size()==4){
										TypedListView tlvsource=requireTypedList(args.get(3), ElementType.DOUBLE, args.get(0));
										source=tlvsource.asDoubleList();
									}
									_Move_card((Double)var1, target, source);
									break;
								}
								case "IDLIST_TO_CARDLIST": //snapshot, no write back (only mutate the maps)!
								{
									args_size_check(args,2,2,args.get(0));
									TypedListView tlv=requireTypedList(args.get(2),ElementType.DOUBLE, args.get(0));
									ArrayList<HashMap<String, Object>> temp_hand_list=new ArrayList<>();
									for(Object id:tlv){
										temp_hand_list.add(card_list.get(((Double)id).intValue()));}
									vars.put(args.get(1), new TypedListView(temp_hand_list, ElementType.MAP));
									break;
								}
								//unknown commands
								default:
								throw new IllegalArgumentException("Unknown command "+args.get(0)+"!!");
							}
						}
					}
					line_pos++;
					steps_runned++;
					if(steps_runned>steps_limit){
						_Log("Warning, script running for over "+String.valueOf(steps_limit)+" steps, terminated now");
						return;
					}
				}
			}
			catch(VariableTypeException ex){
				_Log(ex.getMessage()+"(line "+String.valueOf(line_pos)+")");
			}
			catch(ArgsCountException ex){
				_Log(ex.getMessage()+"(line "+String.valueOf(line_pos)+")");
			}
			catch(IllegalArgumentException ex){
				_Log(ex.getMessage()+"(line "+String.valueOf(line_pos)+")");
			}
			catch(IllegalStateException ex){
				_Log(ex.getMessage()+"(line "+String.valueOf(line_pos)+")");
			}
			catch(RuntimeException ex){
				_Log("Some "+ex.getClass().getName()+" occured on line "+String.valueOf(line_pos)+". Message:"+ex.getMessage());
			}
		}
		//more commands coming soon
	}
	
	public enum ElementType {
		STRING(String.class, "string", "str", "text"),
		DOUBLE(Double.class, "double", "float", "number", "num"),
		MAP(Interpreter.StringMapView.class, "map", "hashmap", "dict", "object");
		
		private final Class<?> visibleClass;
		private final Set<String> aliases;   //  all lowercase
		
		ElementType(Class<?> visibleClass, String... aliases) {
			this.visibleClass = visibleClass;
			this.aliases = new HashSet<>(Arrays.asList(aliases));
		}
		
		public Class<?> visibleClass() {
			return visibleClass;
		}
		
		/** from string to ElementType, case insensitive, throws exception when not found */
		public static ElementType fromString(String keyword) {
			if (keyword == null) {
				throw new IllegalArgumentException("Datatype keyword is null");
			}
			String k = keyword.toLowerCase(Locale.ROOT);
			for (ElementType t : values()) {
				if (t.aliases.contains(k)) return t;
			}
			throw new IllegalArgumentException(
			"Unrecognized datatype keyword: '" + keyword + "'");
		}
		
		
		/** check whether the raw item is of the correct type */
		public boolean acceptsRaw(Object raw) {
			if (raw == null) return true;
			switch (this) {
				case STRING: return raw instanceof String;
				case DOUBLE: return raw instanceof Double
				|| raw instanceof Number
				|| (raw instanceof String && isParseableDouble((String) raw));
				case MAP:    return raw instanceof Map;
				default:     return false;
			}
		}
		
		private static boolean isParseableDouble(String s) {
			try { Double.parseDouble(s); return true; }
			catch (NumberFormatException e) { return false; }
		}
	}
	
	{
	}
	
	
	public void _Evaluate_cost(final HashMap<String, Object> _card) {
		double base_cost=0;
		double cost=0;
		if (_card.containsKey("cost")) {
			base_cost = Double.parseDouble(_card.get("cost").toString());
			cost = base_cost;
			if (_card.containsKey("cost_change_for_turn")) {
				cost = cost + Double.parseDouble(_card.get("cost_change_for_turn").toString());
			}
			_card.put("turn_cost", String.valueOf((long)(cost)));
			if (_card.containsKey("cost_change_until_play")) {
				cost = cost + Double.parseDouble(_card.get("cost_change_until_play").toString());
			}
			if(cost<0&&cost<base_cost){
				cost=(base_cost>=0?0:base_cost);
			}
			_card.put("final_cost", String.valueOf((long)(cost)));
		}
	}
	
	
	public void _Update_cards() {
		for(HashMap<String,Object> card:card_list){
			_Evaluate_cost(card);
		}
	}
	
	
	public void _Check_game_state() {
		_Check_any_death();
		_Update_cards();
	}
	
	
	public void _MewEnvironment_asd() {
		throw new RuntimeException();
	}
	void RunMewtype(HashMap<String,Object> actor, 
	HashMap<String,Object> target,
	HashMap<String, Object> card, String mewProgram, HashMap<String, Object> vars){
		mewenvironment.resetupEnvironment(actor,target,card,entity_list,card_list,hand_id_list,library_id_list,discard_id_list);
		mewenvironment.injectedVariables.putAll(mewenvironment.injectSpecialStringVariables(vars));
		MewtypeInterpreter interpreter = new MewtypeInterpreter(mewenvironment);
		MewtypeProgram program = interpreter.interpret(mewProgram, mewenvironment);
		MewVariable result = program.run(mewenvironment);
		_Log("Script result:"+result.stringifyValue());
	}
	
	public class SketchwareMewEnvironment extends MewEnvironment {
		HashMap<String,Object> actor;
		HashMap<String,Object> target;
		HashMap<String, Object> card; //may be null if non-card
		java.util.List<HashMap<String, Object>> entitiesList;
		java.util.List<HashMap<String, Object>> cardsList;
		java.util.List<Double> handsIdList; 
		java.util.List<Double> libraryIdList; 
		java.util.List<Double> discardIdList; 
		// Cache of the maps and the lists
		MewVariable _actorMap;
		MewVariable _targetMap;
		MewVariable _cardMap;
		MewVariable _entitiesList;
		MewVariable _cardsList;
		MewVariable _handIdList;
		MewVariable _libraryIdList;
		MewVariable _discardIdList;
		
		// Add your own custom functions here
		// Example functions:
		void attack(HashMap<String, Object> actor, HashMap<String, Object> target, double value, HashMap<String, Object> extras) {
			//logMessage("attack() is called!");
			_Attack(actor, target, value, extras);
		}
		
		void applyBlock(HashMap<String, Object> actor, HashMap<String, Object> target, double value, HashMap<String, Object> extras) {
			//logMessage("applyBlock() is called!");
			_Apply_block(actor, target, value, extras);
		}
		
		void discardCard(double id, boolean can_trigger_effects) {
			//logMessage("discardCard() is called!");
			_Discard_by_id(id, can_trigger_effects);
		}
		
		void drawCard(double id) {
			_Draw_card(id);
		}
		
		void moveCard(double id, List<Double> target, List<Double> source){
			_Move_card(id, target, source);
		}
		
		
		//deleted constructor with args because I don't want to update its arg list
		public void resetupEnvironment(HashMap<String, Object> _actor, HashMap<String, Object> _target, HashMap<String, Object> _card, java.util.List<HashMap<String, Object>> entitiesList, 
		java.util.List<HashMap<String, Object>> cardsList, java.util.List<Double> handsIdList, java.util.List<Double> libraryIdList, java.util.List<Double> discardIdList) {
			//clean away old variables
			variables = new MewtypeVariableMap();
			sbLog = new StringBuilder();
			escapeLoopFlag = false;
			escapeProgramFlag = false;
			recursiveExecuteStringCap = 0;
			codeBlockDepth = 0;
			totalIterations = 0;
			//bind the variables
			this.actor = _actor;
			this.target = _target;
			this.card = _card;
			this.entitiesList = entitiesList;
			this.cardsList = cardsList;
			this.handsIdList = handsIdList;
			this.libraryIdList = libraryIdList;
			this.discardIdList = discardIdList;
			initEnvironment();
		}
		
		public void initEnvironment() {
			// Add more initialization parameters here
			
			// random = new java.util.Random(); // Set your own random here
			random = randomizer;
			
			// Initialize the maps so that we don't need to initialize them every single time
			MewBridge converter = new MewBridge();
			_actorMap = new MewVariable(MewVariableType.Map, converter.externalMapAny(actor, true)); // The boolean true is the flag for mutability
			_targetMap = new MewVariable(MewVariableType.Map, converter.externalMapAny(target, true));
			_cardMap = new MewVariable(MewVariableType.Map, converter.externalMapAny(card, true));
			_entitiesList = new MewVariable(MewVariableType.List, converter.externalListAny(entitiesList, true)); // Same with maps, the boolean true is for mutability
			_cardsList = new MewVariable(MewVariableType.List, converter.externalListAny(cardsList, true));
			_handIdList = converter.listOfDoubles(handsIdList, true); // mutable now it's a list of ids, and pre-wrapped already
			_libraryIdList = converter.listOfDoubles(libraryIdList, true);
			_discardIdList = converter.listOfDoubles(discardIdList, true);
		}
		
		// Modify existing functions here
		public SketchwareMewEnvironment() {
			super();
			initEnvironment();
		}
		
		@Override
		public Object[] getSerializationObjects() {
			return new Object[]{ BattleActivity.this };
		}
		
		@Override
		public void showMessage(String message) {
			SketchwareUtil.showMessage(getApplicationContext(), message);
			logMessage(message);
		}
		
		@Override
		int getMaxStackDepth() {
			// Change the maximum stack depth if you want
			return 100;
		}
		
		@Override
		long getForeverLoopDetectionThreshold() {
			// Change this to change the maximum number of iterations a while loop is allowed to run
			return (long)1000000;
		}
		
		@Override
		public MewVariable getConstantValues(String name) {
			switch (name.toUpperCase().trim()) {
				// Set up your custom constant values like this
				case "ACTOR_NAME":
				if (actor != null) {
					if (actor.containsKey("name")) {
						return createStringVariable(actor.get("name").toString());
					} else {
						return createStringVariable("An unnamed actor");
					}
				} else {
					return createStringVariable("No actor");
				}
				case "ACTOR_BLOCK":
				if (actor != null) {
					if (actor.containsKey("block")) {
						try {
							return createDoubleVariable(Double.parseDouble(actor.get("block").toString()));
						} catch (Exception e) {
							return createErrorVariable("Cannot parse actor's block value as a double", e);
						}
					} else {
						return createDoubleVariable(0d);
					}
				} else {
					return createDoubleVariable(0d);
				}
				case "ACTOR":
				return _actorMap;
				case "TARGET":
				return _targetMap;
				case "CARD":
				return _cardMap;
				case "ENTITIES":
				return _entitiesList;
				case "CARDS":
				case "ALL_CARDS":
				return _cardsList;
				case "CARDS_ID_IN_HAND":
				return _handIdList;
				case "CARDS_ID_IN_LIBRARY":
				return _libraryIdList;
				case "CARDS_ID_IN_DISCARD":
				return _discardIdList;
				case "AP":
				return createDoubleVariable(ap);
				// Default: inherit
				default:
				return super.getConstantValues(name);
			}
		}
		
		@Override
		public HashMap<String, MewVariable> injectSpecialVariables() {
			HashMap<String, MewVariable> vars = new HashMap<>();
			
			// Register your own special variables here
			vars.put("EMU_OTORI", createStringVariable("WONDERHOY"));
			
			return vars;
		}
		
		public HashMap<String, MewVariable> injectSpecialStringVariables(Map<String,Object> strvars) {
			HashMap<String, MewVariable> vars = new HashMap<>();
			for(Map.Entry<String, Object> entry: strvars.entrySet()){
				vars.put(entry.getKey(), createStringVariable(entry.getValue().toString()));
			}
			return vars;
		}
		
		@Override
		public ArrayList<Class<?>> getInjectionClasses() {
			ArrayList<Class<?>> classes = new ArrayList<>();
			
			// Register your own verbs here
			classes.add(AttackVerb.class);
			classes.add(BlockSelfVerb.class);
			classes.add(DiscardCardVerb.class);
			classes.add(SetApVerb.class);
			classes.add(DrawCardVerb.class);
			classes.add(MoveCardVerb.class);
			return classes;
		}
		
		@Override
		public void onBeginInitClassList(MewtypeInterpreter.MewtypeClassList classList) {
			super.onBeginInitClassList(classList);
			
			// Do your own configuration here (remove classes, rename classes etc.)
		}
		
		@Override
		public void onEndInitClassList(MewtypeInterpreter.MewtypeClassList classList) {
			super.onEndInitClassList(classList);
			
			// Do your own configuration here (remove classes, rename classes etc.)
		}
		
		@Override
		public void logMessage(String message) {
			sbLog.append("Script output:");
			sbLog.append(message);
			sbLog.append("\n");
		}
		
		// Helper functions for u
		public MewVariable createStringVariable(String value) {
			return new MewVariable(MewVariableType.String, value);
		}
		public MewVariable createIntVariable(int value) {
			return new MewVariable(MewVariableType.Integer, value);
		}
		public MewVariable createDoubleVariable(Double value) {
			return new MewVariable(MewVariableType.Float, value);
		}
		public MewVariable createBoolVariable(boolean value) {
			return new MewVariable(MewVariableType.Bool, value);
		}
		public MewVariable createErrorVariable(String value) {
			return new MewVariable(MewVariableType.Error, new Exception(value));
		}
		public MewVariable createErrorVariable(String value, Exception innerException) {
			return new MewVariable(MewVariableType.Error, new Exception(value, innerException));
		}
		
		@Override
		public String getPlatformName() {
			return "Sketchware";
		}
	}
	
	class AttackVerb extends MewNode {
		@Override
		public String getName() {
			return "Attack";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Attack expects a numeric argument but received one of type " + first.type.name()));
			}
			
			// Do stuff
			double value = Math.floor(first.toDouble());
			env.attack(env.actor, env.target, value, new HashMap<String, Object>());
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 1");
		}
		
		@Override
		public int getMinArgumentCount() { return 1; }
		
		@Override
		public int getMaxArgumentCount() { return 1; }
		
		public AttackVerb() { }
	}
	
	class BlockSelfVerb extends MewNode {
		@Override
		public String getName() {
			return "Block-Self";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Block-Self expects a numeric argument but received one of type " + first.type.name()));
			}
			
			// Do stuff
			double value = Math.floor(first.toDouble());
			env.applyBlock(env.actor, env.target, value, new HashMap<String, Object>());
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 1");
		}
		
		@Override
		public int getMinArgumentCount() { return 1; }
		
		@Override
		public int getMaxArgumentCount() { return 1; }
		
		public BlockSelfVerb() { }
	}
	
	class DiscardCardVerb extends MewNode {
		@Override
		public String getName() {
			return "Discard-Card-By-Id";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Discard-Card-By-Id expects a numeric argument but received one of type " + first.type.name()));
			}
			
			// Do stuff
			double value = Math.floor(first.toDouble());
			env.discardCard(value, true);
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 3");
		}
		
		@Override
		public int getMinArgumentCount() { return 1; }
		
		@Override
		public int getMaxArgumentCount() { return 1; }
		
		public DiscardCardVerb() { }
	}
	
	class SetApVerb extends MewNode {
		@Override
		public String getName() {
			return "Set-Ap";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Set Ap expects a numeric argument but received one of type " + first.type.name()));
			}
			
			// Do stuff
			double value = Math.floor(first.toDouble());
			ap = value;
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 3");
		}
		
		@Override
		public int getMinArgumentCount() { return 1; }
		
		@Override
		public int getMaxArgumentCount() { return 1; }
		
		public SetApVerb() { }
	}
	
	class DrawCardVerb extends MewNode {
		@Override
		public String getName() {
			return "Draw-Card-By-Id";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Draw-Card-By-Id expects a numeric argument but received one of type " + first.type.name()));
			}
			
			// Do stuff
			double value = Math.floor(first.toDouble());
			env.drawCard(value);
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 5");
		}
		
		@Override
		public int getMinArgumentCount() { return 1; }
		
		@Override
		public int getMaxArgumentCount() { return 1; }
		
		public DrawCardVerb() { }
	}
	
	class MoveCardVerb extends MewNode {
		@Override
		public String getName() {
			return "Move-Card-By-Id";
		}
		
		@Override
		public MewVariable evaluate(MewEnvironment environment) {
			// Get and evaluate first argument
			MewVariable first = nodes.get(0).evaluate(environment);
			MewVariable second = nodes.get(1).evaluate(environment);
			// Environment validation (just in case)
			// We run on SketchwareMewEnvironment so we reject everything else
			SketchwareMewEnvironment env;
			if (environment instanceof SketchwareMewEnvironment) {
				env = (SketchwareMewEnvironment)environment;
			} else {
				return new MewVariable(MewVariableType.Error, new Exception("Wrong platform!"));
			}
			
			// Type validation
			if (!first.isNumeric()) {
				// Return error
				return new MewVariable(MewVariableType.Error, new Exception("Move-Card-By-Id expects a numeric argument but received one of type " + first.type.name()));
			}
			if (!(second.type==MewVariableType.List)) {
				return new MewVariable(MewVariableType.Error, new Exception("Move-Card-By-Id expects a list argument but received one of type " + second.type.name()));
			}
			else if(!(second.value instanceof MewExternalList)){
				return new MewVariable(MewVariableType.Error, new Exception("Move-Card-By-Id expects an external list argument but received one list of type " + second.value.getClass().toString()));
			}
			
			// Do stuff
			// moveCard does in place mutation to simulate card zones
			// therefore we accept external lists only
			double id = Math.floor(first.toDouble());
			List<Double> target=((MewExternalList)(second.value)).backing;
			if(nodes.size()==3){
				MewVariable third = nodes.get(2).evaluate(environment);
				if (!(third.type==MewVariableType.List)) {
					return new MewVariable(MewVariableType.Error, new Exception("Move-Card-By-Id expects a list argument but received one of type " + third.type.name()));
				}
				else if(!(third.value instanceof MewExternalList)){
					return new MewVariable(MewVariableType.Error, new Exception("Move-Card-By-Id expects an external list argument but received one list of type " + third.value.getClass().toString()));
				}
				List<Double> source=((MewExternalList)(third.value)).backing;
				env.moveCard(id, target, source);
			}else{
				env.moveCard(id,target,null);
			}
			
			// Return something
			return new MewVariable(MewVariableType.Null, "Placeholder 5");
		}
		
		@Override
		public int getMinArgumentCount() { return 2; }
		
		@Override
		public int getMaxArgumentCount() { return 3; }
		
		public MoveCardVerb() { }
	}
	
	{
	}
	
	
	public void _Run_script_type(final HashMap<String, Object> _actor, final HashMap<String, Object> _target, final HashMap<String, Object> _card, final String _script, final String _language, final HashMap<String, Object> _var) {
		_Log("Running script:"+_script);
		switch(_language.toLowerCase()){
			case "mew": case "mewtype":
			RunMewtype(_actor, _target,_card,_script,_var);
			break;
			case "jumpscript": default:
			//use the default language
			new Interpreter(_actor, _target, _card, _script, _var).run();
			break;
		}
	}
	
	
	public void _Inflict_effect(final HashMap<String, Object> _Target, final String _name, final String _duration_string) {
		_Inflict_effect_generic(_Target, _name, _duration_string);
	}
	
	void _Inflict_effect_generic(Map<String,Object> _Target, String _name, String _duration_string){
		if (!_Target.containsKey(_name) || (Double.parseDouble(_duration_string) > Double.parseDouble(_Target.get(_name).toString()))) {
			_Target.put(_name, _duration_string);
		}
	}
	
	
	public String _Action_to_description(final HashMap<String, Object> _action) {
		String results="";
		for (Map.Entry<String, Object> entry : _action.entrySet()) {
			switch(entry.getKey()){
				case "attack":
				results+="🗡️"+entry.getValue().toString();
				break;
				case "block_self":
				results+="🛡️"+entry.getValue().toString();
				break;
				case "block_ally":
				results+="🛡️↔️"+entry.getValue().toString();
				break;
				default:
				results+="❓";
				break;
			}
		}
		return results;
	}
	
	
	public void _Apply_field_cards_turn_start() {
		ArrayList<Double> field_temp=new ArrayList<>(field_id_list);
		for(double _card_id:field_temp){
			HashMap<String, Object> _card=card_list.get((int)_card_id);
			apply_card_map = new HashMap<>();
			if (_card.containsKey("field_turn_start_give_ap")) {
				ap = ap + Double.parseDouble(_card.get("field_turn_start_give_ap").toString());
			}
			if (_card.containsKey("field_turn_start_give_card")) {
				for(int _repeat82 = 0; _repeat82 < (int)(Double.parseDouble(_card.get("field_turn_start_give_card").toString())); _repeat82++) {
					_Draw_card();
				}
			}
			if (_card.containsKey("field_turn_start_script")) {
				String language=_card.getOrDefault("script_language","").toString();
				_Run_script_type(player, new HashMap<String, Object>(), _card, _card.get("field_turn_start_script").toString(), language, new HashMap<String,Object>());
			}
		}
		_Check_game_state();
		_Refresh_views();
	}
	
	
	public void _Apply_field_cards_turn_end() {
		ArrayList<Double> field_temp=new ArrayList<>(field_id_list);
		for(double _card_id:field_temp){
			HashMap<String, Object> _card=card_list.get((int)_card_id);
			apply_card_map = new HashMap<>();
			if (_card.containsKey("remaining_field_time")) {
				_card.put("remaining_field_time", String.valueOf((long)(Double.parseDouble(_card.get("remaining_field_time").toString()) + -1)));
			} else {
				_card.put("remaining_field_time", "0");
			}
			if (Double.parseDouble(_card.get("remaining_field_time").toString()) > 0) {
				if (_card.containsKey("field_turn_end_script")) {
					String language=_card.getOrDefault("script_language","").toString();
					_Run_script_type(player, new HashMap<String, Object>(), _card, _card.get("field_turn_end_script").toString(), language, new HashMap<String,Object>());
				}
			} else {
				_card.remove("remaining_field_time");
				field_id_list.remove(_card_id);
				if (_card.containsKey("on_field_destination")) {
					if (_card.get("on_field_destination").toString().equals("hand")) {
						hand_id_list.add(Double.valueOf(_card_id));
					}
					if (_card.get("on_field_destination").toString().equals("discard")) {
						discard_id_list.add(Double.valueOf(_card_id));
					}
				} else {
					discard_id_list.add(Double.valueOf(_card_id));
				}
			}
		}
		_Check_game_state();
		_Refresh_views();
	}
	
	public class Entity_viewAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Entity_viewAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.entity, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final ProgressBar health_bar = _view.findViewById(R.id.health_bar);
			
			map_placeholder = _data.get((int)_position);
			if (map_placeholder.containsKey("name")) {
				string_placeholder = map_placeholder.get("name").toString();
			} else {
				string_placeholder = "Unnamed";
			}
			String temp_effects="";
			for(Map.Entry<String,Object>en:durations_map.entrySet()){
				if(map_placeholder.containsKey(en.getKey())){
					temp_effects+=en.getValue().toString()+map_placeholder.get(en.getKey()).toString();
				}
			}
			if(!temp_effects.equals("")){
				string_placeholder+="("+temp_effects+")";
			}
			if (map_placeholder.containsKey("health")) {
				string_placeholder = string_placeholder.concat("\n");
				string_placeholder = string_placeholder.concat("❤️".concat(map_placeholder.get("health").toString()));
				if (map_placeholder.containsKey("block")) {
					if (Double.parseDouble(map_placeholder.get("block").toString()) > 0) {
						string_placeholder = string_placeholder.concat("(+".concat(map_placeholder.get("block").toString().concat(")")));
					} else {
						if (Double.parseDouble(map_placeholder.get("block").toString()) < 0) {
							string_placeholder = string_placeholder.concat("(".concat(map_placeholder.get("block").toString().concat(")")));
						}
					}
				}
				if (map_placeholder.containsKey("max_health")) {
					health_bar.setIndeterminate(false);
					string_placeholder = string_placeholder.concat("/".concat(map_placeholder.get("max_health").toString()));
					health_bar.setMax((int)Double.parseDouble(map_placeholder.get("max_health").toString()));
					health_bar.setProgress((int)Double.parseDouble(map_placeholder.get("health").toString()));
				} else {
					health_bar.setIndeterminate(true);
				}
				String group=(player.containsKey("group")?player.get("group").toString():"No group");
				String group2=(map_placeholder.containsKey("group")?map_placeholder.get("group").toString():null);
				boolean is_self=(player==map_placeholder);
				boolean is_ally=is_self ||(group.equals(group2));
				android.graphics.drawable.Drawable pd= health_bar.getProgressDrawable().mutate();
				
				if (is_ally) {pd.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);}
				else{pd.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);}
				health_bar.setProgressDrawable(pd);
				string_placeholder = string_placeholder.concat("\n");
			}
			string_placeholder = string_placeholder.concat("\n".concat(_Action_to_description(_get_upcoming_action(map_placeholder))));
			textview1.setText(string_placeholder);
			
			return _view;
		}
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
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.card, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			map_placeholder = _data.get((int)_position);
			if (_position == selected_card_pos) {
				textview1.setTypeface(Typeface.DEFAULT, 1);
			} else {
				textview1.setTypeface(Typeface.DEFAULT, 0);
			}
			if (map_placeholder.containsKey("name")) {
				string_placeholder = map_placeholder.get("name").toString();
			} else {
				string_placeholder = "Unnamed card";
			}
			if (map_placeholder.containsKey("final_cost")) {
				string_placeholder = string_placeholder.concat("(".concat(map_placeholder.get("final_cost").toString().concat(")")));
			} else {
				
			}
			textview1.setText(string_placeholder);
			
			return _view;
		}
	}
}