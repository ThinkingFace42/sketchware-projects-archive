package com.my.cardbattle;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
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
import android.widget.Button;
import android.widget.TextView;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	private TextView textview1;
	private Button home_btn;
	
	private Intent intent = new Intent();
	private SharedPreferences cards_filesaver;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		textview1 = findViewById(R.id.textview1);
		home_btn = findViewById(R.id.home_btn);
		cards_filesaver = getSharedPreferences("cards", Activity.MODE_PRIVATE);
		
		home_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void initializeLogic() {
		_Load_default_cards();
		intent.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void _Load_default_cards() {
		cards_filesaver.edit().clear().commit();
		cards_filesaver.edit().putString("Holy 3", "{\"name\":\"Holy 3\",\"cost\":\"3\",\"desc\":\"3 is a holy number.\",\"damage\":\"3\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Tribute to a bird", "{\"name\":\"Tribute to a bird\",\"cost\":\"788\", \"desc\":\"Give me 788\",\"damage\":\"788\",\"target\":\"self\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Cleave", "{\"name\":\"Cleave\",\"cost\":\"2\",\"desc\":\"Deal 12 damage to all enemy\", \"damage_enemies\":\"12\", \"target\":\"enemy\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("This card has no cost and can't be played", "{\"name\":\"This card has no cost and can't be played\",\"desc\":\"This card is here to annoy you\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Adrenaline", "{\"name\":\"Adrenaline\",\"desc\":\"Gain 1 AP and draw 2 cards. Exhaust.\",\"give_card\":\"2\",\"give_ap\":\"1\",\"on_play_destination\":\"exhaust\",\"cost\":\"0\",\"target\":\"self\",\"tag\":\"{rarity=rare}\"}").commit();
		cards_filesaver.edit().putString("Energy surge", "{\"name\":\"Energy surge\",\"desc\":\"Gain 1 AP \",\"give_ap\":\"1\",\"cost\":\"0\",\"target\":\"self\",\"tag\":\"{rarity=basic}\"}").commit();
		cards_filesaver.edit().putString("Useless card in library", "{\"name\":\"Useless card in library\",\"desc\":\"This card is for testing library functionality\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Useless card in library that you can get rid of", "{\"name\":\"Useless card in library that you can get rid of\",\"desc\":\"This card is for testing exhaust functionality\",\"cost\":\"3\",\"on_play_destination\":\"exhaust\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Strike", "{\"name\":\"Strike\",\"cost\":\"2\",\"desc\":\"Deal 10 damage to an enemy\", \"damage\":\"10\", \"target\":\"enemy\",\"tag\":\"{rarity=basic,strike=1}\"}").commit();
		cards_filesaver.edit().putString("Double strike", "{\"name\":\"Double strike\",\"cost\":\"3\",\"desc\":\"Deal 10 damage to an enemy twice\", \"damage\":\"10\",\"attack_modifiers\":\"{count=2}\",\"target\":\"enemy\",\"tag\":\"{rarity=common,strike=1}\"}").commit();
		cards_filesaver.edit().putString("Defend", "{\"name\":\"Defend\",\"cost\":\"2\",\"desc\":\"Gain 8 block\", \"block_self\":\"8\", \"target\":\"self\",\"tag\":\"{rarity=basic}\"}").commit();
		cards_filesaver.edit().putString("Fancy defend", "{\"name\":\"Fancy defend\",\"cost\":\"2\",\"desc\":\"Gain 8 block but with script instead of attributes\", \"on_play_script\":\"BLOCK_SELF 8;\", \"target\":\"self\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Card conjurer", "{\"name\":\"Card conjurer\",\"cost\":\"0\", \"desc\":\"Conjure a named card to hand. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a card name\\\",\\\"choice_name\\\":\\\"card_name\\\",\\\"type\\\":\\\"text\\\"}]\",\"add_text_choice_to_hand\":\"1\",\"on_play_destination\":\"hand\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("AP surge", "{\"name\":\"AP surge\",\"desc\":\"Gain 1000 AP \",\"give_ap\":\"1000\",\"cost\":\"0\",\"target\":\"self\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Reinforce", "{\"name\":\"Reinforce\",\"cost\":\"2\",\"desc\":\"Gain block equal to current block, clamped to between 6 and 100\", \"on_play_script\":\"\nCLAMP b 6 ACTOR_BLOCK 100;\nBLOCK_SELF b;\n\", \"target\":\"self\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Fortify", "{\"name\":\"Fortify\",\"cost\":\"2\",\"desc\":\"Gain 8 block. You don't lose block at the start of next turn\", \"block_self\":\"8\",\"block_modifiers\":\"{block_duration=2}\", \"target\":\"self\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Body slam", "{\"name\":\"Body slam\",\"cost\":\"2\",\"desc\":\"Deal damage to an enemy equal to current block, up to 1000\", \"on_play_script\":\"\nCLAMP b 0 ACTOR_BLOCK 1000;\nATTACK b;\n\", \"target\":\"enemy\",\"tag\":\"{rarity=uncommon}\"}").commit();
		cards_filesaver.edit().putString("Discard outlet", "{\"name\":\"Discard outlet\",\"cost\":\"0\", \"desc\":\"Select any number of cards from your hand and discard them\",\"choices\":\"[{\\\"desc\\\":\\\"Choose cards to discard\\\",\\\"choice_name\\\":\\\"card_in_hand\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"hand\\\"}]\",\"discard_card_choice\":\"1\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Name tag", "{\"name\":\"Name tag\",\"cost\":\"2\",\"desc\":\"Rename an entity to a string of your choice\", \"on_play_script\":\"ASSIGN_STRING name name;\nMAP_PUT TARGET name entity_name;\", \"choices\":\"[{\\\"desc\\\":\\\"Enter the new name\\\",\\\"choice_name\\\":\\\"entity_name\\\",\\\"type\\\":\\\"text\\\"}]\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Mind control", "{\"name\":\"Mind control\",\"cost\":\"4\",\"desc\":\"Convert target entity to your team\", \"on_play_script\":\"ASSIGN_STRING group group;\nMAP_GET ourgroup ACTOR group;\nMAP_PUT TARGET group ourgroup;\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Script runner", "{\"name\":\"Script runner\",\"cost\":\"0\", \"desc\":\"Run script in a textbox. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a script\\\",\\\"choice_name\\\":\\\"script\\\",\\\"type\\\":\\\"text\\\",\\\"num_lines\\\":\\\"6\\\"}]\",\"run_text_choice\":\"1\",\"on_play_destination\":\"hand\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Critical strike", "{\"name\":\"Critical strike\",\"cost\":\"3\",\"desc\":\"Discard up to a card. If you discarded a strike type card this way, deal 25 damage to an enemy, otherwise deal 10 damage to that enemy\",\n\"choices\":\"[{\\\"desc\\\":\\\"Choose up to one card to discard\\\",\\\"choice_name\\\":\\\"card_choice\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"hand\\\",\\\"max_choice\\\":\\\"1\\\"}]\",\n\"on_play_script\":\"\nMAP_FROM_STRING card_choice card_choice;\nMAP_UNPACK card_choice;\nNUM_LIST_FROM_STRING idlist chosen_cards_id;\nLIST_LENGTH l idlist;\nIF EQ l 0;\nATTACK 10;\nELSE;\nLIST_GET id idlist 0 DOUBLE;\nLIST_GET card CARDS id MAP;\nASSIGN_STRING tag tag;\nMAP_GET cardtag card tag;\nMAP_FROM_STRING tagmap cardtag;\nASSIGN_STRING strike strike;\nMAP_GET result tagmap strike;\nIF EQ result EMPTY_STRING;\nATTACK 10;\nELSE;\nATTACK 25;\nEND_IF;\nDISCARD_CARD_ID id;\nEND_IF;\",\n\"target\":\"enemy\",\"tag\":\"{rarity=common,strike=1}\"}").commit();
		cards_filesaver.edit().putString("Blood wall", "{\"name\":\"Blood wall\",\"cost\":\"3\",\"desc\":\"Gain 17 block and lose 3 health\", \"block_self\":\"17\",\"lose_health_self\":\"3\", \"target\":\"self\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Striking spree", "{\"name\":\"Striking spree\",\"cost\":\"2\",\"desc\":\"All strike type card in your hand costs 2 less this turn\", \"on_play_script\":\"IDLIST_TO_CARDLIST cardsinhand HAND_ID_LIST;\nLIST_LENGTH l cardsinhand;\nASSIGN counter 0;\nWHILE LT counter l;\nLIST_GET card cardsinhand counter MAP;\nASSIGN_STRING tag tag;\nMAP_GET cardtag card tag;\nMAP_FROM_STRING tagmap cardtag;\nASSIGN_STRING strike strike;\nMAP_GET result tagmap strike;\nIF NEQ result EMPTY_STRING;\nASSIGN_STRING ccft cost_change_for_turn;\nMAP_ADD_DOUBLE card ccft -2;\nEND_IF;\nADD counter counter 1;\nEND_WHILE;\", \"target\":\"self\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Striking preparation", "{\"name\":\"Striking preparation\",\"cost\":\"1\",\"desc\":\"Look at top 5 cards of your library. Put all strike type cards into your hand and the rest into discard\",\"choices\":\"[{\\\"desc\\\":\\\"Look at the top 5 cards of your library...\\\",\\\"choice_name\\\":\\\"card_choice\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"library\\\",\\\"source_count\\\":\\\"5\\\",\\\"max_choice\\\":\\\"0\\\"}]\",\n\"on_play_script\":\"\nMAP_FROM_STRING card_choice card_choice;\nMAP_UNPACK card_choice;\nNUM_LIST_FROM_STRING idlist card_choices;\nLIST_LENGTH l idlist;\nASSIGN counter 0;\nWHILE LT counter l;\nLIST_GET id idlist counter DOUBLE;\nLIST_GET card CARDS id MAP;\nASSIGN_STRING tag tag;\nMAP_GET cardtag card tag;\nMAP_FROM_STRING tagmap cardtag;\nASSIGN_STRING strike strike;\nMAP_GET result tagmap strike;\nIF NEQ result EMPTY_STRING;\nDRAW_CARD_ID id;\nELSE;\nMOVE_CARD_ID id DISCARD_ID_LIST LIBRARY_ID_LIST;\nEND_IF;\nADD counter counter 1;\nEND_WHILE;\",\n\"target\":\"self\",\"tag\":\"{rarity=uncommon}\"}").commit();
		cards_filesaver.edit().putString("Script runner 2", "{\"name\":\"Script runner 2\",\"cost\":\"0\", \"desc\":\"Run Kyrre's script in a textbox. Return this card to your hand\",\"choices\":\"[{\\\"desc\\\":\\\"Enter a script\\\",\\\"choice_name\\\":\\\"script\\\",\\\"type\\\":\\\"text\\\",\\\"num_lines\\\":\\\"6\\\"}]\",\"run_text_choice\":\"1\",\"script_language\":\"mewtype\",\"on_play_destination\":\"hand\",\"tag\":\"{rarity=debug}\"}").commit();
		cards_filesaver.edit().putString("Quick plan", "{\"name\":\"Quick plan\",\"cost\":\"1\",\"desc\":\"Draw a card if your library is not empty, that card costs 2 less to play this turn.\", \"on_play_script\":\"\nLIST_LENGTH l LIBRARY_ID_LIST;\nIF GEQ l 1;\nLIST_GET id LIBRARY_ID_LIST 0 DOUBLE;\nLIST_GET card CARDS id MAP;\nASSIGN_STRING ccft cost_change_for_turn;\nMAP_PUT_DOUBLE card ccft -2;\nDRAW_CARD_ID id;\nEND_IF;\", \"target\":\"self\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Bash", "{\"name\":\"Bash\",\"cost\":\"3\",\"desc\":\"Deal 13 damage to an enemy. Inflict 2 vulnerable\", \"damage\":\"13\",\"attack_modifiers\":\"{vulnerable_duration=2}\", \"target\":\"enemy\",\"tag\":\"{rarity=common}\"}").commit();
		cards_filesaver.edit().putString("Flash bomb", "{\"name\":\"Flash bomb\",\"cost\":\"4\",\"desc\":\"Deal 8 damage to all enemy. Inflict 1 stun. Exhaust\", \"damage_enemies\":\"8\",\"attack_modifiers\":\"{stunned_duration=1}\", \"target\":\"enemy\",\"on_play_destination\":\"exhaust\",\"tag\":\"{rarity=rare}\"}").commit();
		cards_filesaver.edit().putString("Crushing blast", "{\"name\":\"Crushing blast\",\"cost\":\"4\",\"desc\":\"Choose and discard up to two strike card. If you discard 2, deal 30 damage to an enemy, inflict 1 stun and this card gains exhaust. Otherwise deal 18 damage to that enemy\",\n\"choices\":\"[{\\\"desc\\\":\\\"Choose up to two strike cards to discard\\\",\\\"choice_name\\\":\\\"card_choice\\\",\\\"type\\\":\\\"card\\\",\\\"source\\\":\\\"hand\\\",\\\"max_choice\\\":\\\"2\\\"}]\",\n\"on_play_script\":\"\nMAP_FROM_STRING card_choice card_choice;\nMAP_UNPACK card_choice;\nNUM_LIST_FROM_STRING idlist chosen_cards_id;\nLIST_LENGTH l idlist;\nASSIGN strikecount 0;\nASSIGN loop 0;\nWHILE LT loop l;\nLIST_GET id idlist loop DOUBLE;\nLIST_GET card CARDS id MAP;\nASSIGN_STRING tag tag;\nMAP_GET cardtag card tag;\nMAP_FROM_STRING tagmap cardtag;\nASSIGN_STRING strike strike;\nMAP_GET result tagmap strike;\nIF EQ result EMPTY_STRING;\nELSE;\nDISCARD_CARD_ID id;\nADD strikecount strikecount 1;\nEND_IF;\nADD loop loop 1;\nEND_WHILE;\nIF GEQ strikecount 2;\nATTACK 30;\nASSIGN_STRING sd stunned_duration;\nINFLICT_EFFECT sd 1;\nASSIGN_STRING ex exhaust;\nASSIGN_STRING opd on_play_destination;\nMAP_PUT CARD opd ex;\nELSE;\nATTACK 18;\nEND_IF;\n\",\n\"target\":\"enemy\",\"tag\":\"{rarity=uncommon}\"}").commit();
		cards_filesaver.edit().putString("Energy drink", "{\"name\":\"Energy drink\",\"desc\":\"Gain 1 AP at the start of next 4 turns \",\"field_turn_start_give_ap\":\"1\",\"cost\":\"2\",\"target\":\"self\",\"on_play_destination\":\"field\",\"max_field_time\":\"5\",\"tag\":\"{rarity=uncommon}\"}").commit();
	}
	
}