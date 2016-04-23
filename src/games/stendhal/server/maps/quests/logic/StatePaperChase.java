/**
 * 
 */
package games.stendhal.server.maps.quests.logic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestToTimeStampAction;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.player.Player;

/**
 * @author mbax3zx2
 *
 */
public class StatePaperChase extends AbstractQuestState {
	
	private String passWord;
	private String finishReply = "I have never been this happy before!!!Thank you!!";
	private List<ChatAction> completeAction = new LinkedList<ChatAction>();
	private int xp = 0;
	private String historyNote;

	
	/**
	 * Build a single stage of paper chase
	 *
	 * @param the NPC that player need to talk to
	 * @param the name of this state
	 * @param next state (the state to go to after this state is finished)
	 * @param the QUEST_SLOT identifier
	 * @param the keyword that need to say to the target NPC in order to get to the next stage
	 * @param a message said by NPC after player says the keyword
	 */
	public StatePaperChase(SpeakerNPC npc, String thisState, String nextState,
			String QUEST_SLOT, String PassWord, String finishReply) {
		super(npc, thisState, nextState, QUEST_SLOT);

		passWord = PassWord;
		this.finishReply = finishReply;
		historyNote = "I need to find " + npc.getName() + " and say the word: " + PassWord;
	}
	
	public void setKarmaAction(int karma){
    	completeAction.add(new IncreaseKarmaAction(karma));
	}
	
	public void setXP(int xp){
		this.xp = xp;
	}
	
	/**
	 * Add the rewards of this state, if player finishes this stage, he/she will 
	 * get such reward.
	 *
	 * @param name of the rewarded item
	 * @param quantity of the rewarded item
	 */
	public void addRewardItem(String itemName, int quant){
		completeAction.add(new EquipItemAction(itemName, quant));
	}

	@Override
	public void addToWorld() {
		
		completeAction.add(new SetQuestAction(getQUEST_SLOT(), getNextState()));
    	completeAction.add(new IncreaseXPAction(xp));
    	// set up endState
		if(getNextState().equals("done")) 
		{
			// If this is the last state, set timestamp
			completeAction.add(new SetQuestToTimeStampAction(getQUEST_SLOT(), 1));
		}
		
		ChatAction fullCompleteAction = new MultipleActions(completeAction);
		
		// say the right word
    	getNPC().add(ConversationStates.ATTENDING, 
    				Arrays.asList(passWord),
		    		new QuestActiveCondition(getQUEST_SLOT()),
		    		ConversationStates.ATTENDING, 
		    		finishReply,
		    		fullCompleteAction);
		
	}
	
	/**
	 * A reminder of current state of the quest, which will be showed in "travel log"
	 * 
	 * @param the information about what the player needs to do at this state of the quest
	 */
	public void setHistoryNote(String notes){
		this.historyNote = notes;
	}

	@Override
	public String getHistoryNote(Player player) {
		
		if(player.getQuest(getQUEST_SLOT()).equals(getState()))
			return historyNote;
		else
			return null;
	}

}
