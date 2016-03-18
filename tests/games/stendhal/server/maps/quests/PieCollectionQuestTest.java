/***************************************************************************
 *                   (C) Copyright 2003-2013 - Stendhal                    *
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

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.semos.mountain.PieCollectorNPC;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

/*
*
* @author Suhasini Suresh
*
*
*/

/*private static final String HISTORY_DEFAULT = "Peter asked me for all kind of hot pies for his stomach.";
	private static final String HISTORY_REJECTED = "I decided not find Peter any pie, someone else will help him.";
	private static final String HISTORY_START = "I still need to bring Peter " + Grammar.enumerateCollection(missingItems.toStringList()) + ".";
	private static final String HISTORY_REPEATABLE = "It's been a while since I brought Peter hot pies, I wonder if the he is hungry again?";
	private static final String HISTORY_COMPLETED_NOT_REPEATABLE ="I brought Peter lots of pies, he don't want to see any pie for now.";
*/
public class PieCollectionQuestTest extends ZonePlayerAndNPCTestImpl {

   private Player player = null;
   private SpeakerNPC npc = null;
   private Engine en = null;
   
   private String questSlot;
   private static final String ZONE_SEMOS = "0_semos_mountains_n2_w";
   private static final String NPC_PETER = "Peter";

   /*final ItemCollection missingItems = new ItemCollection();
	missingItems.addFromQuestStateString(questState);
	*/
   
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		 setupZone(ZONE_SEMOS);
	}

public PieCollectionQuestTest() {
		super(ZONE_SEMOS, NPC_PETER);
	}

@Override
@Before
	public void setUp() throws Exception {
   final StendhalRPZone mountains = new StendhalRPZone(ZONE_SEMOS);
		new PieCollectorNPC().configureZone(mountains, null);
		AbstractQuest quest = new PieCollection();
		quest.addToWorld();
		questSlot = quest.getSlotName();
		
  player = PlayerTestHelper.createPlayer("sally");
	}

/**
	 * Tests for getSlotName.
	 */
	@Test
	public final void testGetSlotName() {
		final PieCollection pc = new PieCollection();
		assertEquals("pies_peter", pc.getSlotName());
	}

@Test
	public void testQuest() {
		npc = SingletonRepository.getNPCList().get(NPC_PETER);
		en = npc.getEngine();


		// -----------------------------------------------


		// -----------------------------------------------

		// [19:04] Welcome to Stendhal. Need help? http://stendhalgame.org/wiki/AskForHelp - please report problems, suggestions and bugs. Remember to keep your password completely secret, never tell to another friend, player, or admin.

		// -----------------------------------------------

		// [19:04] Synchronized

		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals("Hello, I am Peter, the king of 0_semos_mountain_n2_w.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "food");

		// -----------------------------------------------

		assertEquals("I WANT FOOD!!!! Save me please!! I love #pies...", getReply(npc));

		// -----------------------------------------------

		en.step(player, "pie");

		// -----------------------------------------------

		assertEquals("There are not much hot food around here. I really want to eat some pies. Could you please get me some pies?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "no");

		// -----------------------------------------------

		assertEquals(	"Ok, just let me starve to death then...", getReply(npc));

		// -----------------------------------------------

		en.step(player, "bye");

		// -----------------------------------------------

		assertEquals("Have a nice day! Don't kill my goats by the way.", getReply(npc));

		// -----------------------------------------------


		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals("Hello, I am Peter, the king of 0_semos_mountain_n2_w.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "quest");

		// -----------------------------------------------

		assertEquals("Are you willing to find me some hot pies yet?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "yes");

		// -----------------------------------------------

		assertEquals("You are so kind!! I'd like all kind of pie: an #'apple pie', a #'cherry pie', a #'fish pie', and a #pie.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "bye");

		// -----------------------------------------------

		assertEquals("Have a nice day! Don't kill my goats by the way.", getReply(npc));

		// -----------------------------------------------

		PlayerTestHelper.equipWithStackableItem(player, "apple pie", 1);

		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals(	"Hello again. If you've brought me some hot #pies as my food, I'll happily take them!", getReply(npc));

		// -----------------------------------------------

		en.step(player, "pie");

		// -----------------------------------------------

		assertEquals("I'd still like an #'apple pie', a #'cherry pie', a #'fish pie', and a #pie. Have you brought any?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "apple pie");

		// -----------------------------------------------

		assertEquals("Wonderful! Did you bring anything else with you?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "no");

		// -----------------------------------------------

		assertEquals("Oh, that's a shame, do tell me when you find some. I'd still like a #'cherry pie', a #'fish pie', and a #pie.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "bye");

		// -----------------------------------------------

		assertEquals("Have a nice day! Don't kill my goats by the way.", getReply(npc));

		// -----------------------------------------------

		PlayerTestHelper.equipWithStackableItem(player, "cherry pie", 9);

		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals("Hello again. If you've brought me some hot #pies as my food, I'll happily take them!", getReply(npc));

		// -----------------------------------------------

		en.step(player, "pie");

		// -----------------------------------------------

		assertEquals("I'd still like a #'cherry pie', a #'fish pie', and a #pie. Have you brought any?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "yes");

		// -----------------------------------------------

		assertEquals("Wonderful, which pie have you brought?", getReply(npc));

		// -----------------------------------------------

		en.step(player, "cherry pie");
		
		// -----------------------------------------------

		PlayerTestHelper.equipWithStackableItem(player, "pie", 1);
		PlayerTestHelper.equipWithStackableItem(player, "fish pie", 1);
		
		final int xp = player.getXP();
		final double karma = player.getKarma();
		
		en.step(player, "pie");
		en.step(player, "fish pie");

		// -----------------------------------------------
		
		assertEquals("I have never been this happy before!!! If you want, you can have closer look of me goat."
								+ " Here are some rewards for you.", getReply(npc));

		// -----------------------------------------------
		
		// [19:05] pinch earns 50 experience points.
		assertTrue(player.isEquipped("milk"));
		assertTrue(player.isEquipped("cheese"));
		assertThat(player.getXP(), greaterThan(xp));
		assertThat(player.getKarma(), greaterThan(karma));

		// -----------------------------------------------

		en.step(player, "bye");

		// -----------------------------------------------

		assertEquals("Have a nice day! Don't kill my goats by the way.", getReply(npc));

		// -----------------------------------------------


		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals("Hello, I am Peter, the king of 0_semos_mountain_n2_w.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "food");

		// -----------------------------------------------

		assertEquals(	"I am sooooooooo full, let me take a rest....", getReply(npc));

		// -----------------------------------------------

		en.step(player, "bye");

		// -----------------------------------------------

		assertEquals("Have a nice day! Don't kill my goats by the way.", getReply(npc));

		// -----------------------------------------------


		// -----------------------------------------------

		en.step(player, "hi");

		// -----------------------------------------------

		assertEquals("Hello, I am Peter, the king of 0_semos_mountain_n2_w.", getReply(npc));

		// -----------------------------------------------

		en.step(player, "pie");

		// -----------------------------------------------

		player.setQuest(questSlot, "done;0");
	}
}
