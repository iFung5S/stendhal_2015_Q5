package games.stendhal.server.maps.sedah.storage;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the storage NPC in Sedah City.
 *
 * @author Teiv
 */
public class StorageNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param zone
	 *            The zone to be configured.
	 * @param attributes
	 *            Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final
			Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC storageNPC = new SpeakerNPC("Pjotr Yearl") {

			@Override
			protected void createPath() {
				List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(35, 23));
				nodes.add(new Node(35, 15));
				nodes.add(new Node(21, 15));
				nodes.add(new Node(21, 23));
				nodes.add(new Node(18, 23));
				nodes.add(new Node(18, 12));
				nodes.add(new Node(16, 12));
				nodes.add(new Node(16, 3));
				nodes.add(new Node(13, 3));
				nodes.add(new Node(13, 13));
				nodes.add(new Node(15, 13));
				nodes.add(new Node(15, 20));
				nodes.add(new Node(21, 20));
				nodes.add(new Node(21, 15));
				nodes.add(new Node(35, 15));
				nodes.add(new Node(35, 23));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello my friend. I should be busy.");
				addJob("My job is to serve the #Scarlet Army.");
				addReply(
						"scarlet",
						"The Scarlet Army is a special division of Kalavan's Army. They all wear a red armor.");
				addHelp("Have you seen this, no armor left here. At the moment I'm not able to serve the #Scarlet Army!");
				addOffer("Bring me some armor and i pay you out!");
				new BuyerAdder().add(this, new BuyerBehaviour(SingletonRepository.getShopList().get("buyred")), false);
				addGoodbye("Have a nice day!");
			}
		};

		storageNPC.setEntityClass("scarletarmynpc");
		storageNPC.setPosition(35, 23);
		storageNPC.initHP(100);
		zone.add(storageNPC);
	}
}
