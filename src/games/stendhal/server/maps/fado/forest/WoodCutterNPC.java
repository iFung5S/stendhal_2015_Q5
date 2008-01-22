package games.stendhal.server.maps.fado.forest;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought axes
 * He is a wood cutter.
 *
 * @author kymara
 */
public class WoodCutterNPC implements ZoneConfigurator {
	private ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		SpeakerNPC npc = new SpeakerNPC("Woody") {

			@Override
			protected void createPath() {
				List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(55, 84));
				nodes.add(new Node(68, 84));
				nodes.add(new Node(68, 84));
				nodes.add(new Node(68, 71));
				nodes.add(new Node(57, 71));
				nodes.add(new Node(57, 76));
				nodes.add(new Node(57, 75));
				nodes.add(new Node(53, 75));
				nodes.add(new Node(53, 82));
				nodes.add(new Node(55, 82));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to this forest, south of Or'ril river.");
				addJob("I'm a wood cutter by trade. Can you #offer me any axes?");
				addHelp("You can sometimes collect wood that's lying around the forest. Oh, and I take #offers of any good axe you might sell.");
				addOffer("My axes become blunt fast. Please check the sign I made outside my lodge to see the axes I buy.");
				addQuest("What's that you say? I don't need anything, though my young friend Sally over the river might need a hand.");
				new BuyerAdder().add(this, new BuyerBehaviour(shops.get("buyaxe")), false);
 				addGoodbye("Bye.");
			}
		};

		npc.setDescription("You see Woody, an outdoorsy-looking fellow.");
		npc.setEntityClass("woodcutternpc");
		npc.setPosition(55, 84);
		npc.initHP(100);
		zone.add(npc);
	}
}
