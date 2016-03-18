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
package games.stendhal.server.maps.ados.bar;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

public class LonJathamNPC implements ZoneConfigurator {
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
		final SpeakerNPC latham = new SpeakerNPC("Lon Jatham") {

			@Override
			public void say(final String text) {
				setDirection(Direction.DOWN);
				super.say(text, false);
			}

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to #Stendhal! Built with #Java it's like arcade, except it works.");
				addReply("java",
					"Java is awesome, buy my #book.");
				addReply("book",
						"Yes! It can teach you everything! Get down to SSO in Kilburn now!");
				addReply("stendhal",
					"Stendhal is completely #free software.");
				addReply("free",
						"You must know that, you're playing it.");
				addGoodbye("Remember, 80 chars per line or I will hunt you down.");
			}
		};

		latham.setEntityClass("mithrilforgernpc");
		latham.setDescription("Lon Jatham knows everything about Java.");
		latham.setPosition(24, 19);
		latham.setDirection(Direction.DOWN);
		latham.initHP(100);
		zone.add(latham);
	}
}
