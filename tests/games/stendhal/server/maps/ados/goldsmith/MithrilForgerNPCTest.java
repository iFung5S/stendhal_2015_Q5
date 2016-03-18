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
package games.stendhal.server.maps.ados.felinashouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.MockStendlRPWorld;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;
import utilities.RPClass.CatTestHelper;

/**
 * Test buying cats.
 * @author Martin Fuchs
 */
public class MithrilForgerNPCTest extends ZonePlayerAndNPCTestImpl {

	private static final String ZONE_NAME = "int_ados_goldsmith";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		
		CatTestHelper.generateRPClasses();
		QuestHelper.setUpBeforeClass();

		setupZone(ZONE_NAME, new MithrilForgerNPC());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Tests for hiAndBye.
	 */
	@Test
	public void testHiAndBye() {
		final SpeakerNPC npc = getNPC("PedingHaus");
		final Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi PedingHaus"));
		assertEquals("Greetings. I sense you may be interested in mithril. If you desire me to #make you a #'mithril bar' or #stealth #ring just say the word.", getReply(npc));

		assertTrue(en.step(player, "bye"));
		assertEquals("Bye.", getReply(npc));
	}

	/**
	 * Tests for PedingHaus.
	 */
	@Test
	public void testPeinghaus() {
		final SpeakerNPC npc = getNPC("Pedinghaus");
		final Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi"));
		assertEquals("Greetings. I sense you may be interested in mithril. If you desire me to #make you a #'mithril bar' or #stealth #ring just say the word.", getReply(npc));

		assertTrue(en.step(player, "mithril ore"));
		assertEquals("Nowadays these rare nuggets are only likely to be found in the Ados mountains. I have no idea if that area is still civilised...", getReply(npc));

		assertTrue(en.step(player, "mithril nugget"));
		assertEquals("Nowadays these rare nuggets are only likely to be found in the Ados mountains. I have no idea if that area is still civilised...", getReply(npc));

		assertTrue(en.step(player, "stealth ring"));
		assertEquals("One ring to rule them all, one ring to find them all, one ring to bring them and in the darkness bind them...", getReply(npc));

		assertTrue(en.step(player, "stealth"));
		assertEquals("One ring to rule them all, one ring to find them all, one ring to bring them and in the darkness bind them...", getReply(npc));

		assertTrue(en.step(player, "ring"));
		assertEquals("One ring to rule them all, one ring to find them all, one ring to bring them and in the darkness bind them...", getReply(npc));

		assertTrue(en.step(player, "mithril"));
		assertEquals("Mithril is an incredibly valuable commodity, as it makes armor of astounding strength, yet remains featherlight. Guard any mithril stash you own with great care.", getReply(npc));

		assertTrue(en.step(player, "bar"));
		assertEquals("Mithril is an incredibly valuable commodity, as it makes armor of astounding strength, yet remains featherlight. Guard any mithril stash you own with great care.", getReply(npc));

		assertTrue(en.step(player, "mithril bar"));
		assertEquals("Mithril is an incredibly valuable commodity, as it makes armor of astounding strength, yet remains featherlight. Guard any mithril stash you own with great care.", getReply(npc));






		assertTrue(en.step(player, "make"));
		assertEquals("What do you want me to make?", getReply(npc));

	
		assertTrue(en.step(player, "bye"));
		assertEquals("Bye.", getReply(npc));
	}

	

}
