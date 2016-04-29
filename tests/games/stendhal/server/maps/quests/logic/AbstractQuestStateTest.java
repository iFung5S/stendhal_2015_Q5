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

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.semos.city.RudolphNPC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import games.stendhal.server.entity.player.Player;

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
public class AbstractQuestStateTest extends ZonePlayerAndNPCTestImpl{
	
	

	private static final class Mockquest extends AbstractQuestState {

		public Mockquest(SpeakerNPC npc, String thisState, String nextState,
				String QUEST_SLOT) {
			super(npc, thisState, nextState, QUEST_SLOT);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getHistoryNote(Player player) {
			return QUESTHISTORYNOTE;
		}

		@Override
		public void addToWorld() {
			// do nothing
		}
	}
	
	public AbstractQuestStateTest() throws Exception {
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

	private SpeakerNPC npc;
	private Engine en;
	
	private static final String ZONE_SEMOS = "0_semos_city";

	private static final String NPC_RUDOLPH = "Rudolph";

	private static String QUESTSlotSTRING = "TESTQUEST";
	
	private static String QUESTHISTORYNOTE = "test note";

	private static String THIS_STATE = "test this state";
	
	private static String NEXT_STATE = "test next state";

	/**
	 * Tests for quest object.
	 */
	@Test
	public final void testQuestObject() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest  quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		assertFalse(quest.equals(null));
	}	

	/**
	 * Tests for getHistoryNote.
	 */
	@Test
	public final void testGetHistoryNote() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		assertEquals(quest.getHistoryNote(pl), QUESTHISTORYNOTE);
	}

	/**
	 * Tests for getQUEST_SLOT.
	 */
	@Test
	public final void testGetQUEST_SLOT() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest  quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		assertEquals(quest.getQUEST_SLOT(), QUESTSlotSTRING);
	}	

	/**
	 * Tests for getState.
	 */
	@Test
	public final void testGetState() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		assertEquals(quest.getState(), THIS_STATE);
	}	

	/**
	 * Tests for getNextState.
	 */
	@Test
	public final void testGetNextState() {
		System.out.println("hi");
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		System.out.println("Next state"  + quest.getNextState());
		assertEquals(quest.getNextState(), NEXT_STATE);
	}	


	/**
	 * Tests for getNPC.
	 */
	@Test
	public final void testGetNPC() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		pl.setQuest(QUESTSlotSTRING, null);
		final Mockquest quest = new Mockquest(npc, THIS_STATE, NEXT_STATE, QUESTSlotSTRING);
		assertEquals(quest.getNPC(), npc);
	}

}
