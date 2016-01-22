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

(function() {

	var HEALTH_BAR_HEIGHT = 6;



/**
 * RPEntity
 */
marauroa.rpobjectFactory.rpentity = marauroa.util.fromProto(marauroa.rpobjectFactory.activeEntity, {
	zIndex: 8000,
	drawY: 0,
	spritePath: "",
	titleStyle: "#FFFFFF",
	_target: null,

	set: function(key, value) {
		marauroa.rpobjectFactory.rpentity.proto.set.apply(this, arguments);
		if (key == "text") {
			this.say(value);
		} else if (key in ["hp", "base_hp"]) {
			this[key] = parseInt(value);
		} else if (key == "target") {
			for (var i in stendhal.zone.entities) {
				var entity = stendhal.zone.entities[i];
				if (entity.id == value) {
					this._target = entity;
					break;
				}
			}
		}
	},

	isVisibleToAction: function(filter) {
		return true;
	},

	/** 
	 * says a text
	 */
	say: function (text) {
		if (!marauroa.me) {
			return;
		}
		if (marauroa.me.isInHearingRange(this)) {
			if (text.match("^!me") == "!me") {
				stendhal.ui.chatLog.addLine("emote", text.replace(/^!me/, this.title));
			} else {
				stendhal.ui.chatLog.addLine("normal", this.title + ": " + text);
			}
		}
	},

	/**
	 * draw an outfix part
	 *
	 * @param ctx   ctx
	 * @param part  part
	 * @param index index
	 */
	drawOutfitPart: function(ctx, part, index) {
		var n = index;
		if (index < 10) {
			n = "00" + index;
		} else if(index < 100) {
			n = "0" + index;
		}
		var filename = "/data/sprites/outfit/" + part + "/" + part + "_" + n + ".png";
		this.drawSprite(ctx, filename);
	},

	/** 
	 * draw RPEntities
	 */
	draw: function(ctx) {
		var filename;
		if (typeof(this.outfit) != "undefined") {
			this.drawOutfitPart(ctx, "body", (this.outfit % 100));
			this.drawOutfitPart(ctx, "dress", (Math.floor(this.outfit/100) % 100));
			this.drawOutfitPart(ctx, "head", (Math.floor(this.outfit/10000) % 100));
			this.drawOutfitPart(ctx, "hair", (Math.floor(this.outfit/1000000) % 100));
		} else {
			filename = "/data/sprites/" + this.spritePath + "/" + this["class"];
			if (typeof(this.subclass) != "undefined") {
				filename = filename + "/" + this["subclass"];
			}
			filename = filename + ".png";
			this.drawSprite(ctx, filename)
		}
	},
	
	drawSprite: function(ctx, filename) {
		var localX = this._x * 32;
		var localY = this._y * 32;
		var image = stendhal.data.sprites.get(filename);
		if (image.complete) {
			var nFrames = 3;
			var nDirections = 4;
			var yRow = this.dir - 1;
			// Ents are a hack in Java client too
			if (this["class"] == "ent") {
				nFrames = 1;
				nDirections = 2;
				yRow = Math.floor((this.dir - 1) / 2);
			}
			this.drawHeight = image.height / nDirections;
			this.drawWidth = image.width / nFrames;
			var drawX = ((this.width * 32) - this.drawWidth) / 2;
			var frame = 0;
			if (this.speed > 0 && nFrames != 1) {
				// % Works normally with *floats* (just whose bright idea was
				// that?), so use floor() as a workaround
				frame = Math.floor(Date.now() / 100) % nFrames;
			}
			var drawY = (this.height * 32) - this.drawHeight;
			ctx.drawImage(image, frame * this.drawWidth, yRow * this.drawHeight, this.drawWidth, this.drawHeight, localX + drawX, localY + drawY, this.drawWidth, this.drawHeight);
		}
	},

	drawTop: function(ctx) {
		var localX = this._x * 32;
		var localY = this._y * 32;

		this.drawHealthBar(ctx, localX, localY);
		this.drawTitle(ctx, localX, localY);
	},

	drawHealthBar: function(ctx, x, y) {
		var drawX = x + ((this.width * 32) - this.drawWidth) / 2;
		var drawY = y + (this.height * 32) - this.drawHeight - HEALTH_BAR_HEIGHT;
		
		ctx.fillStyle = "#E0E0E0";
		ctx.fillRect(drawX + 1, drawY + 1, this.drawWidth - 2, HEALTH_BAR_HEIGHT - 2);

		// Bar color
		var hpRatio = this.hp / this.base_hp;
		var red = Math.floor(Math.min((1 - hpRatio) * 2, 1) * 255);
		var green = Math.floor(Math.min(hpRatio * 2, 1) * 255);
		ctx.fillStyle = "rgb(".concat(red, ",", green, ",0)");
		ctx.fillRect(drawX + 1, drawY + 1, this.drawWidth * hpRatio - 2, HEALTH_BAR_HEIGHT - 2);

		ctx.strokeStyle = "#000000 1px";
		ctx.beginPath();
		ctx.rect(drawX, drawY, this.drawWidth - 1, HEALTH_BAR_HEIGHT - 1);
		ctx.stroke();
	},

	drawTitle: function(ctx, x, y) {
		if (typeof(this.title) != "undefined") {
			ctx.font = "14px Arial";
			ctx.fillStyle = "#A0A0A0";
			var textMetrics = ctx.measureText(this.title);
			var drawY = y + (this.height * 32) - this.drawHeight - HEALTH_BAR_HEIGHT;
			ctx.fillText(this.title, x + (this.width * 32 - textMetrics.width) / 2+2, drawY - 5 - HEALTH_BAR_HEIGHT);
			ctx.fillStyle = this.titleStyle;
			ctx.fillText(this.title, x + (this.width * 32 - textMetrics.width) / 2, drawY - 5 - HEALTH_BAR_HEIGHT);
		}
	},

	// attack handling
	getAttackTarget: function() {
		return this._target;
	},
	
	onDamaged: function(source, damage) {
		this.say(this.title + " got hit by " + source.title + " causing a damage of " + damage);
	},

	onBlocked: function(source) {
		
	},

	onMissed: function(source) {
		
	},

	onAttackPerformed: function(nature, ranged) {
	}

});

})();