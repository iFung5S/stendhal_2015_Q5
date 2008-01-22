package games.stendhal.server.actions.admin;

import games.stendhal.server.actions.CommandCenter;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPAction;
import static games.stendhal.server.actions.WellKnownActionConstants.TARGET;

public class TeleportToAction extends AdministrationAction {

	private static final String _TELEPORTTO = "teleportto";

	public static void register() {
		CommandCenter.register(_TELEPORTTO, new TeleportToAction(), 300);
	}

	@Override
	public void perform(Player player, RPAction action) {
		if (action.has(TARGET)) {
			String name = action.get(TARGET);
			RPEntity teleported = SingletonRepository.getRuleProcessor().getPlayer(name);

			if (teleported == null) {
				teleported = SingletonRepository.getNPCList().get(name);
				if (teleported == null) {

					String text = "Player \"" + name + "\" not found";
					player.sendPrivateText(text);
					logger.debug(text);
					return;
				}
			}

			StendhalRPZone zone = teleported.getZone();
			int x = teleported.getX();
			int y = teleported.getY();

			player.teleport(zone, x, y, null, player);
			SingletonRepository.getRuleProcessor().addGameEvent(player.getName(),
					_TELEPORTTO, action.get(TARGET), zone.getName(),
					Integer.toString(x), Integer.toString(y));
		}
	}

}
