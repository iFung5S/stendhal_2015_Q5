/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 206 - Univ of Manchester                *
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

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.kalavan.castle.OffworldTouristNPC;
import games.stendhal.server.maps.semos.temple.BooksellerNPC;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

public class SouvenirsForSarkTest extends ZonePlayerAndNPCTestImpl {

	private static final String SARK_ZONE_NAME = "0_kalavan_castle";
	private static final String BOOKSELLER_ZONE_NAME = "0_ados_city_n";

	private static final String SARK_NAME = "Sark";
	private static final String FIDOREA_NAME = "Fidorea";

	private Player player;
	private SpeakerNPC sarkNpc;
	private Engine en;

	private AbstractQuest quest;
	private String questSlot;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(SARK_ZONE_NAME);
		setupZone(BOOKSELLER_ZONE_NAME);
	}

	public SouvenirsForSarkTest() {
		super(SARK_ZONE_NAME, SARK_NAME);
	}

	@Override
	@Before
	public void setUp() {
		StendhalRPZone kalavanCastleZone = new StendhalRPZone(SARK_ZONE_NAME);
		new OffworldTouristNPC().configureZone(kalavanCastleZone, null);
		
		StendhalRPZone booksellerZone = new StendhalRPZone(BOOKSELLER_ZONE_NAME);
		new BooksellerNPC().configureZone(booksellerZone, null);

		// For Quick Quest Challenge, change this to whatever code loads the quest through your new mechanism
		quest = new SouvenirsForSark();
		quest.addToWorld();
		
		questSlot = quest.getSlotName();

		player = PlayerTestHelper.createPlayer("bob");
		sarkNpc = SingletonRepository.getNPCList().get(SARK_NAME);
	}
	
	@Override
	@After
	public void tearDown() {
		en.step(player, "bye");
	}

	
	private String startConversationWith(SpeakerNPC thisNpc) {
		en = thisNpc.getEngine();

		en.step(player, "hi");
		return getReply(thisNpc);
	}
	
	
	@Test
	public void testDescribeSark() {
		assertEquals("You see a small alien-like creature.  It is carrying a strange flat box, which it looks at often.", sarkNpc.getDescription());
	}
	
	@Test
	public void testSayingHelloAndGoodbye() {
		String response = startConversationWith(sarkNpc);
		assertEquals("Hello.  Bonjour.  Nnọọ.  Buna.", response);
		
		en.step(player, "bye");
		assertEquals("Goodbye.  Au revoir.  Ka ọ dị.  La revedere.", getReply(sarkNpc));
	}
	
	@Test
	public void testAskingAboutJob() {
		startConversationWith(sarkNpc);
		
		en.step(player, "job");
		assertEquals("Back home, I am a transferase reduction specialist.  Just now, I am visiting your world as an envoy for my people.", getReply(sarkNpc));
	}

	@Test
	public void testAcceptQuest() {
		player.setQuest(questSlot, null);

		startConversationWith(sarkNpc);

		en.step(player, "quest");
		assertEquals("I'm making a record of my visit.  Will you help me gather some representative objects from your culture?", getReply(sarkNpc));

		en.step(player, "yes");
		assertEquals("Thank you.  Merci.  Daalụ.  Mulțumesc.  " +
				     "I hope to find an opened envelope, a blank scroll, a map and a note.  " +
				     "This amazing communication technology you have will fascinate the people back home.", getReply(sarkNpc));

		assertEquals("start", player.getQuest(questSlot));
		assertHistory("I met an offworld visitor called Sark.",
					  "Sark asked me to help find some souvenirs to take home and I promised to help.");
	}
	
	@Test
	public void testRefuseQuest() {
		player.setQuest(questSlot, null);

		startConversationWith(sarkNpc);

		en.step(player, "quest");
		assertEquals("I'm making a record of my visit.  Will you help me gather some representative objects from your culture?", getReply(sarkNpc));

		en.step(player, "no");
		assertEquals("Hmmmm... did I say something to offend?  Offworld diplomacy is so challenging.", getReply(sarkNpc));

		assertEquals("rejected", player.getQuest(questSlot));
		assertHistory("I met an offworld visitor called Sark.", "It asked me for help but I decided not to.");
	}
	
	@Test
	public void testReturnWithoutTheItems() {
		player.setQuest(questSlot, "start");

		String firstReply = startConversationWith(sarkNpc);

		assertEquals("Hello again.  Did you forget what I asked you to bring?  " +
					 "Don't be embarrassed.  Your low brain-to-body ratio is not your fault.", firstReply);
		assertEquals("start", player.getQuest(questSlot));
	}

	@Test
	public void testReturnWithTheItemsAndAcceptScrapbookRequest() {
		player.setQuest(questSlot, "start");

		equipPlayerWithDocuments();
		
		String firstReply = startConversationWith(sarkNpc);
		assertEquals("Hello again.  Ah, I see you brought the items.  Excellent!  " +
					 "But now I need something to transport them in.  " +
					 "A lady I met in Ados told me about a thing called a #scrapbook.  She makes them.  " +
					 "If you are passing, perhaps you could acquire one for me?", firstReply);

		checkThatSarkHasTakenTheItems();
		
		en.step(player, "yes");
		assertEquals("Thank you.  Merci.  Daalụ.  Mulțumesc.", getReply(sarkNpc));
		assertEquals("start2", player.getQuest(questSlot));

		assertHistory("I met an offworld visitor called Sark.",
				  	  "Sark asked me to help find some souvenirs to take home and I promised to help.",
				  	  "I brought the documents but now Sark needs a scrapbook.",
				  	  "I said I would get one and bring it back.");
	}

	@Test
	public void testReturnWithTheItemsAndRefuseScrapbookRequest() {
		player.setQuest(questSlot, "start");

		equipPlayerWithDocuments();
		
		String firstReply = startConversationWith(sarkNpc);
		assertEquals("Hello again.  Ah, I see you brought the items.  Excellent!  " +
					 "But now I need something to transport them in.  " +
					 "A lady I met in Ados told me about a thing called a #scrapbook.  She makes them.  " +
					 "If you are passing, perhaps you could acquire one for me?", firstReply);

		checkThatSarkHasTakenTheItems();
		
		en.step(player, "no");
		assertEquals("That is a pity.  I don't think I will visit Ados again on this trip.", getReply(sarkNpc));
		assertEquals("refused2", player.getQuest(questSlot));

		assertHistory("I met an offworld visitor called Sark.",
				  	  "Sark asked me to help find some souvenirs to take home and I promised to help.",
				  	  "I brought the documents but now Sark needs a scrapbook.",
				  	  "I decided not to help out any further.");
	}
	
	@Test
	public void testAskAboutScrapbook() {
		player.setQuest(questSlot, "start2");
		
		startConversationWith(sarkNpc);
		
		en.step(player, "scrapbook");
		assertEquals("A most fascinating item.  The lady said she would design one especially for me.  " +
					 "I think she was called Fidorea.", getReply(sarkNpc));
	}

	@Test
	public void testGetScrapBookFromFidorea() {
		player.setQuest(questSlot, "start2");
		SpeakerNPC fidoreaNpc = SingletonRepository.getNPCList().get(FIDOREA_NAME);

		startConversationWith(fidoreaNpc);

		en.step(player, "scrapbook");
		assertEquals("Ah, have you come for the scrapbook for Sark?  It's finished.  Here you are.", getReply(fidoreaNpc));

		PlayerTestHelper.equipWithItem(player, "scrapbook");
		assertEquals("scrapbook", player.getQuest(questSlot));
	}
	
	@Test
	public void testAskForScrapBookFromFidoreaWhenNotOnQuest() {
		String questState = player.getQuest(questSlot);
		
		SpeakerNPC fidoreaNpc = SingletonRepository.getNPCList().get(FIDOREA_NAME);

		startConversationWith(fidoreaNpc);

		en.step(player, "scrapbook");
		assertEquals("So you have heard about my scrapbooks?  Maybe I shall open a book shop one day.", getReply(fidoreaNpc));

		assertFalse(player.isEquipped("scrapbook"));
		assertEquals(questState, player.getQuest(questSlot));
	}

	@Test
	public void testBackToSarkWithTheScrapbook() {
		player.setQuest(questSlot, "scrapbook");
		PlayerTestHelper.equipWithItem(player, "scrapbook");
		int initialXp = player.getXP();

		String firstReply = startConversationWith(sarkNpc);
		assertEquals("Hello once again.  I see you have the scrapbook from Fidorea.  Many thanks.", firstReply);

		assertFalse(player.isEquipped("scrapbook"));
		assertEquals(500, player.getXP() - initialXp);
		assertEquals("done", player.getQuest(questSlot));
		
		assertHistory("I met an offworld visitor called Sark.",
			  	  "Sark asked me to help find some souvenirs to take home and I promised to help.",
			  	  "I brought the documents but now Sark needs a scrapbook.",
			  	  "I said I would get one and bring it back.",
			  	  "I returned the scrapbook to Sark, who was very pleased.");
	}
	
	@Test
	public void testBackToSarkWithoutTheScrapbook() {
		player.setQuest(questSlot, "scrapbook");

		String firstReply = startConversationWith(sarkNpc);
		assertEquals("Hello once again.  You have not been back to Ados, yet, I see.  " +
					 "I hope you will get the chance soon.  My ship will return for me soon.", firstReply);

		assertFalse(player.isEquipped("scrapbook"));
		assertEquals("scrapbook", player.getQuest(questSlot));
	}
	
	@Test
	public void testBackToSarkWhenQuestAlreadyDone() {
		player.setQuest(questSlot, "done");

		String firstReply = startConversationWith(sarkNpc);
		assertEquals("Hello, friend.  I hope you are well.", firstReply);
		
		en.step(player, "quest");
		assertEquals("I thank you, but I have no further room for any more mass in my quantum destabilising travel pocket.", getReply(sarkNpc));
		assertEquals("done", player.getQuest(questSlot));
	}


	private void assertHistory(String... entries) {
		assertEquals(Arrays.asList(entries), quest.getHistory(player));
	}
	
	private void equipPlayerWithDocuments() {
		PlayerTestHelper.equipWithItem(player, "opened envelope");
		PlayerTestHelper.equipWithItem(player, "blank scroll");
		PlayerTestHelper.equipWithItem(player, "note");
	}
	
	private void checkThatSarkHasTakenTheItems() {
		assertFalse(player.isEquipped("opened envelope"));
		assertFalse(player.isEquipped("blank scroll"));
		assertFalse(player.isEquipped("note"));
	}
}