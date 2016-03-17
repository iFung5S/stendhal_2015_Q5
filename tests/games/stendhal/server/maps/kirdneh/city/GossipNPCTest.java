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
package games.stendhal.server.maps.kirdneh.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.server.game.db.DatabaseFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

/**
 * Test making pies from Jef.
 * @author Cheuk Man Fung
 */
public class GossipNPCTest extends ZonePlayerAndNPCTestImpl {

	private static final String ZONE_NAME = "int_kirdneh_inn";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		new DatabaseFactory().initializeDatabase();
		MockStendlRPWorld.get();

		QuestHelper.setUpBeforeClass();

		setupZone(ZONE_NAME, new GossipNPC());
	}


	public GossipNPCTest() {
		super(ZONE_NAME, "Jef");
	}

	/**
	 * Tests for hiAndBye.
	 */
	@Test
	public void testHiAndBye() {
		final SpeakerNPC npc = getNPC("Jef");
		final Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi Jef"));
		assertEquals("Hello, pal! You have probably heard about the delicious pie "
		 	         + "I make and want me to #make one for you, is that right?", getReply(npc));

		assertTrue(en.step(player, "bye"));
		assertEquals("See you around.", getReply(npc));
	}

	/**
	 * Tests for makePie.
	 */
	@Test
	public void testMakePie() {
		final SpeakerNPC npc = getNPC("Jef");
		final Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi"));

		// Player does not have the required ingredients
		assertTrue(en.step(player, "make"));
		assertEquals("I can only make a cheese and onion pie if you bring me 2 #'sacks of flour', "
				+ "4 #'pieces of cheese', and an #onion.", getReply(npc));

		assertTrue(en.step(player, "bye"));

		// Equip player with enough ingredients to make a cheese and onion pie
		assertTrue(equipWithItem(player, "flour"));
		assertTrue(equipWithItem(player, "flour"));
		assertTrue(equipWithItem(player, "cheese"));
		assertTrue(equipWithItem(player, "cheese"));
		assertTrue(equipWithItem(player, "cheese"));
		assertTrue(equipWithItem(player, "cheese"));
		assertTrue(equipWithItem(player, "onion"));

		assertTrue(en.step(player, "hi"));

		// Player now has the required ingredients
		assertTrue(en.step(player, "make"));
		assertEquals("I need you to fetch me 2 #'sacks of flour', 4 #'pieces of cheese', "
				+"and an #onion for this job, which will take 1 minute. Do you have what I need?", getReply(npc));

		assertTrue(en.step(player, "yes"));
		String reply = getReply(npc);
		// If respond time is short, 1 minute is left. Otherwise, just slightly less than 1 minute is left.
		// So both cases are correct.
		assertTrue(reply.equals("OK, I will make a cheese and onion pie for you, but that will take some time. "
				+"Please come back in 1 minute.")
				||
				reply.equals("OK, I will make a cheese and onion pie for you, but that will take some time. "
				+"Please come back in less than a minute."));

		assertTrue(en.step(player, "bye"));

	}

}
