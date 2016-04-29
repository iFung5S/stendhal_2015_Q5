/**
 * 
 */
package games.stendhal.server.maps.quests.logic;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

/**
 * @author Zhenzhong Xiao
 * 
 * Abstract quest state
 *
 */
public abstract class AbstractQuestState {

	private SpeakerNPC npc;
	private String thisState;
	private String nextState;
	private String QUEST_SLOT;
	
	
	/**
	 * Build a state
	 *
	 * @param the NPC that player need to interact with in the state
	 * @param the name of this state
	 * @param next state (the state to go to after this state is finished)
	 * @param the QUEST_SLOT identifier
	 */
	public AbstractQuestState(SpeakerNPC npc, String thisState, String nextState, String QUEST_SLOT){
		this.npc = npc;
		this.thisState = thisState;
		this.nextState = nextState;
		this.QUEST_SLOT = QUEST_SLOT;
	}
	
	/**
	 * Get the NPC that player need to interact with in the state
	 */
	public SpeakerNPC getNPC(){
		return this.npc;
	}
	
	/**
	 * Get the QUEST_SLOT identifier for this quest
	 */
	public String getQUEST_SLOT(){
		return this.QUEST_SLOT;
	}
	
	/**
	 * Get the name of this state
	 */
	public String getState(){
		return this.thisState;
	}
	
	/**
	 * Get the name of next state
	 */
	public String getNextState(){
		return this.nextState;
	}
	
	/**
	 * Add interactions in this state to the World
	 */
	public abstract void addToWorld();
	
	/**
	 * A reminder of current state of the quest, which will be showed in "travel log"
	 */
	public abstract String getHistoryNote(Player player);
	
	
}
