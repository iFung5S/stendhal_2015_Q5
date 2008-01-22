package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.ados.swamp.DeathmatchRecruiterNPC;
import marauroa.common.Log4J;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;

public class AdosDeathmatchTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log4J.init();
		PlayerTestHelper.generateNPCRPClasses();
		MockStendlRPWorld.get();
		
	}
	
	@AfterClass
	public static void teardownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}
	
	
	@Test
	public void testRecruiter() throws Exception {
		DeathmatchRecruiterNPC configurator =  new DeathmatchRecruiterNPC();
		StendhalRPZone zone = new StendhalRPZone("dmTestZone");
		StendhalRPZone ados_wall_n = new StendhalRPZone("0_ados_wall_n", 200, 200);
		MockStendlRPWorld.get().addRPZone(ados_wall_n);

		configurator.configureZone(zone, null);
		SpeakerNPC recruiter = SingletonRepository.getNPCList().get("Thonatus");
		assertNotNull(recruiter);
		Player dmPlayer = PlayerTestHelper.createPlayer("dmPlayer");
		Engine en = recruiter.getEngine();
		assertEquals(ConversationStates.IDLE, en.getCurrentState());
		en.step(dmPlayer, "hi");
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals("Hey there. You look like a reasonable fighter.", recruiter.get("text"));
		
		en.step(dmPlayer, "job");
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals("I'm recruiter for the Ados #deathmatch.", recruiter.get("text"));
		
		en.step(dmPlayer, "deathmatch");
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals("The deathmatch is the ultimate challenge for true #heroes.", recruiter.get("text"));
		
		en.step(dmPlayer, "heroes");
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals("Are you such a hero? I can take you to the #challenge.", recruiter.get("text"));
		
		dmPlayer.setLevel(19);
		en.step(dmPlayer, "challenge");
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals("Sorry, you are too weak!", recruiter.get("text"));
		recruiter.remove("text");
		
		en.step(dmPlayer, "bye");
		assertEquals(ConversationStates.IDLE, en.getCurrentState());
		assertEquals("I hope you will enjoy the Ados #Deathmatch!", recruiter.get("text"));
		

		
		dmPlayer.setLevel(20);
		en.step(dmPlayer, "hi");
		recruiter.remove("text");
		en.step(dmPlayer, "challenge");
		
		assertEquals(ConversationStates.ATTENDING, en.getCurrentState());
		assertEquals(null, recruiter.get("text"));
		assertEquals(ados_wall_n, dmPlayer.getZone());
		assertEquals(100, dmPlayer.getX());
		
		en.setCurrentState(ConversationStates.IDLE);
		
		Player joiner = PlayerTestHelper.createPlayer("dmPlayer");
		joiner.setLevel(19);
		en.step(joiner, "hi");
		en.step(joiner, "challenge");
		assertEquals(null, joiner.getZone());
		joiner.setLevel(20);
		
		en.step(joiner, "challenge");
		assertEquals(ados_wall_n, joiner.getZone());
		
	}

	
}
