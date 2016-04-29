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
package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.semos.hostel.BoyNPC;
import games.stendhal.server.maps.semos.temple.HealerNPC;
import games.stendhal.server.maps.semos.townhall.DecencyAndMannersWardenNPC;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

public class MedicineForTadTest extends ZonePlayerAndNPCTestImpl {

	private static final String HOSTEL_ZONE_NAME = "int_semos_hostel";
	private static final String TEMPLE_ZONE_NAME = "int_semos_temple";
	private static final String TOWNHALL_ZONE_NAME = "int_semos_townhall";

	private static final String TAD_TALK_SSSHH_COME_HERE = "Ssshh! Come here, player! I have a #task for you.";

	private Player player;
	private SpeakerNPC npc;
	private Engine en;

	private AbstractQuest quest;
	private String questSlot;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		setupZone(HOSTEL_ZONE_NAME);
		setupZone(TEMPLE_ZONE_NAME);
		setupZone(TOWNHALL_ZONE_NAME);
	}

	public MedicineForTadTest() {
		super(HOSTEL_ZONE_NAME, "Tad");
	}

	@Override
	@Before
	public void setUp() {
		StendhalRPZone hostelZone = new StendhalRPZone(HOSTEL_ZONE_NAME);
		new BoyNPC().configureZone(hostelZone, null);
		
		StendhalRPZone templeZone = new StendhalRPZone(TEMPLE_ZONE_NAME);
		new HealerNPC().configureZone(templeZone, null);
		
		StendhalRPZone townhallZone = new StendhalRPZone(TOWNHALL_ZONE_NAME);
		new DecencyAndMannersWardenNPC().configureZone(townhallZone, null);

		quest = new MedicineForTad();
		quest.addToWorld();
		new MeetKetteh().addToWorld();
		
		questSlot = quest.getSlotName();

		player = PlayerTestHelper.createPlayer("player");
		player.setQuest("TadFirstChat", "done");
	}
	
	@Override
	@After
	public void tearDown() {
		en.step(player, "bye");
	}

	private String startTalkingToNpc(String name) {
		npc = SingletonRepository.getNPCList().get(name);
		en = npc.getEngine();

		en.step(player, "hi");
		return getReply(npc);
	}
	
	@Test
	public void testKettehIntroducesTad() {
		player.setQuest(questSlot, null);

		startTalkingToNpc("Ketteh Wehoh");

		en.step(player, "bye");
		assertEquals("Bye.", getReply(npc));
	}
	
	@Test
	public void testKettehRemindsOfTad() {
		player.setQuest(questSlot, MedicineForTad.STATE_START);

		startTalkingToNpc("Ketteh Wehoh");
		
		en.step(player, "bye");
		assertEquals("Bye.", getReply(npc));
	}

	@Test
	public void testAcceptQuest() {
		player.setQuest(questSlot, null);

		String firstReply = startTalkingToNpc("Tad");
		assertEquals(TAD_TALK_SSSHH_COME_HERE, firstReply);

		en.step(player, "quest");
		assertEquals(MedicineForTad.TAD_TALK_ASK_FOR_EMPTY_FLASK, getReply(npc));

		en.step(player, "yes");
		String expectedReply = MedicineForTad.TAD_TALK_QUEST_ACCEPTED + " " + MedicineForTad.TAD_TALK_FLASK_MARGARET + " "
								+ MedicineForTad.TAD_TALK_INTRODUCE_MARGARET;
		assertEquals(expectedReply, getReply(npc));

		assertEquals("flask=1", player.getQuest(questSlot));
	}
	
	@Test
	public void testRefuseQuest() {
		player.setQuest(questSlot, null);

		String firstReply = startTalkingToNpc("Tad");
		assertEquals(TAD_TALK_SSSHH_COME_HERE, firstReply);

		en.step(player, "quest");
		assertEquals(MedicineForTad.TAD_TALK_ASK_FOR_EMPTY_FLASK, getReply(npc));

		en.step(player, "no");
		assertEquals(MedicineForTad.TAD_TALK_QUEST_REFUSED, getReply(npc));

		assertEquals(null, player.getQuest(questSlot));
	}

	@Test
	public void testBackToTadWithFlask() {
		player.setQuest(questSlot, "flask=1");
		PlayerTestHelper.equipWithItem(player, "flask");

		String firstReply = startTalkingToNpc("Tad");
		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!" , firstReply);
		
		en.step(player, "flask");
		assertEquals("I'd still need a #flask. Have you brought any?", getReply(npc));

		en.step(player, "flask");
		String expectedReply = MedicineForTad.TAD_TALK_GOT_FLASK + " " 
				+ MedicineForTad.TAD_TALK_REWARD_MONEY + " "
				+ MedicineForTad.TAD_TALK_FLASK_ILISA + " "
				+ MedicineForTad.TAD_TALK_INTRODUCE_ILISA;
		assertEquals(expectedReply, getReply(npc));
		assertEquals("flask=1", player.getQuest(questSlot));
	}

	@Test
	public void testGoToIlisaWithFlask() {
		player.setQuest(questSlot, "flask=1");
		PlayerTestHelper.equipWithItem(player, "flask");

		String firstReply = startTalkingToNpc("Ilisa");
		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!", firstReply);
		
		en.step(player, "flask");
		assertEquals("I'd still need a #flask. Have you brought any?", getReply(npc));
		
		en.step(player, "flask");
		assertEquals(MedicineForTad.ILISA_TALK_INTRODUCE_TAD + " " + MedicineForTad.ILISA_TALK_ASK_FOR_HERB, getReply(npc));
		
		assertEquals("shownDrawing", player.getQuest(questSlot));

		en.step(player, "yes");
		assertEquals("North of Semos, near the tree grove, grows a herb called #arandula. Here is a picture I drew so you know what to look for.", getReply(npc));
	}

	@Test
	public void testBackToTadWithoutPotion() {
		player.setQuest(questSlot, "flask=1");
		PlayerTestHelper.equipWithItem(player, "flask");

		String firstReply = startTalkingToNpc("Tad");

		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!", firstReply);
	}

	@Test
	public void testBackToIlisaWithoutHerb() {
		player.setQuest(questSlot, "arandula=1");
		PlayerTestHelper.equipWithItem(player, "flask");

		String firstReply = startTalkingToNpc("Ilisa");
		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!", firstReply);
		
		en.step(player, "arandula");
		assertEquals("I'd still need a #'sprig of arandula'. Have you brought any?", getReply(npc));
	}

	@Test
	public void testBackToIlisaWithHerb() {
		player.setQuest(questSlot, "arandula=1");
		PlayerTestHelper.equipWithItem(player, "flask");
		PlayerTestHelper.equipWithItem(player, "arandula");

		String firstReply = startTalkingToNpc("Ilisa");

		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!", firstReply);
		
		assertEquals("arandula=1", player.getQuest(questSlot));
	}

	@Test
	public void testBackToTadWithPotion() {
		player.setQuest(questSlot, "find tad");

		startTalkingToNpc("Tad");

		en.step(player, "finished");
		assertEquals(MedicineForTad.TAD_TALK_COMPLETE_QUEST + " " + MedicineForTad.TAD_TALK_REMIND_MEDICINE, getReply(npc));
		
		assertEquals("done", player.getQuest(questSlot, 0));
	}
	
	@Test
	public void testKettehDoesNotMentionTad() {
		player.setQuest(questSlot, "done");

		startTalkingToNpc("Ketteh Wehoh");

		en.step(player, "bye");
		assertEquals("Bye.", getReply(npc));
	}
	
	@Test
	public void testTalkAboutHerbsBeforeStarting() {
		// Test for bug #5839. Saying "herbs" to Ilisa broke the quest state
		// in a way that it was not possible to start the quest.
		player.setQuest(questSlot, null);
		startTalkingToNpc("Ilisa");
		en.step(player, "herbs");
		assertFalse(new QuestStartedCondition(questSlot).fire(player, null, null));
	}

}
