package games.stendhal.server.maps.ados.bar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.ExpressionType;
import games.stendhal.common.parser.Sentence;
import games.stendhal.common.parser.SentenceImplementation;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.NPC;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;

public class LonJathamNPCTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log4J.init();
		MockStendlRPWorld.get();
	}

	/**
	 * Tests for configureZone.
	 */
	@Test
	public void testConfigureZone() {
		
		SingletonRepository.getRPWorld();
		final LonJathamNPC barmaidConfigurator = new LonJathamNPC();

		final StendhalRPZone zone = new StendhalRPZone("testzone");
		barmaidConfigurator.configureZone(zone, null);
		assertFalse(zone.getNPCList().isEmpty());
		final NPC latham = zone.getNPCList().get(0);
		assertThat(latham.getName(), is("Lon Jatham"));
		assertThat(latham.getDescription(), is("Lon Jatham knows everything about Java."));
	}
	
	/**
	 * Tests for hiandBye.
	 */
	@Test
	public void testHiandBye() {
		SingletonRepository.getRPWorld();
		final LonJathamNPC lathamConfigurator = new LonJathamNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		lathamConfigurator.configureZone(zone, null);
		final SpeakerNPC latham = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(latham.getName(), is("Lon Jatham"));
		final Engine engine = latham.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(PlayerTestHelper.createPlayer("bob"), sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(latham), is("Welcome to #Stendhal! Built with #Java it's like arcade, except it works."));

		sentence = new SentenceImplementation(new Expression("bye", ExpressionType.VERB));
		engine.step(PlayerTestHelper.createPlayer("bob"), sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(latham), is("Remember, 80 chars per line or I will hunt you down."));
	}
	
	/**
	 * Conversation responses
	 */
	@Test
	public void testConversationBehaviour() {
		SingletonRepository.getRPWorld();

		final LonJathamNPC lathamConfigurator = new LonJathamNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		lathamConfigurator.configureZone(zone, null);
		final SpeakerNPC barMaid = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(barMaid.getName(), is("Lon Jatham"));
		final Engine engine = barMaid.getEngine();
		engine.setCurrentState(ConversationStates.ATTENDING);

		Sentence sentence0 = new SentenceImplementation(new Expression("java", ExpressionType.VERB));
		engine.step(PlayerTestHelper.createPlayer("bob"), sentence0);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat("java", getReply(barMaid), equalTo("Java is awesome, buy my #book."));
		
		Sentence sentence1 = new SentenceImplementation(new Expression("book", ExpressionType.VERB));
		engine.step(PlayerTestHelper.createPlayer("bob"), sentence1);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat("book", getReply(barMaid), equalTo("Yes! It can teach you everything! Get down to SSO in Kilburn now!"));
		
		Sentence sentence2 = new SentenceImplementation(new Expression("stendhal", ExpressionType.VERB));
		engine.step(PlayerTestHelper.createPlayer("bob"), sentence2);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat("stendhal", getReply(barMaid), equalTo("Stendhal is completely #free software."));
		
	}

}