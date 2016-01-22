/***************************************************************************
 *                   (C) Copyright 2003-2014 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

"use strict";
window.stendhal = window.stendhal || {};

stendhal.main = {

	/**
	 * register marauroa event handlers.
	 */
	registerMarauroaEventHandlers: function() {

		marauroa.clientFramework.onDisconnect = function(reason, error){
			stendhal.ui.chatLog.addLine("error", "Disconnected: " + error);
		}
	
		marauroa.clientFramework.onLoginRequired = function() {
			window.location = "/index.php?id=content/account/login&url="
				+ escape(window.location.pathname + window.location.hash);
		}

		marauroa.clientFramework.onLoginFailed = function(reason, text) {
			alert("Login failed. Please login on the Stendhal website first and make sure you open the client on an https://-URL");
			marauroa.clientFramework.close();
			document.getElementById("chatbar").disabled = true;
			document.getElementById("chat").style.backgroundColor = "#AAA";
		}

		marauroa.clientFramework.onAvailableCharacterDetails = function(characters) {
			var name = null;
			if (window.location.hash) {
				name = window.location.hash.substring(1);
			} else {
				name = marauroa.util.first(characters).a.name;
				var admin = 0;
				for (var i in characters) {
					if (characters.hasOwnProperty(i)) {
						if (characters[i].a.adminlevel > admin) {
							admin = characters[i].a.adminlevel;
							name = characters[i].a.name;
						}
					}
				}
			}
			marauroa.clientFramework.chooseCharacter(name);
			var body = document.getElementById("body")
			body.style.cursor = "auto";
			stendhal.ui.chatLog.addLine("client", "Loading world...");
		}


		marauroa.clientFramework.onTransferREQ = function(items) {
			for (var i in items) {
				if (typeof(items[i].name) != "undefined" && items[i].name.match(".collision$")) {
					items[i].ack = true;
				}
			}
		}

		// update user interface on perceptions
		if (document.getElementById("gamewindow")) {
			marauroa.perceptionListener.onPerceptionEnd = function(type, timestamp) {
				stendhal.zone.sortEntities();
				stendhal.ui.minimap.drawEntities();
				stendhal.ui.buddyList.update();
				stendhal.ui.equip.update();
				stendhal.ui.bag.update();
				stendhal.ui.keyring.update();
				stendhal.data.map.load(marauroa.currentZoneName);
			}
		}
	},


	/**
	 * registers global browser event handlers.
	 */
	registerBrowserEventHandlers: function() {
		var gamewindow = document.getElementById('gamewindow');
		if (gamewindow) {
			gamewindow.addEventListener("click", stendhal.ui.gamewindow.onclick);
		}
	},

	/**
	 * starts the Stendhal web client and connects to the Stendhal server.
	 */
	startup: function() {
		stendhal.ui.chatLog.addLine("error", "This is an early stage of an experimental web-based client. Please use the official client at https://stendhalgame.org to play Stendhal.");
		stendhal.ui.chatLog.addLine("client", "Client loaded. Connecting...");
		var body = document.getElementById("body");

		stendhal.main.registerMarauroaEventHandlers();
		stendhal.main.registerBrowserEventHandlers();
		marauroa.clientFramework.connect(null, null);
		
		if (document.getElementById("gamewindow")) {
			stendhal.ui.gamewindow.draw.apply(stendhal.ui.gamewindow, arguments);
			
			document.addEventListener("click", function(e) {
				if (e.target.dataItem) {
					marauroa.clientFramework.sendAction({
						type: "use", 
						"target_path": e.target.dataItem.getIdPath()
					});
				}
			});
		}
	}
}

document.addEventListener('WebComponentsReady', stendhal.main.startup);