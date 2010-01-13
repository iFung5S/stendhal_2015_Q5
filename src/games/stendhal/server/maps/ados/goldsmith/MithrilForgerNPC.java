package games.stendhal.server.maps.ados.goldsmith;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Ados MithrilForger (Inside / Level 0).
 *
 * @author kymara
 */
public class MithrilForgerNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildForger(zone, attributes);
	}

	private void buildForger(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC forger = new SpeakerNPC("Pedinghaus") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings.");
				addJob("I forge mithril. But magically, mind you. Joshua has kindly allowed me space to work here, despite the fact that I am so different from the others in Ados.");
				addHelp("If you're here for gold bars, you must speak with Joshua. I #cast the rare and precious #mithril #bar.");
				addGoodbye("Bye.");

				// Pedinghaus makes mithril if you bring him mithril nugget and wood
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
				requiredResources.put("wood", 20);
				requiredResources.put("mithril nugget", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour("Pedinghaus_cast_mithril",
						"cast", "mithril bar", requiredResources, 18 * 60);

				new ProducerAdder().addProducer(this, behaviour,
				        "Greetings. I sense you may be interested in mithril. If you desire me to #cast you a #'mithril bar', just say the word.");
				addReply("wood",
		        		"The wood is for the fire. I hope you collect yours from the forest, and not the barbaric practise of killing ents.");
				addReply(Arrays.asList("mithril ore", "mithril nugget"),
				        "Nowadays these rare nuggets are only likely to be found in the Ados mountains. I have no idea if that area is still civilised...");
				addReply(Arrays.asList("mithril bar", "mithril", "bar"),
				        "Mithril is an incredibly valuable commodity, as it makes armor of astounding strength, yet remains featherlight. Guard any mithril stash you own with great care.");
			}
		};

		forger.setEntityClass("mithrilforgernpc");
		forger.setDirection(Direction.RIGHT);
		forger.setPosition(10, 12);
		forger.initHP(100);
		zone.add(forger);
	}
}
