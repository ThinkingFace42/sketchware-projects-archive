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
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {
	
	
	private HashMap<String, Object> TreasureMap = new HashMap<>();
	
	private Intent neutral_intent = new Intent();
	private SharedPreferences upgrades;
	private SharedPreferences decks;
	private SharedPreferences player;
	private SharedPreferences trans;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		upgrades = getSharedPreferences("upgrades", Activity.MODE_PRIVATE);
		decks = getSharedPreferences("decks", Activity.MODE_PRIVATE);
		player = getSharedPreferences("player", Activity.MODE_PRIVATE);
		trans = getSharedPreferences("translations", Activity.MODE_PRIVATE);
	}
	private void initializeLogic() {
		TreasureMap = new HashMap<>();
		TreasureMap = new Gson().fromJson(new Gson().toJson(TreasureMap), new TypeToken<HashMap<String, Object>>(){}.getType());
		_InitializeUpgrades();
		_InitializeDecks();
		_InitializeTrans();
		neutral_intent.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(neutral_intent);
		finish();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _InitializeUpgrades () {
		upgrades.edit().clear().commit();
		upgrades.edit().putString("upgrades", "[{\"image\":\"new moon face\",\"cost\":\"30\",\"name\":\"Astronomy\",\"count\":\"0\",\"desc\":\"astronomy_desc\"},{\"image\":\"crossed swords\",\"cost\":\"30\",\"name\":\"Attack up\",\"count\":\"1\",\"desc\":\"attack_up_desc\"},{\"image\":\"down arrow\",\"cost\":\"12\",\"name\":\"Backward bullet\",\"count\":\"5\",\"desc\":\"backward_bullet_desc\"},\n{\"image\":\"up right arrow\",\"cost\":\"50\",\"name\":\"Diagonal bullets\",\"count\":\"0\",\"desc\":\"diagonal_bullets_desc\"},\n{\"image\":\"fast up arrow\",\"cost\":\"80\",\"name\":\"Extra round\",\"count\":\"0\",\"desc\":\"extra_round_desc\"},\n{\"image\":\"up arrow\",\"cost\":\"80\",\"name\":\"Front bullet\",\"count\":\"0\",\"desc\":\"front_bullet_desc\"},\n{\"image\":\"red heart\",\"cost\":\"25\",\"name\":\"Health up\",\"count\":\"0\",\"desc\":\"health_up_desc\"},\n{\"image\":\"relieved face\",\"cost\":\"3\",\"name\":\"Holiness up\",\"count\":\"0\",\"desc\":\"holiness_up_desc\"},\n{\"image\":\"oncoming fist\",\"cost\":\"15\",\"name\":\"Knockback\",\"count\":\"0\",\"desc\":\"knockback_desc\"},\n{\"image\":\"game die\",\"cost\":\"5\",\"name\":\"Lucky wheel\",\"count\":\"0\",\"desc\":\"lucky_wheel_desc\"},\n{\"image\":\"sagittarius\",\"cost\":\"20\",\"name\":\"Penetration\",\"count\":\"1\",\"desc\":\"penetration_desc\"},\n{\"image\":\"thinking face\",\"cost\":\"5\",\"name\":\"Pondering\",\"count\":\"0\",\"desc\":\"pondering_desc\"},\n{\"image\":\"\",\"cost\":\"2147483647\",\"name\":\"Random option\",\"count\":\"0\",\"desc\":\"random_option_desc\"},\n{\"image\":\"growing heart\",\"cost\":\"25\",\"name\":\"Regeneration\",\"count\":\"0\",\"desc\":\"regeneration_desc\"},\n{\"image\":\"stopwatch\",\"cost\":\"30\",\"name\":\"Reload\",\"count\":\"1\",\"desc\":\"reload_desc\"},\n{\"image\":\"left right arrow\",\"cost\":\"30\",\"name\":\"Side bullets\",\"count\":\"0\",\"desc\":\"side_bullets_desc\"},\n{\"image\":\"dashing away\",\"cost\":\"15\",\"name\":\"Swiftness\",\"count\":\"0\",\"desc\":\"swiftness_desc\"},\n{\"image\":\"dizzy\",\"cost\":\"15\",\"name\":\"Wall bounce\",\"count\":\"2\",\"desc\":\"wall_bounce_desc\"},\n{\"image\":\"hollow red circle\",\"cost\":\"500\",\"name\":\"Forcefield\",\"count\":\"2\",\"desc\":\"forcefield_desc\"},\n{\"image\":\"fire\",\"cost\":\"30\",\"name\":\"Flame\",\"count\":\"2\",\"desc\":\"flame_desc\"},\n{\"image\":\"skull\",\"cost\":\"25\",\"name\":\"Poison\",\"count\":\"2\",\"desc\":\"poison_desc\"},\n{\"image\":\"water wave\",\"cost\":\"30\",\"name\":\"Tides\",\"count\":\"2\",\"desc\":\"tides_desc\"}\n]\n\n\n\n\n\n\n\n\n\n\n\n\n\n").commit();
	}
	
	
	private void _InitializeDecks () {
		if (player.getString("initilized", "").equals("")) {
			player.edit().putString("initilized", "1").commit();
			player.edit().putString("selected_deck", "Default deck").commit();
			player.edit().putString("show_tutorial", "1").commit();
			decks.edit().putString("Default deck", "{\"Backward bullet\":\"1\",\"Front bullet\":\"1\",\"Side bullets\":\"1\",\"Wall bounce\":\"1\",\"Swiftness\":\"1\",\"Random option\":\"0\",\"Health up\":\"1\",\"Regeneration\":\"1\",\"Penetration\":\"1\",\"Holiness up\":\"1\",\"Extra round\":\"1\",\"Attack up\":\"1\",\"Diagonal bullets\":\"1\",\"Lucky wheel\":\"3\",\"Reload\":\"1\",\"Knockback\":\"1\",\"Pondering\":\"3\",\"Astronomy\":\"1\"}").commit();
			decks.edit().putString("Empty deck", "{\"Balding\":\"3\"}").commit();
		}
	}
	
	
	private void _InitializeTrans () {
		trans.edit().putString("chinese", "{\"show_tutorial\":\"顯示教程\",\n\"play_btn\":\"開始\",\n\"credits_btn\":\"鳴謝\",\n\"deck_btn\":\"編輯牌組\",\n\"language_btn\":\"更改語言\",\n\"delete_deck_btn\":\"刪除牌組\",\n\"new_deck_btn\":\"新增牌組\",\n\"clipboard_deck_btn\":\"複製到剪貼簿\",\n\"import_deck_btn\":\"匯入牌組\",\n\"click_card_text\":\"提示：點擊卡片以查看資訊\",\n\"nodeck_text\":\"沒有牌組\",\n\"choose_upgrade_text\":\"選擇下面的升級！\n（擊敗房間裡最後一個敵人時空槽會重新填充）\",\n\"tutorial_general_1_title\":\"遊戲介紹\",\n\"tutorial_general_1\":\"感謝參與(還在想名字)! 在這遊戲中，你是一名冒險家，正在探索深不見底的地牢。然而，你遇到了一些敵人，他們可以透過射擊或近戰來傷害你。\",\n\"tutorial_general_2_title\":\"移動\",\n\"tutorial_general_2\":\"您可以按左下角的方向鍵來移動。當你站著不動時，你會自動瞄準最近的敵人，並在裝填好後射他們。\",\n\"tutorial_general_3_title\":\"其他功能\",\n\"tutorial_general_3\":\"屏幕頂還有兩個按鈕。其中一個用來存取升級選單，當您打開該畫面時可以了解更多資訊。另一個用於暫停和恢復遊戲（長按可以查看遊戲日誌）。\",\n\"tutorial_upgrade_1_title\":\"升級\",\n\"tutorial_upgrade_1\":\"在這裡您可以獲得各種升級（從牌組中隨機選擇）。每當完成房間時，你會收到經驗值，你可以在螢幕頂部的資訊欄查看。您可以花費 EXP 進行升級。\",\n\"tutorial_upgrade_2_title\":\"升級\",\n\"tutorial_upgrade_2\":\"長按某個升級來看它的效果。 當您擊敗房間（並獲得經驗值）時，會刷新已選的升級。 按一下「選擇升級」文字會顯示您擁有的升級。 再次點 ⏫ 按鈕以關閉升級面板。\",\n\"tutorial_next_1_title\":\"下一房間\",\n\"tutorial_next_1\":\"做得好！你獲得了一些經驗。 您可以透過到達頂部邊緣或按下中央按鈕前往下一房間。\",\n\n\"enter_name\":\"請輸入名稱...\",\n\n\"room\":\"房間\",\n\"enemy_left\":\"剩餘敵人\",\n\"cant_afford\":\"買不起 :(\",\n\"upgraded\":\"升級完成！\",\n\"deck\":\"牌組\",\n\"card_info\":\"卡牌資訊\",\n\"exit\":\"離開\",\n\"exit_text\":\"確定要離開嗎？\",\n\"yes\":\"是\",\n\"no\":\"否\",\n\n\"astronomy\":\"天文\",\n\"attack up\":\"攻擊提升\",\n\"backward bullet\":\"背向彈\",\n\"diagonal bullets\":\"斜向彈\",\n\"extra round\":\"連射\",\n\"flame\":\"火焰\",\n\"forcefield\":\"力場\",\n\"front bullet\":\"正向彈\",\n\"health up\":\"生命上限提升\",\n\"holiness up\":\"神聖值提升\",\n\"knockback\":\"擊退\",\n\"lucky wheel\":\"幸運抽獎\",\n\"penetration\":\"貫穿\",\n\"poison\":\"劇毒\",\n\"pondering\":\"思考\",\n\"random option\":\"隨機選項\",\n\"regeneration\":\"再生\",\n\"reload\":\"裝填\",\n\"side bullets\":\"側向彈\",\n\"swiftness\":\"敏捷\",\n\"tides\":\"潮汐\",\n\"wall bounce\":\"彈牆\",\n\"astronomy_desc\":\"增加子彈和身體傷害高達 50%（隨時間變化）。\", \"attack_up_desc\":\"子彈和近戰傷害增加 30%。\", \"backward_bullet_desc\":\"主要攻擊向後發射一枚額外的彈頭。\", \"diagonal_bullets_desc\":\"主要攻擊向斜前方發射額外的彈頭。\", \"extra_round_desc\":\"每次重新裝彈，主攻擊會額外射擊一輪。\",\n\"flame_desc\":\"主要攻擊附帶一定的持續傷害。\",\n\"forcefield_desc\":\"排斥附近區域的敵對實體。\",\n\"front_bullet_desc\":\"主要攻擊在前方發射一枚額外的彈體。\", \"health_up_desc\":\"最大生命值增加 30%。\",\n\"holiness_up_desc\":\"3 是一個神聖的數字。\",\n\"knockback_desc\":\"子彈和近戰擊退增加 100%。\", \"lucky_wheel_desc\":\"放棄目前所有升級選項。\",\n\"penetration_desc\":\"子彈可以貫穿敵人，但對後續敵人造成的傷害減少。 （重複升級進一步增加貫穿和傷害。）\",\n\"poison_desc\":\"主要攻擊附帶長久但緩慢的持續傷害。\",\n\"pondering_desc\":\"獲得額外的升級選項（最多 9 個）。\", \"random_option_desc\":\"我也不知道:(\",\n\"regeneration_desc\":\"每次清理房間時都會恢復 7% 的基礎最大生命值。\",\n\"reload_desc\":\"攻擊速度提高 30%。\", \"side_bullets_desc\":\"主要攻擊向左側右側發射額外的彈頭。\", \"swiftness_desc\":\"移動速度提高 30%。\",\n\"tides_desc\":\"增加攻擊速度高達 50%（隨時間變化）。\",\n\"wall_bounce_desc\":\"主要攻擊彈體從牆上反彈，但傷害會減少。 （反覆升級可提高傷害。）\",\n\n\"lang_name\":\"繁體中文\"}").commit();
		trans.edit().putString("english", "{\"show_tutorial\":\"Show tutorials\",\n\"play_btn\":\"Play\",\n\"credits_btn\":\"Credits\",\n\"deck_btn\":\"Edit deck\",\n\"language_btn\":\"Change language\",\n\"delete_deck_btn\":\"Delete deck\",\n\"new_deck_btn\":\"New deck\",\n\"clipboard_deck_btn\":\"Copy to clipboard\",\n\"import_deck_btn\":\"Import deck\",\n\"click_card_text\":\"Tips: you can click on a card to see its info.\",\n\"nodeck_text\":\"No deck available...\",\n\"choose_upgrade_text\":\"Choose an upgrade below!\n(Empty slots are refilled defeating last enemy in room)\",\n\"tutorial_general_1_title\":\"Introduction\",\n\"tutorial_general_1\":\"Welcome to (name coming soon)! In this game, you are an adventurer who is exploring the depths of a huge dungeon. However, you have encountered some enemies, who can hurt you by shooting bullets or colliding with you.\",\n\"tutorial_general_2_title\":\"Movement\",\n\"tutorial_general_2\":\"You can move around by pressing the direction keys (at the lower left corner). While you are standing still, you would automatically target the closest enemy and shoot at them upon finishing reload.\",\n\"tutorial_general_3_title\":\"Other features\",\n\"tutorial_general_3\":\"There are two more buttons on the top. One of them is for accessing the upgrade menu, more on that when you open that screen. The other one is for pausing and resuming the game(long clicking allows view of game log).\",\n\"tutorial_upgrade_1_title\":\"Upgrade\",\n\"tutorial_upgrade_1\":\"Here you can access a variety of upgrades (chosen randomly from your deck). When you clear a room, you would receive EXP, which you can check on the info bar on top of the screen. You can then spend EXP on upgrades.\",\n\"tutorial_upgrade_2_title\":\"Upgrade\",\n\"tutorial_upgrade_2\":\"You can long click on an upgrade to check what it does.\nChosen upgrade options will be rerolled when you defeat a room (and receive EXP).\nClicking on the \\\"choose upgrade\\\" text shows you what upgrades you have chosen before.\nClose the upgrade panel by clicking on the ⏫ button again.\",\n\"tutorial_next_1_title\":\"Next room\",\n\"tutorial_next_1\":\"Good job in clearing this room! You have gained some experience.\nYou can proceed to the next room by reaching the top edge, or by pressing on the new moon face button (ie the \\\"center direction key\\\").\",\n\n\"enter_name\":\"Enter a name...\",\n\n\"enemy_left\":\"Enemy left\",\n\"cant_afford\":\"Can't afford :(\",\n\"upgraded\":\"Upgraded!\",\n\"card_info\":\"Card info\",\n\"exit_text\":\"Do you want to exit?\",\n\n\"astronomy_desc\":\"Increases Bullet and Body damage by up to 50%(varies over time).\",\n\"attack_up_desc\":\"Increases Bullet and Body Damage by 30%.\",\n\"backward_bullet_desc\":\"Main attack shoot an extra projectile backward.\",\n\"diagonal_bullets_desc\":\"Main attack shoot extra projectiles diagonally foward.\",\n\"extra_round_desc\":\"Main attack shoot an extra round per reload.\",\n\"flame_desc\":\"Main attack inflicts moderate damage over time.\",\n\"forcefield_desc\":\"Repel nearby enemy entities in a zone.\",\n\"front_bullet_desc\":\"Main attack shoot an extra projectile in the front.\",\n\"health_up_desc\":\"Increases Max Health by 30%.\",\n\"holiness_up_desc\":\"3 is a holy number.\",\n\"knockback_desc\":\"Increases Bullet and Body knockback by 100%.\",\n\"lucky_wheel_desc\":\"Discard all current upgrade options.\",\n\"penetration_desc\":\"Bullet can pierce enemies, but deals less damage to subsequent ones.\n (Repeated upgrade increases pierce and damage further.)\",\n\"poison_desc\":\"Main attack inflicts slow sustained damage over time.\",\n\"pondering_desc\":\"Gains an extra upgrade option (maximum 9).\",\n\"random_option_desc\":\"Uhhh idk :(\",\n\"regeneration_desc\":\"Regenerates 7% of Base Max Health every time a room is cleared.\",\n\"reload_desc\":\"Increases Attack Speed by 30%.\",\n\"side_bullets_desc\":\"Main attack shoot extra projectiles on left and right.\",\n\"swiftness_desc\":\"Increases Movement Speed by 30%.\",\n\"tides_desc\":\"Increases Attack Speed by up to 50%(varies over time).\",\n\"wall_bounce_desc\":\"Main attack projectiles bounce off wall with decreased damage.\n(Repeated upgrade improves damage.)\",\n\n\n\"lang_name\":\"English\"}").commit();
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
