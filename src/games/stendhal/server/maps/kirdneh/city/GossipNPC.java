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
package games.stendhal.server.maps.kirdneh.city;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;

import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Map;
import java.util.TreeMap;


/**
 * Builds a information giving NPC in Kirdneh city. 
 *
 * @author kymara
 */
public class GossipNPC implements ZoneConfigurator {
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
	@Override
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Jef") {

			@Override
			protected void createPath() {
				setPath(null);
			}
			
			@Override
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.DOWN);
			}

			@Override
			protected void createDialog() {
				addGreeting("Heya!");
				addJob("Um, not sure what you mean. Right now I'm waiting for my mum to get back from the #shops.");
				addHelp("I have some #news about the bazaar over there.");
				addOffer("I don't sell stuff, I'm just waiting for my mum. But I have some #news if you wanna hear it.");
				// quest: FindJefsMom , quest sentence given there
				addReply("news", "Some more shopkeepers will be at the market soon! It'll be cool, it's kind of empty round here at the moment.");
				addReply("shops", "Yeah she's had to go out of town. All we have here is that flower seller! There's #news about our bazaar, though ...");
				addGoodbye("See you around.");
				
				// The character will make a cheese and onion pie if the correct ingredients supplied
	     		final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("flour", Integer.valueOf(2));
				requiredResources.put("cheese", Integer.valueOf(4));
				requiredResources.put("onion", Integer.valueOf(1));

				final ProducerBehaviour behaviour = new ProducerBehaviour("jef_make_pie", "make", "cheese and onion pie",
					                                                      requiredResources, 1 * 60);
		 	
		  	    new ProducerAdder().addProducer(this, behaviour,
		 	                                    "Hello, pal! You have probably heard about the delicious pie "
		 	                                    + "I make and want me to make one for you, is that right?");
		    } // createDialog	
		};

		npc.setEntityClass("kid6npc");
		npc.setPosition(22, 4);
		npc.initHP(100);
		npc.setDescription("You see Jef. He seems like waiting for someone.");
		zone.add(npc);
	}
}
