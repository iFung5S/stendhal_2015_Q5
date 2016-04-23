/**
 * 
 */
package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenzhong Xiao
 *
 *	The abstract class that need to be extended if a quest wants to be created
 *	in state quest method.
 */
public abstract class StatedAbstractQuest extends AbstractQuest {
	
	private boolean repeatable = false;
	private int REQUIRED_MINUTES = 0;
	
	/**
	 * Class method, which add a list of states into the World
	 *
	 * @param a list of AbstractQuestState
	 */
	public static void addAllState(List<AbstractQuestState> states){
		for(AbstractQuestState state : states)
			state.addToWorld();
	}
	
	/**
	 * Set the quest to be repeatable
	 *
	 * @param the cool down time for the quest to be repeatable
	 */
	public void setRepeatable(int CD){
		REQUIRED_MINUTES = CD;
		repeatable = true;
	}
	
	@Override
 	public boolean isRepeatable(final Player player) {
		if(!repeatable) return false;
		else	return new AndCondition(
 					new QuestStateStartsWithCondition(getSlotName(), "done;"),
 					new TimePassedCondition(getSlotName(), 1, REQUIRED_MINUTES)).fire(player, null, null);
 	}
	
	/**
	 * Get the list of AbstractQuestState
	 *
	 * @return the list of AbstractQuestState
	 */
	public abstract List<AbstractQuestState> getStateList();
	
	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(getSlotName())) {
			return res;
		}
		res.add(getStateList().get(0).getHistoryNote(player));
		
		if (!player.isQuestCompleted(getSlotName())) {
			// not yet finished
			for(int i = 1; i < getStateList().size(); i++)
				if(getStateList().get(i).getHistoryNote(player) != null)
					res.add(getStateList().get(i).getHistoryNote(player));
		} else if (isRepeatable(player)) {
			// may be repeated now
			res.add("It's been a while since I helped " + getNPCName() + ", should I see if I can help "
					+ getNPCName() + " again?");
        } else {
        	// not (currently) repeatable
        	res.add("I helped " + getNPCName() + " just now, " + getNPCName() + " doesn't need my help for now.");
		}
		return res;
	}
	
}
