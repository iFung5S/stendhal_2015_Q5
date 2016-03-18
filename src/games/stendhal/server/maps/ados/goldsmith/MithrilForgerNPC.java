/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.ados.goldsmith;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.MultiProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.MultiProducerBehaviour;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.HashSet;

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
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildForger(zone);
	}

	private void buildForger(final StendhalRPZone zone) {
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
				final HashSet<String> productsNames = new HashSet<String>();
                productsNames.add("mithril");
                productsNames.add("stealth ring");
			    final HashMap<String, Map<String, Integer>> requiredResources = new HashMap<String, Map<String, Integer>>();	

			    final Map<String, Integer> requiredResourcesForMithril = new TreeMap<String, Integer>();	
				requiredResourcesForMithril.put("wood", 20);
				requiredResourcesForMithril.put("mithril nugget", 1);

				final Map<String, Integer> requiredResourcesForRing = new TreeMap<String, Integer>();	
				
				// Pedinghaus makes stealth ring you bring him mithril and gold nugget
				requiredResourcesForRing.put("gold nugget", 1);
				requiredResourcesForRing.put("mithril nugget", 1);
				
				// for testing
				// requiredResourcesForRing.put("cheese", 1);
				
				requiredResources.put("mithril", requiredResourcesForMithril);
				requiredResources.put("stealth ring", requiredResourcesForRing);

				final HashMap<String, Integer> productionTimesPerProduct = new HashMap<String, Integer>();
				productionTimesPerProduct.put("mithril", 18 * 60);
				productionTimesPerProduct.put("stealth ring", 25 * 60);
        

				final HashMap<String, Boolean> boundMap = new HashMap<String, Boolean>();
				boundMap.put("mithril", true);
				boundMap.put("stealth ring", true);
				
  			    final MultiProducerBehaviour behaviour = new MultiProducerBehaviour(
		          "Pedinghaus_cast_mithrilandring",
		          "make",
		          productsNames,
		          requiredResources,
		          productionTimesPerProduct,
		          boundMap
		          );

				new MultiProducerAdder().addMultiProducer(this, behaviour,
				        "Greetings. I sense you may be interested in mithril. If you desire me to #make you a #'mithril bar' or #'stealth ring' just say the word.");
				addReply("wood",
		        		"The wood is for the fire. I hope you collect yours from the forest, and not the barbaric practise of killing ents.");
				addReply(Arrays.asList("mithril ore", "mithril nugget"),
				        "Nowadays these rare nuggets are only likely to be found in the Ados mountains. I have no idea if that area is still civilised...");
				addReply(Arrays.asList("stealth", "stealth ring", "ring"),
				        "One ring to rule them all, one ring to find them all, one ring to bring them and in the darkness bind them...");
				addReply(Arrays.asList("mithril bar", "mithril", "bar"),
				        "Mithril is an incredibly valuable commodity, as it makes armor of astounding strength, yet remains featherlight. Guard any mithril stash you own with great care.");
			}


			

		};

		forger.setEntityClass("mithrilforgernpc");
		forger.setDirection(Direction.RIGHT);
		forger.setPosition(10, 12);
		forger.initHP(100);
		forger.setDescription("You see Pedinghaus. His clothes look like he seems to be talented in practicing magic...");
		zone.add(forger);
	}
}
