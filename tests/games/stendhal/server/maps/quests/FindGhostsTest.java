package games.stendhal.server.maps.quests;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.StringContains.containsString;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.ados.city.KidGhostNPC;
import games.stendhal.server.maps.ados.hauntedhouse.WomanGhostNPC;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;

public class FindGhostsTest {

	private Player player = null;
	private SpeakerNPC npc = null;
	private Engine en = null;
	private SpeakerNPC npcGhost = null;
	private Engine enGhost = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
	}

	@Before
	public void setUp() {
		StendhalRPZone zone = new StendhalRPZone("admin_test");
		ZoneConfigurator zoneConf = new WomanGhostNPC();
		zoneConf.configureZone(zone, null);
		npc = SingletonRepository.getNPCList().get("Carena");
		en = npc.getEngine();

		zoneConf = new KidGhostNPC();
		zoneConf.configureZone(zone, null);

		zoneConf = new games.stendhal.server.maps.athor.cave.GhostNPC();
		zoneConf.configureZone(zone, null);

		zoneConf = new games.stendhal.server.maps.orril.dungeon.GhostNPC();
		zoneConf.configureZone(zone, null);

		zoneConf = new games.stendhal.server.maps.wofol.house5.GhostNPC();
		zoneConf.configureZone(zone, null);

		AbstractQuest quest = new FindGhosts();
		quest.addToWorld();
		en = npc.getEngine();

		player = PlayerTestHelper.createPlayer("player");
	}

	@org.junit.After
	public void tearDown() throws Exception {
		SingletonRepository.getNPCList().clear();
	}

	@Test
	public void testRejectQuest() {
		en.step(player, "hi");
		assertEquals("Wooouhhhhhh!", npc.get("text"));
		en.step(player, "help");
		assertEquals(
				"Here is a warning: if you die, you will become a ghost like me, partially visible and intangible. But if you can find your way out of the afterlife, you will be reborn.",
				npc.get("text"));
		en.step(player, "task");
		assertEquals(
				"I feel so lonely. I only ever see creatures and alive people. If I knew about #spirits like me, I would feel better.",
				npc.get("text"));
		en.step(player, "spirits");
		assertEquals(
				"I sense that there are 4 other spirits, but if only I knew their names I could contact them. Will you find them, then come back and tell me their names?",
				npc.get("text"));
		en.step(player, "no");
		assertEquals("rejected", player.getQuest("find_ghosts"));
		assertEquals("Oh. Never mind. Perhaps since I'm only a ghost I couldn't offer you much reward anyway.",
				npc.get("text"));
		en.step(player, "bye");
		assertEquals("Bye", npc.get("text"));
	}

	@Test
	public void testAcceptQuest() {
		en.step(player, "hi");
		assertEquals("Wooouhhhhhh!", npc.get("text"));
		en.step(player, "task");
		assertEquals(
				"I feel so lonely. I only ever see creatures and alive people. If I knew about #spirits like me, I would feel better.",
				npc.get("text"));
		en.step(player, "yes");
		assertEquals("That's lovely of you. Good luck searching for them.", npc.get("text"));
		en.step(player, "hi");
		en.step(player, "bye");
		assertEquals("looking:said", player.getQuest("find_ghosts"));
		assertEquals("Bye", npc.get("text"));
	}

	@Test
	public void testRejectthenAcceptQuest() {
		en.step(player, "hi");
		assertEquals("Wooouhhhhhh!", npc.get("text"));
		en.step(player, "help");
		assertEquals(
				"Here is a warning: if you die, you will become a ghost like me, partially visible and intangible. But if you can find your way out of the afterlife, you will be reborn.",
				npc.get("text"));
		en.step(player, "task");
		assertEquals(
				"I feel so lonely. I only ever see creatures and alive people. If I knew about #spirits like me, I would feel better.",
				npc.get("text"));
		en.step(player, "spirits");
		assertEquals(
				"I sense that there are 4 other spirits, but if only I knew their names I could contact them. Will you find them, then come back and tell me their names?",
				npc.get("text"));
		en.step(player, "no");
		assertEquals("rejected", player.getQuest("find_ghosts"));
		assertEquals("Oh. Never mind. Perhaps since I'm only a ghost I couldn't offer you much reward anyway.",
				npc.get("text"));
		en.step(player, "bye");
		assertEquals("Bye", npc.get("text"));

		en.step(player, "hi");
		assertEquals("Wooouhhhhhh!", npc.get("text"));
		assertTrue(en.step(player, "task"));
		assertEquals(
				"I feel so lonely. I only ever see creatures and alive people. If I knew about #spirits like me, I would feel better.",
				npc.get("text"));
		en.step(player, "yes");
		assertEquals("That's lovely of you. Good luck searching for them.", npc.get("text"));
		en.step(player, "hi");
		en.step(player, "bye");
		assertEquals("looking:said", player.getQuest("find_ghosts"));
		assertEquals("Bye", npc.get("text"));
	}

	@Test
	public void testQuest() {
		player.setXP(28900);
		player.setQuest("find_ghosts", "looking:said");
		assertEquals("looking:said", player.getQuest("find_ghosts"));

		int oldxp = player.getXP();
		npcGhost = SingletonRepository.getNPCList().get("Mary");
		enGhost = npcGhost.getEngine();
		enGhost.step(player, "hi");
		assertEquals("Remember my name ... Mary ... Mary ...", npcGhost.get("text"));
		assertEquals(oldxp + 100, player.getXP());
		assertThat(player.getQuest("find_ghosts") , containsString("Mary"));

		// [22:26] superkym earns 100 experience points.

		// -----------------------------------------------
		assertThat(player.getQuest("find_ghosts") , not(containsString("Ben")));

		oldxp = player.getXP();
		npcGhost = SingletonRepository.getNPCList().get("Ben");
		enGhost = npcGhost.getEngine();
		enGhost.step(player, "hi");
		assertEquals(
				"Hello! Hardly anyone speaks to me. The other children pretend I don't exist. I hope you remember me.",
				npcGhost.get("text"));
		// [22:26] superkym earns 100 experience points.
		assertEquals(oldxp + 100, player.getXP());
		assertThat(player.getQuest("find_ghosts") , containsString("Ben"));

		// -----------------------------------------------
		assertThat(player.getQuest("find_ghosts") , not(containsString("Goran")));

		oldxp = player.getXP();
		npcGhost = SingletonRepository.getNPCList().get("Goran");
		enGhost = npcGhost.getEngine();
		enGhost.step(player, "hi");
		assertEquals("Remember my name ... Goran ... Goran ...", npcGhost.get("text"));
		// [22:26] superkym earns 100 experience points.
		assertEquals(oldxp + 100, player.getXP());
		assertThat(player.getQuest("find_ghosts") , containsString("Goran"));

		// -----------------------------------------------
		oldxp = player.getXP();
		assertThat(player.getQuest("find_ghosts") , not(containsString("Zak")));

		npcGhost = SingletonRepository.getNPCList().get("Zak");
		enGhost = npcGhost.getEngine();
		enGhost.step(player, "hi");
		assertEquals("Remember my name ... Zak ... Zak ...", npcGhost.get("text"));
		// [22:26] superkym earns 100 experience points.
		assertEquals(oldxp + 100, player.getXP());
		assertThat(player.getQuest("find_ghosts") , containsString("Zak"));
		
		// -----------------------------------------------
		oldxp = player.getXP();
		int oldHP = player.getBaseHP();
		en.step(player, "hi");
		assertEquals("If you found any #spirits, please tell me their name.", npc.get("text"));
		en.step(player, "yes");
		assertEquals("Sorry, I don't understand you. What name are you trying to say?", npc.get("text"));
		
		en.step(player, "spirits");
		assertEquals(
				"I seek to know more about other spirits who are dead but stalk the earthly world as ghosts. Please tell me any names you know.",
				npc.get("text"));

		assertThat(player.getQuest("find_ghosts").split(":")[0], containsString("Mary"));
		assertThat(player.getQuest("find_ghosts").split(":")[1], not(containsString("Mary")));
		en.step(player, "Mary");
		assertEquals("Thank you. If you met any other spirits, please tell me their name.", npc.get("text"));
		assertThat(player.getQuest("find_ghosts").split(":")[1], containsString("mary"));
		assertThat(player.getQuest("find_ghosts").split(":")[0],  not(containsString("Mary")));

		en.step(player, "Mary");
		assertEquals("You've told me that name already, thanks. If you met any other spirits, please tell me their name.", npc.get("text"));
	
		en.step(player, "Brandy");
		assertEquals("Sorry, I don't understand you. What name are you trying to say?", npc.get("text"));

		en.step(player, "spirits");
		assertEquals(
				"I seek to know more about other spirits who are dead but stalk the earthly world as ghosts. Please tell me any names you know.",
				npc.get("text"));

		assertThat(player.getQuest("find_ghosts").split(":")[1], not(containsString("ben")));
		en.step(player, "Ben");
		assertEquals("Thank you. If you met any other spirits, please tell me their name.", npc.get("text"));
		assertThat(player.getQuest("find_ghosts").split(":")[1], containsString("ben"));

		en.step(player, "spirits");
		assertEquals(
				"I seek to know more about other spirits who are dead but stalk the earthly world as ghosts. Please tell me any names you know.",
				npc.get("text"));

		assertThat(player.getQuest("find_ghosts").split(":")[1], not(containsString("zak")));
		en.step(player, "Zak");
		assertEquals("Thank you. If you met any other spirits, please tell me their name.", npc.get("text"));
		assertThat(player.getQuest("find_ghosts").split(":")[1], containsString("zak"));
		
		
		en.step(player, "spirits");
		assertEquals(
				"I seek to know more about other spirits who are dead but stalk the earthly world as ghosts. Please tell me any names you know.",
				npc.get("text"));

		assertThat(player.getQuest("find_ghosts"), not(containsString("Goran")));
		en.step(player, "Goran");
		assertEquals(
				"Thank you. Now that I know those 4 names, perhaps I can even reach the spirits with my mind. I can't give you anything of material value, but I have given you a boost to your basic wellbeing, which will last forever. May you live long, and prosper.",
				npc.get("text"));
		assertThat(player.getQuest("find_ghosts"), is("done"));
		// [22:27] superkym heals 50 health points.
		// [22:27] superkym earns 5000 experience points.
		en.step(player, "bye");
		assertEquals("Bye", npc.get("text"));
		assertEquals(oldxp + 5000, player.getXP());
		assertEquals(oldHP + 50, player.getBaseHP());
		// -----------------------------------------------

		en.step(player, "hi");
		assertEquals("Wooouhhhhhh!", npc.get("text"));
		en.step(player, "task");
		assertEquals("Thank you! I feel better now that I know the names of other spirits on Fauimoni.",
				npc.get("text"));
		en.step(player, "bye");
		assertEquals("Bye", npc.get("text"));
	}
}
