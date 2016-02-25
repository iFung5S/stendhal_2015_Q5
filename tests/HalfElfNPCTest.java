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

import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.ados.market.HalfElfNPC;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.ZonePlayerAndNPCTestImpl;

/**
 * Test know elf.
 * @author Suhasini Suresh
 */
public class HalfElfNPCTest extends ZonePlayerAndNPCTestImpl {

	private static final String ZONE_NAME = "int_ados_Aerianna_house";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
        setupZone(ZONE_NAME, new HalfElfNPC());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	public HalfElfNPCTest() {
		super(ZONE_NAME, "Aerianna");
	}

	/**
	 * Tests for knowing elves.
	 */
	@Test
	public void testAeriannaKnowsAboutElves() {
		final SpeakerNPC npc = getNPC("Aerianna");
		final Engine en = npc.getEngine();

		assertTrue(en.step(player, "hi"));
		assertEquals("Greetings, #friend. I am happy to see you.", getReply(npc));

		assertTrue(en.step(player, "job"));
		assertEquals("I am a half-elf, my #father was a #human adventurer and my #mother is an #elf from #Nalwor. As I want to understand both cultures I travel through #Faiumoni and learn as much as I can about #cultures and habits.", getReply(npc));
		
		assertTrue(en.step(player, "elf"));
		assertEquals("Elves are very proud to be beautiful, intelligent and magical. I don't say that because I am a half-elf! *hihi* They live seperated from other races in the forest or at other places where magic is strong. Elves get very old, even if you don't see that from their appearance. If you ever can, make friends with some elves as you might learn a lot from them!", getReply(npc));
	}

}
