package games.stendhal.server.maps.semos.mountain;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * The pie collector NPC Peter
 * 
 * @author Zhenzhong Xiao
 *
 */
public class PieCollectorNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Peter") {
			@Override
			public void createDialog() {
				addGreeting("Hello, I am Peter, the king of 0_semos_mountain_n2_w.");
				addJob("I have special power, which is herding goats.");
				addHelp("I can heal you here for free!       Just joking, you really believe that?");
				addGoodbye("Have a nice day! Don't kill my sheeps by the way.");
				
			}

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(35, 13));
				nodes.add(new Node(35, 23));
				nodes.add(new Node(33, 23));
				nodes.add(new Node(33, 31));
				nodes.add(new Node(37, 31));
				nodes.add(new Node(37, 33));
				nodes.add(new Node(40, 33));
				nodes.add(new Node(40, 39));
				nodes.add(new Node(52, 39));
				nodes.add(new Node(52, 28));
				nodes.add(new Node(57, 28));
				nodes.add(new Node(57, 16));
				setPath(new FixedPath(nodes, true));
			}
		};

		npc.setPosition(35, 13);
		npc.setDescription("You see Peter. He looks like someone loves pies.");
		// This determines how the NPC will look like. welcomernpc.png is a picture in data/sprites/npc/
		npc.setEntityClass("yetimalenpc");
		zone.add(npc);
	}

}
