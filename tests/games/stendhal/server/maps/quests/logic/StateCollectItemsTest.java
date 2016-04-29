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


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//import static org.hamcrest.Matchers.greaterThan;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.quests.StatedAbstractQuest;
import games.stendhal.server.maps.semos.city.RudolphNPC;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateCollectItems;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;
import utilities.RPClass.ItemTestHelper;

/**
 * @author Hikmat Hajiyev
 * 
 * Abstract quest state
 *
 */
public class StateCollectItemsTest extends ZonePlayerAndNPCTestImpl{
	
	public StateCollectItemsTest() throws Exception {
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
		player = PlayerTestHelper.createPlayer("player");	
		initialKarma = player.getKarma();
		initialXP = player.getXP();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}

	protected final NPCList npcs = SingletonRepository.getNPCList();
	public static SpeakerNPC npc;
	public static double initialKarma;
	public static int initialXP;
	private Engine en;
	private Player player;
	private static final String ZONE_SEMOS = "0_semos_city";

	private static final String NPC_RUDOLPH = "Rudolph";

	private static String QUEST_SLOT = "TEST";

	public static final String STATE_START = "start_state";

	public static final String STATE_NEXT = "next_state";
	

	public static final String STATE_NEEDED_ITEMS = "pie=1";
	public static final String NEEDED_ITEMS = "a pie";
	public static final String NEEDED_ITEMS_IN_NOTE = "1 pie";
	public static final String NEEDED_ITEMS_WITH_HASHTAG = "a #pie";

		private static final String RUDOLPH_HI_MESSAGE= "Hi, my jolly friend. What a wonderful time of year this is!";
	
	public static List<AbstractQuestState> testList = new LinkedList<AbstractQuestState>();
	
	
	private String STATE_FINISH = "I have never been this happy before!!!Thank you!!";
	/**
	 * Tests for initial messages and addToWorld() and setFinishReply(). 
	 */
	@Test
	public final void testStateCollectItems() throws Exception {
		StateCollectItems collectingState = new StateCollectItems(npc, STATE_NEEDED_ITEMS, STATE_NEXT, QUEST_SLOT);
		collectingState.setFinishReply(STATE_FINISH);
		testList.add(collectingState);
		collectingState.addToWorld();
		StatedAbstractQuest.addAllState(testList);	
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		player.setQuest(QUEST_SLOT, STATE_NEEDED_ITEMS);
		en.step(player, "quest");
		assertEquals(getReply(npc), "I'd still need "+NEEDED_ITEMS_WITH_HASHTAG+". Have you brought any?");
		en.step(player, "pie");
		assertEquals(getReply(npc),"You don't have "+NEEDED_ITEMS+" with you!");			
		en.step(player, "yes");
		assertEquals(getReply(npc),"Wonderful, which one have you brought?");			
		en.step(player, "no");
		assertEquals(getReply(npc),"Oh, that's a shame, do tell me when you find some. I'd still need " + NEEDED_ITEMS_WITH_HASHTAG + ".");
		final Item item = ItemTestHelper.createItem("pie");
		item.setEquipableSlots(Arrays.asList("bag"));
		player.equipToInventoryOnly(item);
		assertTrue(player.isEquipped("pie"));
		player.equipOrPutOnGround(item);
		en.step(player, "quest");
		assertEquals(getReply(npc), "I'd still need "+NEEDED_ITEMS_WITH_HASHTAG+". Have you brought any?");
		en.step(player, "pie");
		assertEquals(getReply(npc),STATE_FINISH);			
	}	
	/**
	 * Tests for setKarmaAction(), setXP() and getHistoryNote(); 
	 */
	@Test
	public final void testXPandKarma() throws Exception {
		StateCollectItems collectingState = new StateCollectItems(npc, STATE_NEEDED_ITEMS, "done", QUEST_SLOT);
		testList.add(collectingState);
		collectingState.addToWorld();
		collectingState.addRewardItem("coal", 1);
		collectingState.setKarmaAction(100);
		collectingState.setXP(100);
		StatedAbstractQuest.addAllState(testList);	
		en.step(player, "hi");
		assertEquals(getReply(npc), RUDOLPH_HI_MESSAGE);
		player.setQuest(QUEST_SLOT, STATE_NEEDED_ITEMS);
		assertEquals(player.getKarma(), initialKarma, 0.1);	
		assertEquals(player.getXP(), initialXP);		
		en.step(player, "quest");
		assertEquals(getReply(npc), "I'd still need "+NEEDED_ITEMS_WITH_HASHTAG+". Have you brought any?");
		assertEquals(collectingState.getHistoryNote(player), "I still need to bring "+NPC_RUDOLPH+" "+NEEDED_ITEMS_IN_NOTE + ".");
		en.step(player, "bye");
		assertEquals(getReply(npc),"It was such a pleasure to meet you.");
		en.step(player, "hi");
		assertEquals(getReply(npc),"Hello again. If you've brought me the things I need, I'll happily take them!");
	
		final int xp = player.getXP();
		
		PlayerTestHelper.equipWithStackableItem(player, "pie", 2);
		en.step(player, "quest");
		assertEquals(getReply(npc), "I'd still need "+NEEDED_ITEMS_WITH_HASHTAG+". Have you brought any?");
		en.step(player, "pie");
		player.setQuest(QUEST_SLOT, STATE_NEXT);
		assertEquals(getReply(npc), STATE_FINISH);
		en.step(player, "bye");
		assertEquals(getReply(npc),"It was such a pleasure to meet you.");
		assertEquals(player.getXP(), xp);
	}	
}
