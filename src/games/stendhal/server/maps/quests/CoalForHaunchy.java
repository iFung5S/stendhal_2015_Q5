/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
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

import games.stendhal.server.entity.npc.SpeakerNPC;

import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateAskForQuest;
import games.stendhal.server.maps.quests.logic.StateCollectItems;

import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: Coal for Haunchy // Converted version \\
 *
 * PARTICIPANTS:
 * <ul>
 * <li>Haunchy Meatoch, the BBQ grillmaster on the Ados market</li>
 * </ul>
 *
 * STEPS:
 * <ul>
 * <li>Haunchy Meatoch asks you to fetch coal for his BBQ</li>
 * <li>Find some coal in Semos Mine or buy some from other players</li>
 * <li>Take the coal to Haunchy</li>
 * <li>Haunchy gives you a tasty reward</li>
 * </ul>
 *
 * REWARD:
 * <ul>
 * <li>Karma +25 in all</li>
 * <li>XP +200 in all</li>
 * <li>Some grilled steaks, random between 1 and 4.</li>
 * </ul>
 *
 * REPETITIONS:
 * <ul>
 * <li>You can repeat it each 2 days.</li>
 * </ul>
 * 
 * @author Vladislav Havalyov
 */

public class CoalForHaunchy extends StatedAbstractQuest 
{

	private static final String QUEST_SLOT = "coal_for_haunchy";

	// The delay between repeating quests is 48 hours or 2880 minutes
	private static final int REQUIRED_MINUTES = 2880;
	
	// All the name of the states in the quest
	public static final String STATE_START = "start_state";
	public static final String STATE_NEEDED_ITEMS = "coal=25;";
	public static final String STATE_COMPLETE = "done";	
	
	
	// Offer quest steps
	private static final String HAUNCHY_WANT_COAL = "I cannot use wood for this huge BBQ. To keep the heat I need some really old stone coal but there isn't much left. The problem is, that I can't fetch it myself because my steaks would burn then so I have to stay here. Can you bring me 25 pieces of #coal for my BBQ please?";
	private static final String HAUNCHY_RECEIVE_COAL = "I can go on with grilling my tasty steaks now! Thank you!";
	private static final String HAUNCHY_ASK_FOR_COAL_AGAIN = "The last coal you brought me is mostly gone again. Will you bring me some more?";
	private static final String HAUNCHY_DOES_NOT_NEED_MORE_COAL_FOR_NOW = "The coal amount behind my counter is still high enough. I will not need more at the moment";
	private static final String PLAYER_AGREE_GET_COAL = "Thank you! If you have found 25 pieces, say #coal to me so I know you have it. I'll be sure to give you a nice and tasty reward.";
	private static final String PLAYER_DOES_NOT_AGREE_GET_COAL = "Oh, never mind. I thought you love BBQs like I do. Bye then.";
	
	
	// The list that stores all the states
	private  List<AbstractQuestState> statesList = new LinkedList<AbstractQuestState>();
	


	@Override
	public void addToWorld()
	{
		fillQuestInfo(
				"Coal for Haunchy",
				"Haunchy Meatoch is afraid of his BBQ grillfire. Will his coal last till the steaks are ready or will he need some more?",
				true);
		SpeakerNPC npc = npcs.get("Haunchy Meatoch");
	
		// Set the quest to be repeatable
		setRepeatable(REQUIRED_MINUTES);
	
		
		// Create the start state
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUEST_SLOT, PLAYER_AGREE_GET_COAL,
															REQUIRED_MINUTES);
		startState.setPurposeQuest(HAUNCHY_WANT_COAL);
		startState.setPurposeAgain(HAUNCHY_ASK_FOR_COAL_AGAIN);
		startState.setCooldownReply(HAUNCHY_DOES_NOT_NEED_MORE_COAL_FOR_NOW);
		startState.setNoToQuest(PLAYER_DOES_NOT_AGREE_GET_COAL);
		statesList.add(startState);
				
		// Create the Collecting items state
		StateCollectItems collectingState = new StateCollectItems(npc, STATE_NEEDED_ITEMS, STATE_COMPLETE, QUEST_SLOT);
		collectingState.setFinishReply(HAUNCHY_RECEIVE_COAL);
		
		collectingState.setXP(200);
		collectingState.setKarmaAction(25);
		collectingState.addRewardItem("grilled steak", 2);	
		statesList.add(collectingState);
	
			
		// Add all the states into the World
		StatedAbstractQuest.addAllState(statesList);
		
	}

	
	@Override
	public String getSlotName()
	{
		return QUEST_SLOT;
	}

	@Override
	public String getName() 
	{
		return "CoalForHaunchy";
	}

	@Override
	public List<AbstractQuestState> getStateList(){
		return statesList;
	}	
	
	
	@Override
	public String getRegion()
	{
		return Region.ADOS_CITY;
	}

	@Override
	public String getNPCName()
	{
		return "Haunchy Meatoch";
	}
}
