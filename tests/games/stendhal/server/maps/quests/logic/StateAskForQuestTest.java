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


import java.util.LinkedList;
import java.util.List;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.quests.StatedAbstractQuest;
import games.stendhal.server.maps.semos.city.RudolphNPC;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateAskForQuest;
import static org.junit.Assert.assertEquals;
import static utilities.SpeakerNPCTestHelper.getReply;

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
public class StateAskForQuestTest extends ZonePlayerAndNPCTestImpl{
	
	public StateAskForQuestTest() throws Exception {
		super(ZONE_SEMOS, NPC_RUDOLPH);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_SEMOS);
	}
	@Override
	@Before
	public void setUp() {
		final StendhalRPZone cityZone = new StendhalRPZone(ZONE_SEMOS);
		new RudolphNPC().configureZone(cityZone, null);
		npc = npcs.get(NPC_RUDOLPH);
		en = npc.getEngine();
		player = PlayerTestHelper.createPlayer("bob");	
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}

	protected final NPCList npcs = SingletonRepository.getNPCList();
	public static SpeakerNPC npc;
	private Engine en;
	private Player player;
	private static final String ZONE_SEMOS = "0_semos_city";

	private static final String NPC_RUDOLPH = "Rudolph";

	private static String QUESTSlotSTRING = "TESTQUEST";

	public static final String STATE_START = "start_state";

	public static final String STATE_NEXT = "next_state";
	
	public static final String STATE_FINISH = "finish_state";
	
	public static final String STATE_NEEDED_ITEMS = "pie=1";

	private static final String QUEST_DETAIL = "test details";
	
	private static final int REQUIRED_MINUTES = 1;
	private String OTHER_MESSAGE = "test";
	private static final String RUDOLPH_HI_MESSAGE= "Hi, my jolly friend. What a wonderful time of year this is!";
	
	public static List<AbstractQuestState> testList = new LinkedList<AbstractQuestState>();
	
	private String purposeQuest = "Can you please help me with something?";
	private String noToQuest = "Ok, maybe next time then...";
	private String yesToQuest = "Thanks!! You are so kind!";

	private String historyNote = NPC_RUDOLPH + " asked me for a favour.";
	/**
	 * Tests for initial messages. 
	 */
	@Test
	public final void testHiAndPurposeMessages() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);	
		testList.add(startState);
		StatedAbstractQuest.addAllState(testList);	
		player.setQuest(QUESTSlotSTRING, null);
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		
		
	}	
	/**
	 * Tests for yes to the quest. 
	 */
	@Test
	public final void testYesToTheQuest() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);	
		testList.add(startState);
		StatedAbstractQuest.addAllState(testList);	
		player.setQuest(QUESTSlotSTRING, null);
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "yes");
		assertEquals(getReply(npc), yesToQuest + " " + QUEST_DETAIL);	
	}		
	
	/**
	 * Tests for no to the quest. 
	 */
	@Test
	public final void testNoToTheQuest() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);	
		testList.add(startState);
		StatedAbstractQuest.addAllState(testList);	
		player.setQuest(QUESTSlotSTRING, null);
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "no");
		assertEquals(getReply(npc), noToQuest);		
	}	

	/**
	 * Tests for setPurposeQuest. 
	 */
	@Test
	public final void testSetPurposeQuest() throws Exception {
	StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);
	
		testList.add(startState);
		startState.setPurposeQuest(OTHER_MESSAGE);
		StatedAbstractQuest.addAllState(testList);	
		player.setQuest(QUESTSlotSTRING, null);
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest +" "+  OTHER_MESSAGE);	
	}	
	
	/**
	 * Tests for setYesToQuest. 
	 */
	@Test
	public final void testSetYesToQuest() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				"", REQUIRED_MINUTES);
		startState.setYesToQuest(OTHER_MESSAGE);
		testList.add(startState);
		StatedAbstractQuest.addAllState(testList);	
		player.setQuest(QUESTSlotSTRING, null);

		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "yes");
		assertEquals(getReply(npc), OTHER_MESSAGE+" ");
	}	
	
	/**
	 * Tests for setNoToQuest. 
	 */
	@Test
	public final void testsetNoToQuest() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);
		testList.add(startState);
		startState.setNoToQuest(OTHER_MESSAGE);
		StatedAbstractQuest.addAllState(testList);	

		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "no");
		assertEquals(getReply(npc), noToQuest + " " + OTHER_MESSAGE);
	}	
	/**
	 * Tests for getHistoryNote. 
	 */
	@Test
	public final void testGetHistoryNote() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);
		testList.add(startState);
		startState.setNoToQuest(OTHER_MESSAGE);
		StatedAbstractQuest.addAllState(testList);	

		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "yes");
		assertEquals(getReply(npc), yesToQuest + " " + QUEST_DETAIL);
		assertEquals(historyNote, startState.getHistoryNote(player));		
	}
	
	/**
	 * Tests for setQuestDescriptionForHistory. 
	 */
	@Test
	public final void testSetQuestDescriptionForHistory() throws Exception {
		StateAskForQuest startState = new StateAskForQuest(npc, STATE_START, STATE_NEEDED_ITEMS, QUESTSlotSTRING,
				QUEST_DETAIL, REQUIRED_MINUTES);
		startState.setQuestDescriptionForHistory(OTHER_MESSAGE);
		testList.add(startState);
		StatedAbstractQuest.addAllState(testList);	
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		en.step(player, "favor");
		assertEquals(getReply(npc), purposeQuest);	
		en.step(player, "yes");
		assertEquals(getReply(npc), yesToQuest + " " + QUEST_DETAIL);
		assertEquals(OTHER_MESSAGE, startState.getHistoryNote(player));	
	}
	
	
	
}
