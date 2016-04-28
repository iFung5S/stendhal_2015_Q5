

/***************************************************************************
 *                   (C) Copyright 2003-2015 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.ExamineChatAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateAskForQuest;
import games.stendhal.server.maps.quests.logic.StateCollectItems;
import games.stendhal.server.maps.quests.logic.StatePaperChase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: Introduce new players to game <p>PARTICIPANTS:<ul>
 * <li> Tad
 * <li> Margaret
 * <li> Ilisa
 * <li> Ketteh Wehoh
 * </ul>
 *
 * <p>
 * STEPS:<ul>
 * <li> Tad asks you to buy a flask to give it to Margaret.
 * <li> Margaret sells you a flask
 * <li> Tad thanks you and asks you to take the flask to Ilisa
 * <li> Ilisa asks you for a few herbs.
 * <li> Return the created dress potion to Tad.
 * <li> Ketteh Wehoh will reminder player about Tad, if quest is started but not complete.
 * </ul>
 * <p>
 * REWARD:<ul>
 * <li> 270 XP
 * <li> some karma (4)
 * <li> 10 gold coins
 * </ul>
 * <p>
 * REPETITIONS:<ul>
 * <li> None.
 * </ul>
 */
public class MedicineForTad extends StatedAbstractQuest {

	static final String ILISA_TALK_ASK_FOR_FLASK = "Medicine for #Tad? Didn't he tell you to bring a flask?";
	static final String ILISA_TALK_ASK_FOR_HERB = "Ah, I see you have that flask. #Tad needs medicine, right? Hmm... I'll need a #herb. Can you help?";
	static final String ILISA_TALK_DESCRIBE_HERB = "North of Semos, near the tree grove, grows a herb called #arandula. Here is a picture I drew so you know what to look for.";
	static final String ILISA_TALK_INTRODUCE_TAD = "He needs a very powerful potion to heal himself. He offers a good reward to anyone who will help him.";
	static final String ILISA_TALK_REMIND_HERB = "Can you fetch those #herbs for the #medicine?";
	static final String ILISA_TALK_PREPARE_MEDICINE = "Okay! Thank you. Now I will just mix these... a pinch of this... and a few drops... there! Can you ask #Tad to stop by and collect it? I want to see how he's doing. Tell him you have #finished.";
	static final String ILISA_TALK_EXPLAIN_MEDICINE = "The medicine that #Tad is waiting for.";

	static final String KETTEH_TALK_BYE_INTRODUCES_TAD = "Farewell. Have you met Tad, in the hostel? If you get a chance, please check in on him. I heard he was not feeling well. You can find the hostel in Semos village, close to Nishiya.";
	static final String KETTEH_TALK_BYE_REMINDS_OF_TAD = "Goodbye. Don't forget to check on Tad. I hope he's feeling better.";

	static final String TAD_TALK_GOT_FLASK = "Ok, you got the flask!";
	static final String TAD_TALK_REWARD_MONEY = "Here, take this money to cover your expense.";
	static final String TAD_TALK_FLASK_ILISA = "Now, I need you to take it to #Ilisa... she'll know what to do next.";
	static final String TAD_TALK_REMIND_FLASK_ILISA = "I need you to take a flask to Ilisa... she'll know what to do next.";
	static final String TAD_TALK_INTRODUCE_ILISA = "Ilisa is the summon healer at Semos temple.";
	static final String TAD_TALK_REMIND_MEDICINE = "*cough* I hope #Ilisa hurries with my medicine...";
	static final String TAD_TALK_COMPLETE_QUEST = "Thanks! I will go talk with #Ilisa as soon as possible.";

	static final String TAD_TALK_ASK_FOR_EMPTY_FLASK = "I'm not feeling well... I need to get a bottle of medicine made. Can you fetch me an empty #flask?";
	static final String TAD_TALK_ALREADY_HELPED_1 = "I'm alright now, thanks.";
	static final String TAD_TALK_ALREADY_HELPED_2 = "You've already helped me out! I'm feeling much better now.";
	static final String TAD_TALK_WAIT_FOR_FLASK = "*cough* Oh dear... I really need this medicine! Please hurry back with the flask from #Margaret.";
	static final String TAD_TALK_FLASK_MARGARET = "You could probably get a flask from #Margaret.";
	static final String TAD_TALK_INTRODUCE_MARGARET = "Margaret is the maid in the inn just down the street.";
	static final String TAD_TALK_CONFIRM_QUEST = "So, will you help?";
	static final String TAD_TALK_QUEST_REFUSED = "Oh, please won't you change your mind? *sneeze*";
	static final String TAD_TALK_QUEST_ACCEPTED = "Great! Please go as quickly as you can. *sneeze*";

	static final String HISTORY_MET_TAD = "I have met Tad in Semos Hostel.";
	static final String HISTORY_QUEST_OFFERED = "He asked me to buy a flask from Margaret in Semos Tavern.";
	static final String HISTORY_GOT_FLASK = "I got a flask and will bring it to Tad soon.";
	static final String HISTORY_TAKE_FLASK_TO_ILISA = "Tad asked me to take the flask to Ilisa at Semos Temple.";
	static final String HISTORY_ILISA_ASKED_FOR_HERB = "Ilisa asked me to get a herb called Arandula which I can find north of Semos, near the tree grove.";
	static final String HISTORY_GOT_HERB = "I found some Arandula herbs and will bring them to Ilisa.";
	static final String HISTORY_POTION_READY = "Ilisa created a powerful potion to help Tad. She asked me to tell him that it is ready.";
	static final String HISTORY_DONE = "Tad thanked me.";

