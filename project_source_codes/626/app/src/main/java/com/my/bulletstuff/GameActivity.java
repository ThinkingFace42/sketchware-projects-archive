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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.SharedPreferences;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.ClipData;
import android.content.ClipboardManager;

public class GameActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private HashMap<String, Object> UselessMap = new HashMap<>();
	private HashMap<String, Object> Translations = new HashMap<>();
	private String trans_placeholder = "";
	
	private LinearLayout l2;
	private LinearLayout l1;
	private LinearLayout hud_large_box;
	private LinearLayout upgrade_box;
	private LinearLayout gui_top_box;
	private LinearLayout gui_filler_box;
	private LinearLayout movement_box;
	private TextView info_text;
	private Button upgrade_btn;
	private Button pause_btn;
	private Button up_btn;
	private LinearLayout movement_box2;
	private Button down_btn;
	private Button left_btn;
	private Button center_btn;
	private Button right_btn;
	private TextView choose_text;
	private LinearLayout upgrade_select;
	
	private TimerTask Main_clock;
	private TimerTask Layout_clock;
	private AlertDialog.Builder tutorial_dialog;
	private AlertDialog.Builder exit_dialog;
	private AlertDialog.Builder log_dialog;
	private Calendar Cal = Calendar.getInstance();
	private SharedPreferences upgrades;
	private SharedPreferences decks;
	private SharedPreferences player;
	private SharedPreferences trans;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.game);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		l2 = (LinearLayout) findViewById(R.id.l2);
		l1 = (LinearLayout) findViewById(R.id.l1);
		hud_large_box = (LinearLayout) findViewById(R.id.hud_large_box);
		upgrade_box = (LinearLayout) findViewById(R.id.upgrade_box);
		gui_top_box = (LinearLayout) findViewById(R.id.gui_top_box);
		gui_filler_box = (LinearLayout) findViewById(R.id.gui_filler_box);
		movement_box = (LinearLayout) findViewById(R.id.movement_box);
		info_text = (TextView) findViewById(R.id.info_text);
		upgrade_btn = (Button) findViewById(R.id.upgrade_btn);
		pause_btn = (Button) findViewById(R.id.pause_btn);
		up_btn = (Button) findViewById(R.id.up_btn);
		movement_box2 = (LinearLayout) findViewById(R.id.movement_box2);
		down_btn = (Button) findViewById(R.id.down_btn);
		left_btn = (Button) findViewById(R.id.left_btn);
		center_btn = (Button) findViewById(R.id.center_btn);
		right_btn = (Button) findViewById(R.id.right_btn);
		choose_text = (TextView) findViewById(R.id.choose_text);
		upgrade_select = (LinearLayout) findViewById(R.id.upgrade_select);
		tutorial_dialog = new AlertDialog.Builder(this);
		exit_dialog = new AlertDialog.Builder(this);
		log_dialog = new AlertDialog.Builder(this);
		upgrades = getSharedPreferences("upgrades", Activity.MODE_PRIVATE);
		decks = getSharedPreferences("decks", Activity.MODE_PRIVATE);
		player = getSharedPreferences("player", Activity.MODE_PRIVATE);
		trans = getSharedPreferences("translations", Activity.MODE_PRIVATE);
		
		upgrade_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if(upgrade_box.getVisibility()==View.INVISIBLE){
					paused=true;
					upgrade_box.setVisibility(View.VISIBLE);
					if(tutorials.contains("upgrade"))_tutorial_upgrade();
				}
				else{
					paused=false;
					upgrade_box.setVisibility(View.INVISIBLE);
				}
				ShowUpgrade();
			}
		});
		
		pause_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				paused=!paused;
				int i=0;
				int j=0;
				while(i<l1.getChildCount()){
					View v=l1.getChildAt(i);
					if(v.getVisibility()==View.VISIBLE){
						i++;}
					else{l1.removeView(v);
						j++;}
				}
				if(j>0){
					SketchwareUtil.showMessage(getApplicationContext(), String.valueOf(j)+" views removed");}
			}});
		pause_btn.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View _view){
				log_dialog.setTitle("Game log");
				String m="";
				for(String log:gobj.log){
					m+=log+"\n";
				}
				log_dialog.setMessage(m);
				log_dialog.create().show();
				log_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				return true;
			}
		});
		
		up_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (false) {
					((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", "3 is a holy number."));
				}
			}
		});
		
		down_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		left_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		center_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (false) {
					UselessMap = new Gson().fromJson("{}", new TypeToken<HashMap<String, Object>>(){}.getType());
					log_dialog.setTitle(new Gson().toJson(UselessMap));
					gobj.player.forcefield++;
					gobj.room_cleared=true;
					gobj.OnRoomClear();
					SketchwareUtil.showMessage(getApplicationContext(), "🌚");
				}
				if(gobj.room_cleared)gobj.player.y=gobj.upper_bound;
			}
		});
		
		right_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		choose_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(),gobj.upgrades_owned.toString());
			}
		});
	}
	private void initializeLogic() {
		if (trans.getString(player.getString("language", ""), "").equals("")) {
			SketchwareUtil.showMessage(getApplicationContext(), "Translations not found!");
		}
		else {
			Translations = new Gson().fromJson(trans.getString(player.getString("language", ""), ""), new TypeToken<HashMap<String, Object>>(){}.getType());
			choose_text.setText(translate("choose_upgrade_text"));
		}
		_load_upgrades_info();
		gobj.timestamp=new SimpleDateFormat("[HH:mm:ss]").format(Cal.getTime());
		//gobj.log.add("Three is a holy number.");
		//gobj.log.add("Why is this the case?");
		//load extra keys
		Bundle extras=
		getIntent().getExtras();
		if (extras!=null){
			if(extras.containsKey("tutorial")&&extras.getString("tutorial","1").equals("1")){
				tutorials.add("general");
				tutorials.add("upgrade");
				tutorials.add("next");
			}
			if(extras.containsKey("seed")){
				String seed=extras.getString("seed","");
				if(!seed.equals("")){
					int seed2=isInt(seed)?Integer.parseInt(seed):seed.hashCode(); gobj.random.setSeed(seed2);
					gobj.log.add(gobj.timestamp+"Seed set to "+String.valueOf(seed2)+".");
				}
			}
		}
		//l2 is the all encompassing layout, l1 is the layout for sprites
		//Init
		final int large_n=10000000;
		l1.setLayoutParams(new LinearLayout.LayoutParams(large_n,large_n));
		l1.setPadding(0,0,0,0);
		screen_w=(int)(SketchwareUtil.getDisplayWidthPixels(getApplicationContext()));
		screen_h=(int)(SketchwareUtil.getDisplayHeightPixels(getApplicationContext()));
		l2.setLayoutParams(new LinearLayout.LayoutParams(screen_w,10*large_n));
		hud_large_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,screen_h));
		Layout_clock = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hud_large_box.setTranslationY((int)(screen_h)-hud_large_box.getHeight()-SketchwareUtil.getLocationY(hud_large_box)+50);
						/*
gobj.log.add("y location of UI is "+String.valueOf(SketchwareUtil.getLocationY(hud_large_box))+".");
gobj.log.add("Screen height is "+String.valueOf(screen_h)+".");
gobj.log.add("UI height is "+String.valueOf(hud_large_box.getHeight())+".");*/
						upgrade_box.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,screen_h-info_text.getHeight()));
						Layout_clock = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										upgrade_box.setTranslationY((int)(screen_h)-upgrade_box.getHeight()-SketchwareUtil.getLocationY(upgrade_box)+50+0*info_text.getHeight());
										upgrade_box.setVisibility(View.INVISIBLE);
										ShowUpgrade();
									}
								});
							}
						};
						_timer.schedule(Layout_clock, (int)(10));
					}
				});
			}
		};
		_timer.schedule(Layout_clock, (int)(100));
		/*
//spawn square array
for(int i=0;i<6;i++){
for(int j=0;j<8;j++){
Sprite s2=new Square();
s2.x=200*i;
s2.y=300*j;
s2.name="Square of "+String.valueOf(i)+","+String.valueOf(j);
s2.colour=getRandom(0,2147483646);
ss.add(s2);}}
gobj.shape_capacity=0;
//spawn enemy array
for(int i=0;i<3;i++){
for(int j=0;j<5;j++){
Shooter s2=new Shooter(100);
s2.x=400*i;
s2.y=450*j;
s2.mode="Quad tank";
s2.photo="new moon face";
s2.name="Enemy of "+String.valueOf(i)+","+String.valueOf(j);
s2.tag="enemy";
s2.colour=0xFFFF0000;
ss.add(s2);}}
//spawn enemy stack
for(int k=0;k<20;k++){
Shooter s2=gobj.getMeleeEnemy();
s2.x=0;
s2.y=100;
s2.name="Enemy #"+String.valueOf(k);
gobj.ss.add(s2);}
gobj.player.attacks.get(0).burn=1;
*/
		gobj.Initialize();
		//gobj.exp=1000;
		Main_clock = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(focused){
							if(!paused){
								//tutorials
								if(tutorials.contains("general"))_tutorial_general();
								if(gobj.room_cleared&&tutorials.contains("next"))_tutorial_next();
								//Movement control
								double sp=gobj.player.move_speed;
								gobj.player.active=true;
								if(up_btn.isPressed()){
									gobj.player.yspeed-=sp;
									gobj.player.active=false;
								}
								else if(down_btn.isPressed()){
									gobj.player.yspeed+=sp;
									gobj.player.active=false;
								}
								if(left_btn.isPressed()){
									gobj.player.xspeed-=sp;
									gobj.player.active=false;
								}
								else if(right_btn.isPressed()){
									gobj.player.xspeed+=sp;
									gobj.player.active=false;
								}
								if(killer!=null&&killer.dispose)killer=null;
								gobj.camera_x=(int)clamp(gobj.left_bound, gobj.player.x-screen_w/2,gobj.right_bound-screen_w);
								gobj.camera_y=(int)clamp(gobj.upper_bound,gobj.player.y-screen_h/2,gobj.lower_bound-screen_h);
								Update(gobj);
							}
							Render(gobj);
							Cal = Calendar.getInstance();
							gobj.timestamp=new SimpleDateFormat("[HH:mm:ss]").format(Cal.getTime());
							//UI rendering
							pause_btn.setText(paused?"▶️":"⏸️");
							info_text.setText(translate("Room")+" #"+String.valueOf(gobj.room_number)+"  "+translate("enemy_left")+":"+String.valueOf (gobj.enemy_count)+"\nEXP:"+String.valueOf(gobj.exp));
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(Main_clock, (int)(100), (int)(40));
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
	public void onPause() {
		super.onPause();
		focused=false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		focused=true;
	}
	
	@Override
	public void onBackPressed() {
		paused=true;
		exit_dialog.setTitle(translate("Exit")+"?");
		exit_dialog.setMessage(translate("exit_text"));
		trans_placeholder=translate("Yes");
		exit_dialog.setPositiveButton(trans_placeholder, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				Main_clock.cancel();
				finish();
			}
		});
		trans_placeholder=translate("No");
		exit_dialog.setNegativeButton(trans_placeholder, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		exit_dialog.create().show();
	}
	private void _asd () {
	}
	//Variable declare
	ArrayList<Sprite>ss=new ArrayList<>();
	GameObject gobj=new GameObject(ss);
	int screen_h=800;
	int screen_w=1200;
	Sprite killer;
	Boolean paused=false;
	Boolean focused=true;
	Boolean dead=false;
	Boolean show_upgrade=false;
	Boolean show_promo=true;
	Boolean show_death_box=true;
	//ArrayList<Button>promo_btns=new ArrayList<>();
	ArrayList<String>tutorials=new ArrayList<>();
	//these load from sharedpreference
	HashMap<String, Integer> upgrade_costs=new HashMap<>();
	HashMap<String, String> upgrade_images=new HashMap<>();
	HashMap<String, String> upgrade_desc=new HashMap<>();
	public class GameObject{
		ArrayList<Sprite> ss=new ArrayList<>();
		ArrayList<Particle> ps=new ArrayList<>();
		int upper_bound=0;
		int left_bound=0;
		int right_bound=1200;
		int lower_bound=2400;
		int enemy_count=0;
		//int shape_capacity=400;
		int room_number=0;
		boolean room_cleared;
		int exp=0;
		ArrayList<String>upgrades=new ArrayList<>();
		ArrayList<String>upgrades_deck=new ArrayList<>();
		ArrayList<String>upgrades_discard=new ArrayList<>();
		int upgrades_options=3;
		HashMap<String, Integer>upgrades_owned=new HashMap<>();
		int camera_x=0;
		int camera_y=0;
		Sprite player;
		String timestamp="[33:33:33]";
		int ticks=0;
		ArrayList<String>log=new ArrayList<>();
		Random random=new Random();
		public GameObject(ArrayList<Sprite> sps){
			ss=sps;
		}
		int getRandom(int lower, int upper){
			return lower+random.nextInt (upper-lower+1);}
		void Update(){
			ticks++;
			enemy_count=0;
			ArrayList<Sprite>temp=new ArrayList<>(ss);
			for(Sprite s:temp){
				s.CheckCollision(ss);
				s.Update(this);
				s.ApplyEffects();
				s.x+=s.xspeed;
				s.y+=s.yspeed;
				//friction
				s.xspeed=((1-s.friction)*s.xspeed);
				s.yspeed=((1-s.friction)*s.yspeed);
				//Regen when max health increases
				if(s.last_max_health<s.max_health){
					s.health+=s.max_health-s.last_max_health;}
				s.last_max_health=s.max_health;
				if(s.health<=0){s.dispose=true;
					if(s.last_hit_by!=null){s.last_hit_by.score+=s.getValue();}
					if(s.type.equals("shooter")){
						String killer="Unknown source";
						int killer_score=0;
						if(s.last_hit_by!=null){killer=s.last_hit_by.name;
							killer_score=s.last_hit_by.score;}
						if(s==gobj.player)gobj.log.add(gobj.timestamp+ s.name+"("+s.score+")"+" was killed by "+killer+"("+killer_score+").");
					}
				}
				if(s.health>s.max_health){s.health=s.max_health;}
				int health_change=s.health- s.last_health;
				if(health_change<0){
					s.last_damaged=0;
					if(s.last_health>0&&s.show_particle)gobj.ps.add(new Particle((int)s.x,(int)s.y,String.valueOf(health_change),0xFFFFFFFF));
				}else{
					if(s.last_health>0&&health_change>0&&s.show_particle)gobj.ps.add(new Particle((int)s.x,(int)s.y,"+"+String.valueOf(health_change),0xFF00FF00));
					
					s.last_damaged++;
					//disabled idle regen
					 //if(s.last_damaged>=800)s.health+=s.max_health/500;
				}
				s.last_health=s.health;
				
				s.health+=s.regeneration;
				s.age++;
				if(s.lifetime>=0){s.lifetime--;
					if(s.lifetime<=0){s.dispose=true;}}
				
				if(!s.wall_kill){
					if(s.x<left_bound+s.r){s.x=left_bound+s.r;
						if(s.wall_bounce&&s.xspeed<0){s.xspeed=-s.xspeed;s.onBounce();}}
					else if(s.x>right_bound-s.r){s.x=right_bound-s.r;
						if(s.wall_bounce&&s.xspeed>0){s.xspeed=-s.xspeed;s.onBounce();}}
					if(s.y<upper_bound+s.r){s.y=upper_bound+s.r;
						if(s.wall_bounce&&s.yspeed<0){s.yspeed=-s.yspeed;s.onBounce();}}
					else if(s.y>lower_bound-s.r){s.y=lower_bound-s.r;
						if(s.wall_bounce&&s.yspeed>0){s.yspeed=-s.yspeed;s.onBounce();}}}
				else{
					if(s.x<left_bound||s.x>right_bound){s.dispose=true;}
					else if(s.y<upper_bound||s.y>lower_bound){s.dispose=true;}}
				if(s.tag.equals("enemy")&&!s.type.equals("bullet")){
					enemy_count++;}}
			if(room_cleared){if(player.y<=upper_bound+player.r+0.1)GenerateRoom();
			}
			else{
				if(enemy_count==0){room_cleared=true;
					OnRoomClear();
				}
			}
			
			for(Particle p:ps){
				p.x+=p.xspeed;
				p.y+=p.yspeed;
				p.lifetime-=1;
			}
			//Spawn(1-(double)enemy_count/shape_capacity);
		}
		
		
		void Initialize(){
			//spawn player
			Shooter s=new Shooter(6000,400,500);
			s.auto_face=true;
			s.name="Playtester";
			s.tag="player";
			s.photo="default image";
			s.colour=0xFFB3E5FC;
			AttackType at=new AttackType();
			//at.damage_mul=2.5;
			at.att_interval=30;
			at.proj_speed=20;
			//at.repeat=3;
			//at.bounce_level=2;
			s.attacks.add(at);
			s.x=(gobj.left_bound+gobj.right_bound)/2;
			s.y=gobj.lower_bound;
			s.detect_range=1400;
			//s.score=1000000;
			ss.add(s);
			player=s;
			
			_load_deck();
			
			UpdateUpgrades();
			log.add(gobj.timestamp+gobj.player.name+" started a game.");
			GenerateRoom();
		}
		
		void UpdateUpgrades(){
			while(upgrades.size()<upgrades_options&&!upgrades_deck.isEmpty()){
				Collections.shuffle(upgrades_deck);
				upgrades.add(upgrades_deck.get(0));
				upgrades_deck.remove(0);
			}
		}
		
		void OnRoomClear(){
			exp+=20;
			player.onRoomClear();
			UpdateUpgrades();
		}
		
		void GenerateRoom(){
			for(Sprite s:ss){
				if(s!=player)s.dispose=true;
			}
			room_number++;
			player.x=(gobj.left_bound+gobj.right_bound)/2;
			player.y=gobj.lower_bound;
			room_cleared=false;
			SpawnThings("");
			//SketchwareUtil.showMessage(getApplicationContext(),"Room #"+String.valueOf(room_number)+"  generated!");
			log.add(gobj.timestamp+"Entered room #"+String.valueOf(room_number)+", HP:"+String.valueOf(player.health)+"/"+String.valueOf(player.max_health));
		}
		
		Shooter getQuadEnemy(){
			Shooter s2=new Shooter(4000,300,200);
			AttackType at=new AttackType();
			at.att_interval=36;
			at.side_proj=1;
			at.back_proj=1;
			s2.attacks.add(at);
			s2.photo="new moon face";
			s2.tag="enemy";
			s2.colour=0xFFFF0000;
			return s2;
		}
		
		Shooter getAimingEnemy(){
			Shooter s2=new Shooter(4000,300,200);
			s2.auto_face=true;
			AttackType at=new AttackType();
			at.att_interval=72;
			s2.attacks.add(at);
			s2.photo="thinking face";
			s2.tag="enemy";
			s2.colour=0xFFFF0000;
			return s2;
		}
		
		Shooter getMeleeEnemy(){
			Shooter s2=new MeleeShooter(4000,300,200);
			s2.auto_face=true;
			s2.photo="relieved face";
			//s2.base_damage=788;
			s2.base_move_speed=1;
			s2.tag="enemy";
			s2.colour=0xFFFF0000;
			return s2;
		}
		
		Shooter getRandomEnemy(){
			int r=getRandom(0,2);
			if(r==0)return getQuadEnemy();
			else if(r==1)return getAimingEnemy();
			else return getMeleeEnemy();
		}
		
		void Spawn(double count){
			/*double chance=count-(int)count;
for(int i=0;i<(int)count;i++)Spawn();
if(Math.random()<chance)Spawn();
}
void Spawn(){
Sprite s2;
int w=right_bound-left_bound;
int h=lower_bound-upper_bound;
int x=getRandom(0, w);
int y=getRandom(0, h);
Boolean pent=(x<=0.6*w && 0.4*w<=x &&y<=0.6*h &&0.4*h<=y);
if(pent&&pentagon_area){
if(getRandom(1,3)<3){
s2=new Pentagon();
s2.name="Pentagon";
}
else if(getRandom(1,6)==3){
s2=new Alpha_Pentagon();
s2.name="Alpha Pentagon";
}
else{s2=new Crusher();
s2.name="Crusher";}
}
else{
if(getRandom(1,3)<3){
s2=new Square();
s2.name="Square";}
else if(getRandom(1,4)<4){
s2=new Triangle();
s2.name="Triangle";}
else{s2=new Pentagon();
s2.name="Pentagon";}}
s2.x=left_bound+x;
s2.y=upper_bound+y;
ss.add(s2);*/
		}
		void SpawnThings(String mode){
			if(mode.equals("original")){SpawnThingsOld();return;}
			for(int i=0;i<2;i++){
				for(int j=0;j<2;j++){
					Sprite s2=new Square();
					s2.x=500*i+350;
					s2.y=1200*j+600;
					s2.name="Square of "+String.valueOf(i)+","+String.valueOf(j);
					s2.colour=random.nextInt();
					ss.add(s2);}}
			//spawn enemies
			double x1=.3+random.nextDouble()*.4;
			double x2=random.nextDouble();
			int xrange=right_bound-left_bound;
			double y1=0.618; //golden ratio
			double y2=random.nextDouble();
			int y3=lower_bound-400;
			int yrange=y3>upper_bound?(y3-upper_bound):0;
			for(int k=0;k<room_number;k++){
				Shooter s2=getRandomEnemy();
				s2.x=(float)(left_bound+xrange* (decimal(x1*k+x2)));
				s2.y=(float)(upper_bound+yrange* (decimal(y1*k+y2)));
				s2.name="Enemy #"+String.valueOf(k);
				ss.add(s2);}
			//spawn loot
			if(room_number%3==0){
				Loot l=new Loot();
				l.tag=player.tag;
				l.x=getRandom(left_bound, right_bound);
				l.y=(upper_bound*3+lower_bound)/4;
				ss.add(l);
			}
		}
		void SpawnThingsOld(){
			for(int i=0;i<2;i++){
				for(int j=0;j<2;j++){
					Sprite s2=new Square();
					s2.x=500*i+350;
					s2.y=1200*j+600;
					s2.name="Square of "+String.valueOf(i)+","+String.valueOf(j);
					s2.colour=random.nextInt();
					ss.add(s2);}}
			//spawn enemies
			for(int k=0;k<room_number;k++){
				Shooter s2=getRandomEnemy();
				s2.x=getRandom(left_bound, right_bound);
				int y=lower_bound-250;
				s2.y=getRandom(upper_bound, y>upper_bound?y:upper_bound);
				s2.name="Enemy #"+String.valueOf(k);
				ss.add(s2);}
			//spawn loot
			if(room_number%3==0){
				Loot l=new Loot();
				l.tag=player.tag;
				l.x=getRandom(left_bound, right_bound);
				l.y=(upper_bound*3+lower_bound)/4;
				ss.add(l);
			}
		}
		String DoUpgrade(int i){
			//returns status
			if(i<0||i>=upgrades.size())return "invalid";
			String s=upgrades.get(i);
			if(exp<getUpgradeCost(s))return "expensive";
			exp-=getUpgradeCost(s);
			upgrades.remove(i);
			upgrades_discard.add(s);
			switch(s.toLowerCase()){
				case "attack up":
				MapAdd(player.upgrades,"Attack",0.3,0);break;
				case "reload":
				MapAdd(player.upgrades,"Reload",0.3,0);
				break;
				case "health up":
				MapAdd(player.upgrades,"Max health",0.3,0);
				break;
				case "swiftness":
				MapAdd(player.upgrades,"Movement speed",0.3,0);
				break;
				case "astronomy":
				MapAdd(player.upgrades,"Astronomy",0.25,0);
				break;
				case "penetration":
				MapAdd(player.upgrades,"Bullet penetration",1,0);
				break;
				case "knockback":
				MapAdd(player.upgrades,"Knockback",1,0);
				break;
				case "tides":
				MapAdd(player.upgrades,"Tides",0.25,0);
				break;
				case "forcefield":
				player.forcefield+=0.7;
				break;
				case "side bullets":
				if(!player.attacks.isEmpty()) player.attacks.get(0).side_proj++;
				break;
				case "extra round":
				if(!player.attacks.isEmpty()) player.attacks.get(0).repeat++;
				break;
				case "front bullet":
				if(!player.attacks.isEmpty()) player.attacks.get(0).front_proj++;
				break;
				case "diagonal bullets":
				if(!player.attacks.isEmpty()) player.attacks.get(0).diag_proj++;
				break;
				case "backward bullet":
				if(!player.attacks.isEmpty()) player.attacks.get(0).back_proj++;
				break;
				case "wall bounce":
				if(!player.attacks.isEmpty()) player.attacks.get(0).bounce_level++;
				break;
				case "flame":
				if(!player.attacks.isEmpty()) player.attacks.get(0).burn++;
				break;
				case "poison":
				if(!player.attacks.isEmpty()) player.attacks.get(0).poison++;
				break;
				case "regeneration":
				MapAdd(player.upgrades,"Room health regen",0.07,0);
				break;
				case "lucky wheel":
				upgrades_discard.addAll(upgrades);
				upgrades.clear();break;
				case "pondering":
				if(upgrades_options<=8)upgrades_options+=1;
				default:
				player.score+=100;}
			MapAdd(upgrades_owned,s,1,0);
			log.add(gobj.timestamp+gobj.player.name+" got the upgrade:"+s);
			return "success";
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
	}
	public class Sprite{
		float x=0;
		float y=0;
		float rotation=0;
		double xspeed=0;
		double yspeed=0;
		double move_speed=2;
		int lifetime=-1;
		int age=0;
		double friction=0.2;
		double mass=1; //knock resist
		int knockback=10;
		int r=0;
		String name="Arthur Wu";
		Sprite owner;
		String photo="";
		String type="";
		String tag="";
		int health=1000;
		int max_health=1000;
		int last_health;
		int last_max_health;
		int regeneration=0;
		int last_damaged=0;
		double forcefield=0;
		int score=0;
		int level=0;
		HashMap<String, Double>upgrades=new HashMap<>();
		//int upgrade_cap=7;
		ArrayList<AttackType> attacks=new ArrayList<>();
		ArrayList<Effect> effects=new ArrayList<>();
		ArrayList<Effect> collision_apply_effects=new ArrayList<>();
		Sprite last_hit_by;
		int damage=150;
		int detect_range=900;
		int colour=(0xFFFFFFFF);
		ImageView image;
		TextView tv;
		ProgressBar bar;
		LinearLayout ll;
		Boolean init=false;
		Boolean show_particle=true;
		Boolean dispose=false;
		Boolean wall_kill=false;
		Boolean wall_bounce=false;
		Boolean active=true;
		void Update(ArrayList<Sprite> sps){
			//to be overrided
		}
		void Update(GameObject gobj){Update(gobj.ss);}
		void ApplyEffects(){
			int i=0;
			HashMap<String, Integer> damage_map=new HashMap<>();
			while(i<effects.size()){
				Effect e=effects.get(i);
				e.time--;
				if(e.time<0){effects.remove(i);continue;}
				if(e.time==0){effects.remove(i);}
				else{i++;}
				if(age%5==0&&IntMap(damage_map,e.name,0)<e.damage)damage_map.put(e.name,e.damage);
			}
			for(Map.Entry<String, Integer>en : damage_map.entrySet()){
				health-=en.getValue();
			}}
		final double getDisSq(Sprite s1, Sprite s2){
			return (s1.x-s2.x)*(s1.x-s2.x)+(s1.y-s2.y)*(s1.y-s2.y);}
		final Boolean isColliding(Sprite s1, Sprite s2){if(s1.dispose||s2.dispose){return false;}
			double dis=getDisSq(s1,s2);
			return (dis<=(s1.r+s2.r)*(s1.r+s2.r));
		}
		final Boolean insideForcefield(Sprite s1, Sprite s2){if(s1.dispose||s2.dispose){return false;}
			double dis=getDisSq(s1,s2);
			return (dis<=(s1.r*2.3+s2.r)*(s1.r*2.3+s2.r));
		}
		Boolean isCollidable(Sprite s){
			return !(tag.equals("")||s.tag.equals("")||tag.equals(s.tag)||s.type.equals("loot")||(type.equals("bullet")&&s.type.equals("bullet")));}
		Boolean allowCollide(Sprite s){
			//overrided for bullets
			return true;}
		
		void CheckCollision (ArrayList<Sprite> sps){
			for(Sprite s:sps){
				if(isCollidable(s)&&allowCollide(s)&&s.allowCollide(this)){if(isColliding(this,s)){HandleCollision(s);s.HandleCollision(this);
						HandleCollisionEnded(s);
						s.HandleCollisionEnded(this);}
					else if(insideForcefield(this,s)&&s.mass>0){
						double dist=Math.sqrt(getDisSq(this,s));
						s.xspeed+=forcefield*((s.x-x)/dist)/mass;
						s.yspeed+=forcefield*((s.y-y)/dist)/mass;
					}}
			}
		}
		void HandleCollision(Sprite s)
		{//only handles changes to self
			if(s.type.equals("loot"))return;
			double knock=s.knockback/mass;
			if(s.x>x){xspeed-=knock;}
			else if(s.x<x){xspeed+=knock;}
			if(s.y>y){yspeed-=knock;}
			else if(s.y<y){yspeed+=knock;}
			health-=s.damage;
			for(Effect e:s.collision_apply_effects){
				effects.add(new Effect(e));
			}
			if(s.owner!=null){last_hit_by=s.owner;}
			else{last_hit_by=s;}
		}
		
		void HandleCollisionEnded(Sprite s){}
		
		void Navigate(float tx, float ty){
			Navigate(tx,ty,0,0);}
		void Navigate(float tx, float ty, double td,float angle){
			Navigate(tx,ty,td,angle,false);}
		void Navigate(float tx, float ty, double td,float angle, Boolean rotate){
			double rx=tx-x;
			double ry=ty-y;
			double r2=Math.pow(rx*rx+ry*ry,0.5);
			rotation=(float)Math.atan2(ry,rx)*57.3f+90f-angle;
			if(r2>td+4*Math.max(0,move_speed)){
				xspeed+=move_speed*rx/r2;
				yspeed+=move_speed*ry/r2;}
			else if(r2<td-4*Math.max(0,move_speed)){
				xspeed-=move_speed*rx/r2;
				yspeed-=move_speed*ry/r2;}
			else if(rotate){
				xspeed+=move_speed*ry/r2;
				yspeed-=move_speed*rx/r2;}
		}
		//to be overrided
		void onBounce(){}
		void onRoomClear(){}
		int getValue(){return 10;}
	}
	public class Shooter extends Sprite{
		public Shooter(){
			type="shooter";
			health=4000;
			max_health=base_max_health;
			r=100;
			detect_range=2200;}
		public Shooter(int bsm, int bd, int bbd){
			this();
			base_max_health=bsm;
			max_health=bsm;
			health=bsm;
			base_damage=bd;
			base_bullet_damage=bbd;
		}
		int base_max_health=4000;
		int base_damage=400;
		double bullet_speed_multiplier=1d;
		int base_bullet_health=300;
		int base_bullet_damage=200;
		double base_att_speed=1d;
		double base_move_speed=2;
		int bullet_spread=5;
		int bullet_health=200;
		int bullet_damage=200;
		double att_speed=1d;
		int base_knockback=10;
		double knockback_multiplier=1d;
		boolean auto_face=false;
		
		void Update(ArrayList<Sprite> sps){
			ApplyUpgrades();
			if(auto_face)FaceTarget(sps);
			if(active){
				DoStuff(sps);
				for(AttackType at:attacks){
					at.att_progress+=att_speed;
					if(at.att_progress>=at.att_interval){
						if(at.repeat_counter<at.repeat){
							at.repeat_counter++;
							at.att_progress-=at.att_interval/10;
						}
						else{
							at.repeat_counter=0;
							at.att_progress-=at.att_interval;
						}
						Attack(at,sps);}
				}
			}
			/*ArrayList<Drone>temp=new ArrayList<>(drones);
for(Drone d:temp){
if(d.dispose){drones.remove(d);}}*/
		}
		void DoStuff(ArrayList<Sprite> sps){} //to be overrided
		void ApplyUpgrades(){
			regeneration=0;
			max_health=(int)(base_max_health*(1+0.05*level));
			damage=base_damage;
			bullet_speed_multiplier=1d;
			//bullet_health=base_bullet_health;
			bullet_health=100;
			bullet_damage=base_bullet_damage;
			att_speed=base_att_speed;
			knockback_multiplier=1d;
			move_speed=base_move_speed;
			for (Map.Entry<String, Double> en: upgrades.entrySet()) {
				switch(en.getKey().toLowerCase()){
					case "health regen":
					regeneration+=en.getValue();
					break;
					case "max health":
					max_health+=(int)(base_max_health*en.getValue());
					break;
					case "body damage":
					damage+=(int)(base_damage*en.getValue());
					break;
					case "bullet speed":
					bullet_speed_multiplier+=en.getValue();
					break;
					case "bullet penetration":
					bullet_health+=(int)(base_bullet_health*en.getValue());
					break;
					case "bullet damage":
					bullet_damage+=(int)(base_bullet_damage*en.getValue());
					break;
					case "reload":
					att_speed+=base_att_speed*en.getValue();
					break;
					case "movement speed":
					move_speed+=base_move_speed*en.getValue();
					break;
					case "attack":
					damage+=(int)(base_damage*en.getValue());
					bullet_damage+=(int)(base_bullet_damage*en.getValue());break;
					case "astronomy":
					damage+=(int)(base_damage*en.getValue()*(1-Math.sin(age/200d)));
					bullet_damage+=(int)(base_bullet_damage*en.getValue()*(1+Math.sin(age/200d)));
					break;
					case "tides":
					att_speed+=en.getValue()*(1+Math.sin(age/200d));
					break;
					case "knockback":
					knockback_multiplier+=en.getValue();
					break;
				}
				if(att_speed<0.05)att_speed=0.05;
				knockback=(int)(base_knockback*knockback_multiplier);
			}
		}
		
		
		
		
		
		
		
		void FaceTarget(ArrayList<Sprite> sps){
			TreeMap<Double,Sprite> targets=new TreeMap<>();
			for(Sprite s:sps){
				if(!isCollidable(s))continue;
				switch(s.type){
					case "shooter":
					case "shape":
					double tar_r=Math.pow(getDisSq(this, s),0.5);
					if(tar_r<detect_range){
						targets.put(tar_r,s);
					}}}
			if(targets.size()>0){
				Map.Entry<Double,Sprite> en=targets.pollFirstEntry();
				Sprite target= en.getValue();
				Navigate(target.x, target.y,en.getKey(),0);
			}
		}
		 void onRoomClear(){
			level++;
			if(upgrades.containsKey("Room health regen"))health+=(int)(base_max_health*upgrades.get("Room health regen"));
		}
		/*void Shoot(float dir, ArrayList<Sprite> sps){
Shoot(dir, sps, 0, 0);}
void Shoot(float dir, ArrayList<Sprite> sps, double offset){Shoot(dir, sps, offset, 0);}
void Shoot(float dir, ArrayList<Sprite> sps, double offset, int size){
Bullet s=new Bullet();
s.colour=colour;
s.lifetime=150;
s.damage=bullet_damage;
s.health=bullet_health;
s.max_health=bullet_health;
if(size==0){s.r=25;}
else if(size==333){
//Trapper
s.friction=0.1;
s.r=25;
s.lifetime=600;
}
else if(size==1){
s.knockback=7;
s.r=50;
}
else if(size==-1){
s.r=12;
}
s.x=x;
s.y=y;
float ro=(-90+rotation+dir+getRandom(-bullet_spread,bullet_spread))/57.3f;
s.xspeed=bullet_speed*Math.cos(ro);
s.yspeed=bullet_speed*Math.sin(ro);
//recoil deleted
s.hit_immune_max=240/bullet_speed;
//offset
s.x+=this.r*offset*Math.cos(ro-90);
s.y+=this.r*offset*Math.sin(ro-90);
s.owner=this;
s.tag=tag;
sps.add(s);
}*/
		/*ArrayList<Drone> drones=new ArrayList<>();
int drone_cap=0;
int att_progress2=0;
void AttemptProduce (ArrayList<Sprite> sps){
if(att_progress2>=att_interval){
for(int i=0;i<2;i++){
if(drones.size()<drone_cap){
ProduceDrone(sps);
att_progress2=0;
}}}
}
void ProduceDrone (ArrayList<Sprite> sps){
Drone d=new Drone();
d.colour=0xFFB3E5FC;
d.lifetime=10000;
d.damage=bullet_damage;
d.health=bullet_health;
d.max_health=bullet_health;
d.move_speed=bullet_speed;
d.detect_range=detect_range;
d.r=25;
d.x=x;
d.y=y; 
d.hit_immune_max=10;
d.owner=this;
sps.add(d);
drones.add(d);
}*/
		/*void AttemptShoot(ArrayList<Sprite> sps){
switch(mode){
case "Tank":
case "Sniper":
case "Machine gun":
case "Assassain":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps);}
break;
case "Twins":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps,0.6*(1-2*phase));
phase=(phase+1)%2;}
break;
case "Flank guard":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps);Shoot(180f,sps);}
break;
case "Triple shot":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps);Shoot(60f,sps);
Shoot(300f,sps);}
break;
case "Gunner":
if(att_progress>=att_interval){
att_progress=0;
if(phase==0){
Shoot(0f,sps,0.3,-1);
Shoot(0f,sps,-0.3,-1);
phase=1;}
else{Shoot(0f,sps,1,-1);
Shoot(0f,sps,-1,-1);
phase=0;}}
break;
case "Destroyer":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps,0d,1);}
break;
case "Quad tank":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps);Shoot(180f,sps);
Shoot(90f,sps);Shoot(270f,sps);}
break;
case "Triangle":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps);Shoot(150f,sps);
Shoot(210f,sps);}
break;
case "Twin flank":
if(att_progress>=att_interval){
att_progress=0;
if(phase==0){
Shoot(0f,sps,0.6);
Shoot(180f,sps,0.6);
phase=1;}
else{Shoot(0f,sps,-0.6);
Shoot(180f,sps,-0.6);
phase=0;}}
break;
case "Trapper":
if(att_progress>=att_interval){
att_progress=0;
Shoot(0f,sps,0d,333);}
break;
case "Hunter":
if(att_progress>=att_interval){
att_progress=0;
phase=1;
Shoot(0f,sps,0d,-1);}
if(phase>0){phase++;
if(phase>5){
phase=0;
Shoot(0f,sps,0d,0);}}
break;
case "Overseer":
AttemptProduce(sps);
break;
case "Auto 3":
if(att_progress>=att_interval){
att_progress=0;
AutoShoot(sps, 3);}
break;
case "Smasher":
break;
}}


*/
		
		
		
		
		
		
		
		
		
		
		
		void Attack(AttackType at, ArrayList<Sprite> sps){
			int i=0;
			for(i=0;i<at.front_proj;i++){
				Shoot(at, 0f, sps, -1+(2*i+1)/(float)at.front_proj);}
			for(i=0;i<at.diag_proj;i++){
				Shoot(at, -90*(-1+(i+1)/(float)(at.diag_proj+1)), sps);
				Shoot(at, 90*(-1+(i+1)/(float)(at.diag_proj+1)), sps);}
			for(i=0;i<at.side_proj;i++){
				Shoot(at, 90f, sps, -1+(2*i+1)/(float)at.side_proj);
				Shoot(at, -90f, sps, -1+(2*i+1)/(float)at.side_proj);}
			for(i=0;i<at.back_proj;i++){
				Shoot(at, 180f, sps, -1+(2*i+1)/(float)at.back_proj);}
		}
		void Shoot(AttackType at, float dir, ArrayList<Sprite> sps){
			Shoot(at, dir, sps, 0);}
		void Shoot(AttackType at, float dir, ArrayList<Sprite> sps, double offset){
			Bullet s=new Bullet();
			s.colour=colour;
			s.lifetime=150;
			s.base_damage=(int)(bullet_damage*at.damage_mul);
			s.health=bullet_health;
			s.max_health=bullet_health;
			s.knockback=(int)(at.knockback*knockback_multiplier);
			s.collision_apply_effects=new ArrayList<>(at.apply_effects);
			if(at.burn>0){
				s.collision_apply_effects.add(new Effect("burn",(int)(Math.sqrt(at.burn)*s.base_damage/20),(int)(Math.sqrt(at.burn)*50.01)));}
			if(at.poison>0){
				s.collision_apply_effects.add(new Effect("poison",(int)(Math.sqrt(at.poison)*s.base_damage/45),(int)(Math.sqrt(at.poison)*300.01)));}
			if(at.bounce_level>0){
				s.wall_kill=false;
				s.wall_bounce=true;
				s.bounce_mul=((double)at.bounce_level)/(at.bounce_level+2);
			}
			else{s.wall_bounce=false;}
			
			s.r=25;
			s.x=x;
			s.y=y;
			float ro=(-90+rotation+dir+getRandom(-bullet_spread,bullet_spread))/57.3f;
			double bullet_speed=at.proj_speed*bullet_speed_multiplier;
			s.xspeed=bullet_speed*Math.cos(ro);
			s.yspeed=bullet_speed*Math.sin(ro);
			//recoil, hit immune deleted
			//offset
			s.x+=this.r*offset*Math.cos(ro-90);
			s.y+=this.r*offset*Math.sin(ro-90);
			s.owner=this;
			s.tag=tag;
			sps.add(s);
		}
		//end of shooter class
	}
	public class MeleeShooter extends Shooter{
		public MeleeShooter(int bsm, int bd, int bbd){
			super(bsm,bd,bbd);
		}
		void DoStuff(ArrayList<Sprite> sps){
			TreeMap<Double,Sprite> targets=new TreeMap<>();
			for(Sprite s:sps){
				if(!isCollidable(s))continue;
				switch(s.type){
					case "shooter":
					case "shape":
					double tar_r=Math.pow (getDisSq(this, s),0.5);
					if(tar_r<detect_range)targets.put(tar_r,s);
				}}
			if(targets.size()>0){
				Map.Entry<Double,Sprite> en=targets.pollFirstEntry();
				Sprite target= en.getValue();
				Navigate(target.x, target.y);
			}
		}
	}
	public class Effect{
		public Effect(String name, int damage, int time){
			this.name=name;
			this.damage=damage;
			this.time=time;
		}
		public Effect(Effect e){
			name=e.name;
			type=e.type;
			level=e.level;
			time=e.time;
			damage=e.damage;
		}
		String name="Something";
		String type="";
		double level=0;
		int time=0;
		int damage=0; //damage per 5 ticks
		public String toString(){
			HashMap<String,String> t=new HashMap<>();
			t.put("type",type);
			t.put("name",name);
			t.put("level",String.valueOf(level));
			t.put("time",String.valueOf(time));
			t.put("damage",String.valueOf(damage));
			return t.toString();
		}
	}
	public class AttackType{
		String type;
		double damage_mul=1d;
		double att_interval=36;
		double att_progress;
		int front_proj=1;
		int diag_proj;
		int side_proj;
		int back_proj;
		int proj_speed=15;
		int knockback=3;
		int repeat; //number of extra attacks
		int repeat_counter;
		int bounce_level;
		ArrayList<Effect> apply_effects=new ArrayList<>();
		int burn=0;
		int poison=0;
	}
	public class Bullet extends Sprite{
		double bounce_mul=0.5;
		int base_damage=100;
		public Bullet(){
			wall_kill=true;
			show_particle=false;
			friction=0;
			knockback=3;
			r=15;
			mass=4;
			type="bullet";}
		void Update(ArrayList<Sprite> sps){
			damage=base_damage*health/max_health;
		}
		ArrayList<Sprite> collided=new ArrayList<>();
		void HandleCollisionEnded(Sprite s){collided.add(s);
			if(health<0){dispose=true;damage=0;}
			else{
				damage=base_damage*health/max_health;}
		}
		Boolean allowCollide(Sprite s){
			return !(collided.contains(s));}
		void onBounce(){base_damage=(int)(bounce_mul*base_damage);
			if(damage<=0)dispose=true;}
		int getValue(){return 0;}
	}
	public class Drone extends Bullet{
		public Drone(){
			name="Drone";
			wall_kill=false;
			friction=0.2;
			//health=100000;
			//max_health=100000;
			move_speed=3;
			knockback=5;
			type="bullet";}
		Sprite target;
		Sprite Tracker(){
			return (owner==null)?this:owner;}
		void Update(ArrayList<Sprite> sps){
			if(active){
				if(owner!=null&&owner!=this&&!owner.active){
					//active controlling
					float ro=owner.rotation/57.3f;
					float tx=owner.x+(int)(owner. detect_range*Math.cos(ro)*0.4);
					float ty=owner.y+(int)(owner. detect_range*Math.sin(ro)*0.4);
					Navigate(tx,ty);
				}
				else{
					//target search
					if(target==null){SearchTarget(sps);
						Navigate(Tracker().x, Tracker().y);}
					else{
						if(target.dispose){target=null;}else{Navigate(target.x, target.y);}}}
			}}
		
		void SearchTarget(ArrayList<Sprite> sps){
			ArrayList<Sprite> targets=new ArrayList<>();
			for(Sprite s:sps){
				switch(s.type){
					case "shooter":
					if(!isCollidable(s))break;
					case "enemy":
					double tar_r=getDisSq(Tracker(), s);
					if(tar_r<Math.pow(detect_range,2)){
						targets.add(s);}}}
			if(targets.size()>0){
				Random ran=new Random();
				int ri= ran.nextInt(targets.size());
				target=targets.get(ri);
			}
		}
		
		
	}
	public class Loot extends Sprite{
		public Loot(){
			type="loot";
			photo="red heart";
			colour=0;
			r=50;
		}
		Boolean isCollidable(Sprite s){
			return tag.equals(s.tag)&&s.type.equals("shooter");}
		void HandleCollision(Sprite s)
		{//only handles changes to self
			if(!s.type.equals("shooter"))return;
			s.health+=s.max_health/8;
			dispose=true;
		}
		
	}
	public class Square extends Sprite{
		public Square(){colour=0xB0FFFF00;
			tag="enemy";
			type="shape";
			r=75;}
		public Square(int hp){this();
			health=hp;max_health=hp;}
	}
	public class Particle{
		//movement and position
		int x=0;
		int y=0;
		int xspeed=0;
		int yspeed=0;
		int lifetime=100;
		String text="Arthur Wu";
		//render
		int colour=(0xFFFFFFFF);
		TextView tv;
		Boolean init=false;
		Particle(int x,int y, int xspeed, int yspeed, int lifetime, String text, int colour){
			this.x=x;
			this.y=y;
			this.xspeed=xspeed;
			this.yspeed=yspeed;
			this.lifetime=lifetime;
			this.text=text;
			this.colour=colour;
		}
		Particle(int x,int y, String text, int colour){
			this.x=x;
			this.y=y;
			this.xspeed=0;
			this.yspeed=-15;
			this.lifetime=20;
			this.text=text;
			this.colour=colour;
		}
	}
	void Render(GameObject gobj){
		ArrayList<Sprite>sps=gobj.ss;
		ArrayList<Sprite>temp=new ArrayList<>(sps);
		for(Sprite s:temp){
			if(s.dispose){
				if(s.init)
				s.ll.setVisibility(View.INVISIBLE);
				sps.remove(s);}}
		
		for(Sprite s:sps){if(!s.init){
				s.init=true;
				ImageView im=new ImageView(this);
				s.image = im;
				im.setImageResource(R.drawable.default_image);
				im.setLayoutParams(new ViewGroup.LayoutParams((int)(s.r*1.5), (int)(s.r*1.5)));
				TextView tv=new TextView(this);
				tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				tv.setTextSize(8);
				tv.setGravity(1);
				s.tv=tv;
				s.ll=new LinearLayout(this);
				s.ll.setOrientation(1);
				s.ll.setGravity(1); //center horizontal
				s.ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				//clipping problems
				im.setScaleX(0.7f);
				im.setScaleY(0.7f);
				//health bar
				s.bar=new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
				s.bar.setIndeterminate(false);
				s.bar.setLayoutParams(new ViewGroup.LayoutParams(100, 13));
				android.graphics.drawable.Drawable pd= s.bar.getProgressDrawable().mutate();
				pd.setColorFilter(0xFF009000, android.graphics.PorterDuff.Mode.SRC_IN);
				s.ll.addView(s.bar);
				s.ll.addView(tv);
				s.ll.addView(im);
				l1.addView(s.ll);}
			s.tv.setText(s.name+"\n"+String.valueOf(s.score));
			ImageView im=s.image;
			LinearLayout ll=s.ll;
			im.setImageResource(getImage(s.photo));
			ll.setScaleX(1f);
			ll.setScaleY(1f);
			//Rotation clipping workaround
			ll.setScaleX(ll.getScaleX()*1.5f);
			ll.setScaleY(ll.getScaleY()*1.5f);
			im.setBackgroundColor(s.colour);
			ll.setTranslationX(s.x-SketchwareUtil.getLocationX(ll)-(int)(ll.getWidth()*0.5*ll.getScaleX())+ll.getTranslationX()-gobj.camera_x);
			ll.setTranslationY((int)(s.y-SketchwareUtil.getLocationY(ll)-ll.getHeight()*ll.getScaleY()+im.getHeight()*ll.getScaleY()*0.5+ll.getTranslationY()-gobj.camera_y));
			if(s.type.equals("shooter")){
				s.tv.setVisibility(View.VISIBLE);
				s.tv.setTranslationY((int)(0.2*im.getHeight()));
				s.bar.setTranslationY((int)(0.2*im.getHeight()));
			}
			else{s.tv.setVisibility(View.GONE);}
			im.setRotation(s.rotation);
			s.bar.setMax(s.max_health);
			s.bar.setProgress(s.health);
			if(s.health>=s.max_health){
				s.bar.setVisibility(View.INVISIBLE);
			}
			else{s.bar.setVisibility(View.VISIBLE);}
		}
		RenderParticles(gobj);
		
	}
	void RenderParticles(GameObject gobj){
		ArrayList<Particle>temp=new ArrayList<>(gobj.ps);
		for(Particle s:temp){
			if(s.lifetime<=0){
				if(s.init)s.tv.setVisibility(View.INVISIBLE);
				gobj.ps.remove(s);}}
		for(Particle s:gobj.ps){
			if(!s.init){
				s.init=true;
				TextView tv=new TextView(this);
				s.tv = tv;
				tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				l1.addView(tv);
			}
			
			s.tv.setTranslationX(s.x-SketchwareUtil.getLocationX(s.tv)-(int)(s.tv.getWidth()*0.5*s.tv.getScaleX())+s.tv.getTranslationX()-gobj.camera_x);
			s.tv.setTranslationY(s.y-SketchwareUtil.getLocationY(s.tv)-(int)(s.tv.getHeight()*0.5*s.tv.getScaleY())+s.tv.getTranslationY()-gobj.camera_y);
			s.tv.setText(s.text);
			s.tv.setTextColor(s.colour);
			//s.tv.setBackgroundColor(0xFFFFFFFF);
		}
	}
	void Update(GameObject gobj){
		gobj.Update();
	}
	void ShowUpgrade(){
		ArrayList<String> upgrades=gobj.upgrades;
		//clear existing upgrades
		upgrade_select.removeAllViewsInLayout();
		int row_size=3;
		int rows=(upgrades.size()-1)/row_size+1;
		for(int i=0;i<rows;i++){
			LinearLayout ll=new LinearLayout(this);
			upgrade_select.addView(ll);
			ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			
			for(int j=0;j<row_size;j++){
				if(i*row_size+j>=upgrades.size())break;
				String s=upgrades.get(i*row_size+j);
				
				Button im=new Button(this);
				im.setBackgroundResource(getUpgradeImage(s));
				im.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
				im.setId(j+i*row_size);
				im.setOnClickListener( new OnClickListener() { @Override 
					public void onClick(View v) {
						UpgradeClick((Button)v);
					} });
				im.setOnLongClickListener( new OnLongClickListener() { @Override 
					public boolean onLongClick(View v) {
						SketchwareUtil.showMessage(getApplicationContext(),getUpgradeDesc(gobj.upgrades.get(v.getId())));
						return true;
					} });
				TextView tv=new TextView(this);
				tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				tv.setGravity(1);
				int exp=getUpgradeCost(s);
				tv.setText(translate(s)+"\n"+(exp==2147483647?"N/A":(String.valueOf(exp)+" EXP")));
				LinearLayout ll2=new LinearLayout(this);
				ll2.setOrientation(1);
				tv.setTextColor(0xFFFFFFFF);
				//im.setBackgroundColor(0xFFFFFFFF);
				ll2.setGravity(1); //center horizontal
				ll2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
				ll2.addView(im);ll2.addView(tv);
				ll.addView(ll2);
				
				
			}
		}
	}
	void UpgradeClick(Button b){
		//SketchwareUtil.showMessage(getApplicationContext(),String.valueOf(b.getId()));
		/*String s=gobj.upgrades.get(b.getId());
HashMap<String,String> info=new HashMap<>();
info.put("name",s);
info.put("cost",String.valueOf(getUpgradeCost(s)));
info.put("image",String.valueOf(getUpgradeImage(s)));
info.put("desc",getUpgradeDesc(s));
CopyToClip(new Gson().toJson(info));*/
		String message="";
		switch(gobj.DoUpgrade(b.getId())){
			case "invalid":
			message="Upgrade not available!";break;
			case "expensive":
			message=translate("cant_afford");break;
			case "success":
			message=translate("upgraded");break;
			default:
			message="An unknown error occured!!";
		}
		SketchwareUtil.showMessage(getApplicationContext(),message);
		ShowUpgrade();
	}
	int getUpgradeImage(String s){
		String s2=s.toLowerCase();
		if(upgrade_images.containsKey(s2))return getImage(upgrade_images.get(s2));
		return getImage("");
	}
	String getUpgradeDesc(String s){
		String s2=s.toLowerCase();
		if(upgrade_desc.containsKey(s2))return translate(upgrade_desc.get(s2));
		return "Try it out yourself to find out!";
	}
	int getUpgradeCost(String s){
		String s2=s.toLowerCase();
		return IntMap(upgrade_costs,s2,Integer.MAX_VALUE);
	}
	static Boolean isInt(String s){
		return (s.matches("-?[0123456789]{1,9}"));
	}
	static int IntMap(HashMap<String,Integer> map, String key, int preset){
		if(map.containsKey(key)){
			return map.get(key);
		}
		else{return preset;}
	}
	static void MapAdd(HashMap<String,Double> map, String key, double value, double preset){
		if(map.containsKey(key)){
			map.put(key,map.get(key)+value);
		}
		else{map.put(key,preset+value);}
	}
	static void MapAdd(HashMap<String,Integer> map, String key, int value, int preset){
		if(map.containsKey(key)){
			map.put(key,map.get(key)+value);
		}
		else{map.put(key,preset+value);}
	}
	static double clamp(double min, double input, double max){
		if(min>max)return min;
		if(input<min)return min;
		if(input>max)return max;
		return input;
	}
	static double decimal(double input){
		return input-Math.floor(input);
	}
	int getImage(String s){
		//res_name is the name of the image resource
		String res_name = s.toLowerCase();
		res_name = res_name.replace(" ", "_");
		int result=getResources ().getIdentifier(res_name, "drawable", getPackageName());
		return (result==0?R.drawable.default_image:result);
	}
	void CopyToClip(String s){
		//requires a block to work
		((ClipboardManager)getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard",s));
		SketchwareUtil.showMessage(getApplicationContext(),"Copied to clipboard!");
	}
	String translate(String text){
		if(Translations!=null&& Translations.containsKey(text.toLowerCase()))return Translations.get(text.toLowerCase()).toString();
		return text;
	}
	
	{
		/*
upgrade_deck.clear();
upgrades_deck.add("Attack up");
upgrades_deck.add("Reload");
upgrades_deck.add("Health up");
for(int i=0;i<3;i++)upgrades_deck.add("Lucky wheel");
upgrades_deck.add("Swiftness");
upgrades_deck.add("Side bullets");
upgrades_deck.add("Extra round");
upgrades_deck.add("Front bullet");
upgrades_deck.add("Diagonal bullets");
upgrades_deck.add("Backward bullet");
upgrades_deck.add("Wall bounce");
upgrades_deck.add("Penetration");
upgrades_deck.add("Regeneration");
for(int i=0;i<3;i++)upgrades_deck.add("Pondering");
upgrades_deck.add("Knockback");
upgrades_deck.add("Holiness up");
upgrades_deck.add("Astronomy");
upgrades_deck.add("Random option");
//upgrades_deck.add("Well...");
//for(int i=0;i<10;i++)
int getUpgradeImage(String s){
switch(s.toLowerCase()){
case "suspicion up":
return getImage("thinking face");
case "holiness up":
return getImage("relieved face");
case "health up":
return getImage("red heart");
case "lucky wheel":
return getImage("game die");
case "pondering":
return getImage("thinking face");
case "swiftness":
return getImage("dashing away");
case "side bullets":
return getImage("left right arrow");
case "extra round":
return getImage("fast up arrow");
case "front bullet":
return getImage("up arrow");
case "diagonal bullets":
return getImage("up right arrow");
case "backward bullet":
return getImage("down arrow");
case "wall bounce":
return getImage("dizzy");
case "penetration":
return getImage("sagittarius");
case "regeneration":
return getImage("growing heart");
case "knockback":
return getImage("oncoming fist");
case "astronomy":
return getImage("new moon face");
case "attack up":
return getImage("crossed swords");
case "reload":
return getImage("stopwatch");
default:
return getImage("");
}
}
String getUpgradeDesc(String s){
switch(s.toLowerCase()){
case "suspicion up":
return "Makes the game more suspicious.";
case "holiness up":
return "3 is a holy number.";
case "health up":
return "Increases Max Health by 30%.";
case "lucky wheel":
return "Discard all current upgrade options.";
case "pondering":
return "Gains an extra upgrade option (maximum 9).";
case "swiftness":
return "Increases Movement Speed by 30%.";
case "side bullets":
return "Main attack shoot extra projectiles on left and right.";
case "extra round":
return "Main attack shoot an extra round per reload.";
case "front bullet":
return "Main attack shoot an extra projectile in the front.";
case "diagonal bullets":
return "Main attack shoot extra projectiles diagonally foward.";
case "backward bullet":
return "Main attack shoot an extra projectile backward.";
case "wall bounce":
return "Main attack projectiles bounce of wall with decreased damage.\n(Repeated upgrade improves damage.)";
case "penetration":
return "Bullet can pierce enemies, but deals less damage to subsequent ones.";
case "regeneration":
return "Regenerates 7% of Base Max Health every time a room is cleared.";
case "knockback":
return "Increases Bullet and Body knockback by 100%.";
case "astronomy":
return "Increases Bullet and Body damage by up to 50%(varies over time).";
case "attack up":
return "Increases Bullet and Body Damage by 30%.";
case "reload":
return "Increases Attack Speed by 30%.";
default:
return "Uhhh idk :(";
}
}
int getUpgradeCost(String s){
switch(s.toLowerCase()){
case "attack up":
return 30;
case "reload":
return 30;
case "health up":
return 25;
case "lucky wheel":
return 5;
case "swiftness":
return 15;
case "side bullets":
return 30;
case "extra round":
return 80;
case "front bullet":
return 80;
case "diagonal bullets":
return 50;
case "backward bullet":
return 12;
case "wall bounce":
return 15;
case "penetration":
return 20;
case "regeneration":
return 25;
case "pondering":
return 5;
case "knockback":
return 15;
case "holiness up":
return 3;
case "astronomy":
return 30;
default:
return Integer.MAX_VALUE;
}
}
*/
	}
	
	
	private void _tutorial_general () {
		tutorials.remove("general");
		paused=true;
		tutorial_dialog.setTitle(translate("tutorial_general_1_title"));
		tutorial_dialog.setMessage(translate("tutorial_general_1"));
		tutorial_dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				tutorial_dialog.setTitle(translate("tutorial_general_2_title"));
				tutorial_dialog.setMessage(translate("tutorial_general_2"));
				tutorial_dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						tutorial_dialog.setTitle(translate("tutorial_general_3_title"));
						tutorial_dialog.setMessage(translate("tutorial_general_3"));
						tutorial_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								paused=false;
							}
						});
						tutorial_dialog.create().show();
					}
				});
				tutorial_dialog.create().show();
			}
		});
		tutorial_dialog.create().show();
	}
	
	
	private void _tutorial_upgrade () {
		tutorials.remove("upgrade");
		paused=true;
		tutorial_dialog.setTitle(translate("tutorial_upgrade_2_title"));
		tutorial_dialog.setMessage(translate("tutorial_upgrade_2"));
		tutorial_dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				tutorial_dialog.setTitle(translate("tutorial_upgrade_1_title"));
				tutorial_dialog.setMessage(translate("tutorial_upgrade_1"));
				tutorial_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				tutorial_dialog.create().show();
			}
		});
		tutorial_dialog.create().show();
	}
	
	
	private void _tutorial_next () {
		tutorials.remove("next");
		paused=true;
		tutorial_dialog.setTitle(translate("tutorial_next_1_title"));
		tutorial_dialog.setMessage(translate("tutorial_next_1"));
		tutorial_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				paused=false;
			}
		});
		tutorial_dialog.create().show();
	}
	
	
	private void _load_upgrades_info () {
		ArrayList<HashMap<String,String>>upgrade_info=new Gson().fromJson(upgrades.getString("upgrades","[]"),new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType());
		for(HashMap<String,String>upgrade:upgrade_info){
			if(upgrade.containsKey("name")){
				String n=upgrade.get("name").toLowerCase();
				if(upgrade.containsKey("image"))upgrade_images.put(n,upgrade.get("image"));
				if(upgrade.containsKey("cost"))upgrade_costs.put(n,Integer.parseInt(upgrade.get("cost")));
				if(upgrade.containsKey("desc"))upgrade_desc.put(n,upgrade.get("desc"));
			}
		}
		/*
SketchwareUtil.showMessage(getApplicationContext(),upgrades.getAll().toString());
SketchwareUtil.showMessage(getApplicationContext(),upgrades.getString("upgrades","[]"));
SketchwareUtil.showMessage(getApplicationContext(),String.valueOf(upgrade_info.size()));
SketchwareUtil.showMessage(getApplicationContext(),String.valueOf(upgrade_images));
*/
	}
	
	
	private void _load_deck () {
		gobj.upgrades_deck.clear();
		String deck_name= player.getString("selected_deck","");
		String deckstr=decks.getString(deck_name,"");
		if(deckstr.startsWith("{")){
			HashMap<String,String>deck=new Gson().fromJson(deckstr,new TypeToken<HashMap<String, String>>(){}.getType());
			for (Map.Entry<String, String>en: deck.entrySet()){
				String v=en.getValue();
				int j=0;
				if(isInt(v))j=Integer.parseInt(v);
				for(int i=0;i<j;i++)gobj.upgrades_deck.add(en.getKey());
			}
		}
		else{SketchwareUtil.showMessage(getApplicationContext(),"Deck not found!");
		}
		/*
SketchwareUtil.showMessage(getApplicationContext(),String.valueOf(gobj.upgrades_deck));
*/
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
