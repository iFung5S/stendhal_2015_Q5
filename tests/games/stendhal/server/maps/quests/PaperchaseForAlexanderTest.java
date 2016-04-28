package games.stendhal.server.maps.quests;

import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.ados.market.WeaponryTraderNPC;
import games.stendhal.server.maps.ados.tavern.BarMaidNPC;
import games.stendhal.server.maps.ados.tavern.BarmanNPC;
import games.stendhal.server.maps.athor.dressingroom_male.LifeguardNPC;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

public class PaperchaseForAlexanderTest extends ZonePlayerAndNPCTestImpl {

	/**
	 * There are 4 speaker npcs in this quest
	 */
	private SpeakerNPC alexander = null;
	private SpeakerNPC coralia = null;
	private SpeakerNPC dale = null;
	private SpeakerNPC david = null;
	
	private Engine engAlexander = null;
	private Engine engCoralia = null;
	private Engine engDale = null;
	private Engine engDavid = null;
		
	private static final String CITY_ZONE_NAME = "int_ados_city";
	private static final String TAVERN_ZONE_NAME = "int_ados_tavern";
	private static final String  BEACH_ZONE_NAME = "int_athor_island_beach";
	
	private Player player;
	
	private AbstractQuest quest;
	private String questSlot;
	
	private int playerXPBeforeQuest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		QuestHelper.setUpBeforeClass();
		
		setupZone(CITY_ZONE_NAME);
		setupZone(TAVERN_ZONE_NAME);
		setupZone(BEACH_ZONE_NAME);
		
	}
	
	
	/* constructor */
	public PaperchaseForAlexanderTest() {
		
		super(CITY_ZONE_NAME, "Alexander");
		
	}
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		
		StendhalRPZone city = new StendhalRPZone(CITY_ZONE_NAME);
		new WeaponryTraderNPC().configureZone(city, null);
		
		StendhalRPZone tavern = new StendhalRPZone(TAVERN_ZONE_NAME);
		new BarMaidNPC().configureZone(tavern, null);
		new BarmanNPC().configureZone(tavern, null);
		
		StendhalRPZone beach = new StendhalRPZone(BEACH_ZONE_NAME);
		new LifeguardNPC().configureZone(beach, null);

		quest = new PaperChaseForAlexander();
		quest.addToWorld();
		
		questSlot = quest.getSlotName();
		
		player = PlayerTestHelper.createPlayer("player");
		
		playerXPBeforeQuest = player.getXP();
		
	} /* setup() */
	
	
	@Test
	public void testRegion() {
		
		assertEquals(Region.ADOS_CITY, quest.getRegion());
		
	} /* testRegion() */
	
	
	@Test
	public void testQuestSlot() {
		
		assertEquals("paper_chase_for_alexander", questSlot);
		
	} /* testQuestSlot() */

	
	public void testName() {
		
		assertEquals("PaperChaseForAlexander", quest.getName());
		
	}

	
	public void testMinLevel() {
		
		assertEquals(0, quest.getMinLevel());
		
	}
	
	
	@Test
	public void testQuestSuccess() {
		
		alexander = SingletonRepository.getNPCList().get("Alexander");
		coralia = SingletonRepository.getNPCList().get("Coralia");
		dale = SingletonRepository.getNPCList().get("Dale");
		david = SingletonRepository.getNPCList().get("David");
		
		engAlexander = alexander.getEngine();
		engCoralia = coralia.getEngine();
		engDale = dale.getEngine();
		engDavid = david.getEngine();
		
		engAlexander.step(player, "hi");
		assertEquals("Hello, may i #help you?", getReply(alexander));
		engAlexander.step(player, "quest");
		assertEquals("Can you please help me with something?", getReply(alexander));
		engAlexander.step(player, "yes");
		assertEquals("Please go and tell Coralia I like her " 
					+ PaperChaseForAlexander.PW_Coralia
					+ " Pass around messages from person to person, please", 
					getReply(alexander));
		engCoralia.step(player, "hi");
		assertEquals("Oh hello there, did I just catch you admiring my beautiful #hat?", getReply(coralia));
		engCoralia.step(player, PaperChaseForAlexander.PW_Coralia);
		assertEquals("Please go and tell Dale he is " 
		             + PaperChaseForAlexander.PW_Dale, 
		             getReply(coralia));
		engDale.step(player, "hi");
		assertEquals("Hey, good looking ...", getReply(dale));
		engDale.step(player, PaperChaseForAlexander.PW_Dale);
		assertEquals("Please go and tell David I like his " 
		             + PaperChaseForAlexander.PW_David,
		             getReply(dale));
		engDavid.step(player, "hi");
		assertEquals("Hallo!", getReply(david));
		engDavid.step(player, PaperChaseForAlexander.PW_David);
		assertEquals("Please go and tell Alexander to " 
					 + PaperChaseForAlexander.PW_Alexander,
					 getReply(david));
		engAlexander.step(player, PaperChaseForAlexander.PW_Alexander);
		assertEquals("Thanks for your help!", getReply(alexander));
		
		assertEquals(playerXPBeforeQuest + 500, player.getXP());
		
	} /* testQuest */

	
	public void testQuestRefuse() {
		
		alexander = SingletonRepository.getNPCList().get("Alexander");
		coralia = SingletonRepository.getNPCList().get("Coralia");
		dale = SingletonRepository.getNPCList().get("Dale");
		david = SingletonRepository.getNPCList().get("David");
		
		engAlexander = alexander.getEngine();
		engCoralia = coralia.getEngine();
		engDale = dale.getEngine();
		engDavid = david.getEngine();
		
		engAlexander.step(player, "hi");
		assertEquals("Hello, may i #help you?", getReply(alexander));
		engAlexander.step(player, "quest");
		assertEquals("Can you please help me with something?", getReply(alexander));
		engAlexander.step(player, "no");
		assertEquals("Ok, maybe next time then...", getReply(alexander));
		
	}
	
	
	public void testQuestCheat() {
		
		alexander = SingletonRepository.getNPCList().get("Alexander");
		coralia = SingletonRepository.getNPCList().get("Coralia");
		dale = SingletonRepository.getNPCList().get("Dale");
		david = SingletonRepository.getNPCList().get("David");
		
		engAlexander = alexander.getEngine();
		engCoralia = coralia.getEngine();
		engDale = dale.getEngine();
		engDavid = david.getEngine();
		
		engAlexander.step(player, "hi");
		assertEquals("Hello, may i #help you?", getReply(alexander));
		engAlexander.step(player, "quest");
		assertEquals("Can you please help me with something?", getReply(alexander));
		engAlexander.step(player, "yes");
		assertEquals("Please go and tell Coralia I like her " 
				+ PaperChaseForAlexander.PW_Coralia
				+ " Pass around messages from person to person, please", 
				getReply(alexander));
		engAlexander.step(player, PaperChaseForAlexander.PW_Alexander);
		assertFalse(getReply(alexander).compareTo("Thanks for your help!") == 0);
		
		
	}
	
} /* class PaperchaseForAlexanderTest */
