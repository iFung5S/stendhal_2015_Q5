package games.stendhal.server.maps.quests;

import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.ados.market.FoodSellerNPC;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

public class BreadForAdenaTest extends ZonePlayerAndNPCTestImpl {
	
	
	private SpeakerNPC adena = null;
	private Engine engAdena = null;
	
	private static final String ZONE_NAME = "int_ados_city";
	
	private Player player;
	
	private AbstractQuest quest;
	private String questSlot;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_NAME);
		
	} /* setUpBeforeClass() */
	
	
	public BreadForAdenaTest() {
		
		super(ZONE_NAME, "Adena");
		
	}
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		
		StendhalRPZone market = new StendhalRPZone(ZONE_NAME);
		new FoodSellerNPC().configureZone(market, null);

		quest = new BreadForAdena();
		quest.addToWorld();
		
		questSlot = quest.getSlotName();
		
		player = PlayerTestHelper.createPlayer("player");
		
	} /* setup() */
	
	
	@Test
	public void testRegion() {
		
		assertEquals(Region.ADOS_CITY, quest.getRegion());
		
	} /* testRegion() */
	
	
	@Test
	public void testQuestSlot() {
		
		assertEquals("bread_for_adena", questSlot);
		
	} /* testQuestSlot() */
	
	
	@Test
	public void testName() {
		
		assertEquals("BreadForAdena", quest.getName());
		
	} /* testName() */
	
	
	@Test
	public void testMinLevel() {
		
		assertEquals(0, quest.getMinLevel());
		
	} /* testMinLevel */
	
	
	@Test
	public void testQuestSuccess() {
		
		adena = SingletonRepository.getNPCList().get("Adena");
		engAdena = adena.getEngine();
		
		engAdena.step(player, "hi");
		assertEquals("We just got fresh apples and carrots from several #farms near Semos!", getReply(adena));
		engAdena.step(player, "quest");
		assertEquals("Can you please help me with something?", getReply(adena));
		engAdena.step(player, "yes");
		assertEquals("Thanks!! You are so kind! Please bring me two breads from Erna the baker.", getReply(adena));
		engAdena.step(player, "bye");
		player.setQuest(questSlot, BreadForAdena.STATE_START);
		player.setQuest(questSlot, BreadForAdena.STATE_NEEDED_ITEMS);
		PlayerTestHelper.equipWithItem(player, "bread");
		PlayerTestHelper.equipWithItem(player, "bread");
		engAdena.step(player, "hi");
		assertEquals("Hello again. If you've brought me the things I need, I'll happily take them!", getReply(adena));
		engAdena.step(player, "quest");
		assertEquals("I'd still need 2 #'loaves of bread'. Have you brought any?", getReply(adena));
		engAdena.step(player, "yes");
		assertEquals("Wonderful, which one have you brought?", getReply(adena));
		engAdena.step(player, "bread");
		assertEquals("Thank you for your help young one! Let me give you some apples as a reward", getReply(adena));
		
	} /* testQuestSuccess() */
	
	
	@Test
	public void testQuestRefuse() {
		
		adena = SingletonRepository.getNPCList().get("Adena");
		engAdena = adena.getEngine();
		
		engAdena.step(player, "hi");
		assertEquals("We just got fresh apples and carrots from several #farms near Semos!", getReply(adena));
		engAdena.step(player, "quest");
		assertEquals("Can you please help me with something?", getReply(adena));
		engAdena.step(player, "no");
		assertEquals("Ok, maybe next time then...", getReply(adena));
		
	}
	

}