	private static final String QUEST_SLOT = "introduce_players";
	
	// All the name of the states in the quest
	static final String STATE_START = "start_state";
	static final String STATE_NEEDED_ITEMS_TAD = "flask=1";
	static final String STATE_NEEDED_ITEMS_ILISA = "flask=1";
	static final String STATE_SHOWN_DRAWING = "shownDrawing";
	static final String STATE_NEEDED_ITEMS_HERB = "arandula=1";
	static final String STATE_TAD = "find tad";
	static final String STATE_COMPLETE = "done";
	
	private static final String PW_Tad = "finished";
	
	// The list that stores all the states
	private  List<AbstractQuestState> statesList = new LinkedList<AbstractQuestState>();

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	@Override
	public List<AbstractQuestState> getStateList(){
		return statesList;
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Medicine For Tad",
				"Tad, a boy in Semos Hostel, needs help to get his medicine.",
				false);
		// All the NPC involves
		final SpeakerNPC npc1 = npcs.get("Tad");
		//final SpeakerNPC npc2 = npcs.get("Margaret");
		final SpeakerNPC npc3 = npcs.get("Ilisa");
		//final SpeakerNPC npc4 = npcs.get("Ketteh Wehoh");
		
		// Create the start state
		StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_NEEDED_ITEMS_TAD, QUEST_SLOT, TAD_TALK_FLASK_MARGARET + " " + TAD_TALK_INTRODUCE_MARGARET);
		startState.setPurposeQuest(TAD_TALK_ASK_FOR_EMPTY_FLASK);
		startState.setYesToQuest(TAD_TALK_QUEST_ACCEPTED);
		startState.setNoToQuest(TAD_TALK_QUEST_REFUSED);
		statesList.add(startState);
		
		// Create the Collecting items state
		StateCollectItems collectingState = new StateCollectItems(npc1, STATE_NEEDED_ITEMS_TAD, STATE_NEEDED_ITEMS_ILISA , QUEST_SLOT);
		collectingState.setConversationPhraseForRequiredItem(Arrays.asList("flask"));
		collectingState.setFinishReply(TAD_TALK_GOT_FLASK + " " + TAD_TALK_REWARD_MONEY + " " + TAD_TALK_FLASK_ILISA + " " + TAD_TALK_INTRODUCE_ILISA);
		collectingState.setXP(10);
		collectingState.addRewardItem("money", 10);
		collectingState.addRewardItem("flask", 1);
		statesList.add(collectingState);
		
		// Create the Collecting items state
		StateCollectItems collectingState2 = new StateCollectItems(npc3, STATE_NEEDED_ITEMS_ILISA, STATE_SHOWN_DRAWING, QUEST_SLOT);
		collectingState2.setConversationPhraseForRequiredItem(Arrays.asList("flask"));
		collectingState2.setFinishReply(ILISA_TALK_INTRODUCE_TAD + " " + ILISA_TALK_ASK_FOR_HERB);
		collectingState2.setXP(10);
		statesList.add(collectingState2);
		
		ChatAction showArandulaDrawing = new ExamineChatAction("arandula.png", "Ilisa's drawing", "Arandula");
		ChatAction flagDrawingWasShown = new SetQuestAction(QUEST_SLOT, 1, STATE_NEEDED_ITEMS_HERB);
		npc3.add(
				ConversationStates.ATTENDING,
				Arrays.asList("yes", "ok"),
				new AndCondition(
						new QuestInStateCondition(QUEST_SLOT, 0, STATE_SHOWN_DRAWING),
						new NotCondition(new PlayerHasItemWithHimCondition("arandula"))),
				ConversationStates.ATTENDING,
				ILISA_TALK_DESCRIBE_HERB,
				new MultipleActions(showArandulaDrawing, flagDrawingWasShown));
		
		// Create the Collecting items state
		StateCollectItems collectingState3 = new StateCollectItems(npc3, STATE_NEEDED_ITEMS_HERB, STATE_TAD, QUEST_SLOT);
		collectingState3.setConversationPhraseForRequiredItem(Arrays.asList("arandula"));
		collectingState3.setFinishReply(ILISA_TALK_PREPARE_MEDICINE);
		collectingState3.setXP(50);
		statesList.add(collectingState3);
		
		// Create the Paper Chase state
		StatePaperChase paperChase = new StatePaperChase(npc1, STATE_TAD, STATE_COMPLETE, QUEST_SLOT, PW_Tad, TAD_TALK_COMPLETE_QUEST + " " + TAD_TALK_REMIND_MEDICINE);
		paperChase.setXP(200);
		statesList.add(paperChase);
		
		// Add all the states into the World
    	StatedAbstractQuest.addAllState(statesList);
		
	}
	@Override
	public String getName() {
		return "MedicineForTad";
	}

	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}
	@Override
	public String getNPCName() {
		return "Tad";
	}
}
