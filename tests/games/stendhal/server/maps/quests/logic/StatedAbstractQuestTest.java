/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests.logic;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.quests.StatedAbstractQuest;
import games.stendhal.server.maps.semos.city.RudolphNPC;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateAskForQuest;
import games.stendhal.server.maps.quests.logic.StateCollectItems;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

/**
 * @author Hikmat Hajiyev
 * 
 * Abstract quest state
 *
 */
public class StatedAbstractQuestTest extends ZonePlayerAndNPCTestImpl{
	
	

	private static final class Mockquest extends StatedAbstractQuest {

	
		public List<AbstractQuestState> statesList = new LinkedList<AbstractQuestState>();
		
		public Mockquest() throws Exception {
			super();
		}

		@Override
		public List<AbstractQuestState> getStateList(){
			return statesList;
		}
	

		protected final NPCList npcs = SingletonRepository.getNPCList();
		
		@Override
		public void addToWorld() {

			SpeakerNPC npc1 = npcs.get(NPC_RUDOLPH);
			
			StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
					QUEST_DETAIL, REQUIRED_MINUTES);
			statesList.add(startState);
			
			StateCollectItems collectingState = new StateCollectItems(npc1, STATE_NEEDED_ITEMS, STATE_NEXT, QUESTSlotSTRING);
			collectingState.setFinishReply(STATE_FINISH);
			statesList.add(collectingState);
	    	StatedAbstractQuest.addAllState(testList);
	 	}

		@Override
		public String getSlotName() {
			return QUESTSlotSTRING;
		}

		@Override
		public String getName() {
			return QUESTSlotSTRING;
		}
		
	}
	
	public StatedAbstractQuestTest() throws Exception {
		super(ZONE_SEMOS, NPC_RUDOLPH);
	}


	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_SEMOS);
		final StendhalRPZone cityZone = new StendhalRPZone(ZONE_SEMOS);
		new RudolphNPC().configureZone(cityZone, null);
		MockStendlRPWorld.get();
	}
	@Override
	@Before
	public void setUp() {
		final StendhalRPZone cityZone = new StendhalRPZone(ZONE_SEMOS);
		new RudolphNPC().configureZone(cityZone, null);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}

	protected final NPCList npcs = SingletonRepository.getNPCList();
	public SpeakerNPC npc;
	
	private static final String ZONE_SEMOS = "0_semos_city";

	private static final String NPC_RUDOLPH = "Rudolph";

	private static String QUESTSlotSTRING = "TESTQUEST";

	public static final String STATE_START = "start_state";

	public static final String STATE_NEXT = "next_state";
	
	public static final String STATE_FINISH = "finish_state";
	
	public static final String STATE_NEEDED_ITEMS = "pie=1";

	private static final String QUEST_DETAIL = "test details";
	
	private static final int REQUIRED_MINUTES = 1;
	
	public static List<AbstractQuestState> testList = new LinkedList<AbstractQuestState>();
	

	/**
	 * Tests for addAllState. 
	 */
	@Test
	public final void testAddAllState() throws Exception {
		Mockquest quest = new Mockquest();
		quest.addToWorld();
		assertFalse(quest.statesList.size() == 0);
	}
	
	/**
	 * Tests for getStateList.
	 * @throws Exception 
	 */
	
	@Test
	public final void testGetStateList() throws Exception {
		Mockquest quest = new Mockquest();
		quest.addToWorld();
		int s = quest.getStateList().size();
		assertEquals(s, 2);
		testList = quest.getStateList();
		assertEquals(testList.get(0).getQUEST_SLOT(), QUESTSlotSTRING);
		assertEquals(testList.get(1).getQUEST_SLOT(), QUESTSlotSTRING);
	}
	/**
	 * Tests for addAllState and setRepetable. 
	 */
	@Test
	public final void testIsRepeatableAndSetRepetable() throws Exception {

		final Player pl = PlayerTestHelper.createPlayer("player");
		Mockquest quest = new Mockquest();
		quest.addToWorld();
		pl.setQuest(QUESTSlotSTRING, null);
		assertFalse(quest.isRepeatable(pl));
		quest.setRepeatable(1);
		// some time need to be passed
		assertFalse(quest.isRepeatable(pl));
		
	}
	

	/**
	 * Tests for getHistory. 
	 * @throws Exception
	 */
	@Test
	public final void testGetHistory() throws Exception {
		ArrayList<String> testList = new ArrayList<>();
		final Player pl = PlayerTestHelper.createPlayer("player");
		
		pl.setQuest(QUESTSlotSTRING, null);
		Mockquest quest = new Mockquest();
		quest.addToWorld();
		assertEquals(quest.getHistory(pl), testList);
		pl.setQuest(QUESTSlotSTRING, "done");
		testList.add(NPC_RUDOLPH + " asked me for a favour.");
		testList.add("I helped "+ npc +" just now, "+ npc +" doesn\'t need my help for now.");
		assertEquals(quest.getHistory(pl), testList);
	}
	
	
	
}
