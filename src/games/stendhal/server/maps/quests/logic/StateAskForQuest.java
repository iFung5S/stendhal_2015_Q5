/**
 * 
 */
package games.stendhal.server.maps.quests.logic;

import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;

/**
 * @author Zhenzhong Xiao
 *
 *	The first state of a quest when using stated quest method
 */
public class StateAskForQuest extends AbstractQuestState {

	private String purposeQuest = "Can you please help me with something?";
	private String noToQuest = "Ok, maybe next time then...";
	private String yesToQuest = "Thanks!! You are so kind!";
	private String questDetail = "";
	private String purposeAgain = "It's been a while, can you please help me again?";
	private String cooldownReply = "Thanks for your help last time, I think I am good so far!";
	private int CD_FOR_QUEST = -1;
	private String historyNote = getNPC().getName() + " asked me for a favour.";
	
	
	/**
	 * Build the first state of a quest
	 *
	 * @param the NPC that offer the quest
	 * @param the name of this state (normally, this doesn't matter)
	 * @param next state (the state to go to after this state is finished)
	 * @param the QUEST_SLOT identifier
	 * @param A description about the quest, will be said by NPC to player after the player accept the quest
	 */
	public StateAskForQuest(SpeakerNPC npc, String thisState, String nextState, String QUEST_SLOT,
			 				String questDetail){
		super(npc, thisState, nextState, QUEST_SLOT);
		this.questDetail = questDetail;
	}
	
	/**
	 * Build the first state of a quest
	 *
	 * @param the NPC that offer the quest
	 * @param the name of this state (normally, this doesn't matter)
	 * @param next state (the state to go to after this state is finished)
	 * @param the QUEST_SLOT identifier
	 * @param A description about the quest, will be said by NPC to player after the player accept the quest
	 * @param The cool down time for the quest to be took again (Only if the quest is repeatable)
	 */
	public StateAskForQuest(SpeakerNPC npc, String thisState, String nextState, String QUEST_SLOT,
							String questDetail, int CD_FOR_QUEST){
		super(npc, thisState, nextState, QUEST_SLOT);
		this.CD_FOR_QUEST = CD_FOR_QUEST;
		this.questDetail = questDetail;
	}
	
	/**
	 * Set the quest purpose sentence said by NPC after player ask for quest
	 * 
	 * @param what NPC says after player type "quest"
	 */
	public void setPurposeQuest(String reply){
		this.purposeQuest = reply;
	}
	
	/**
	 * When player rejects the quest
	 * 
	 * @param what NPC says after player reject the quest
	 */
	public void setNoToQuest(String reply){
		this.noToQuest = reply;
	}
	
	/**
	 * When player accepts the quest
	 * 
	 * @param what NPC says after player accepts the quest
	 */
	public void setYesToQuest(String reply){
		this.yesToQuest = reply;
	}
	
	/**
	 * When player ask for quest after the quest can be repeated
	 * 
	 * @param what NPC says after player types "quest" when the quest can be repeated
	 */
	public void setPurposeAgain(String reply){
		this.purposeAgain = reply;
	}
	
	/**
	 * When player ask for quest and the quest cannot be repeated
	 * 
	 * @param what NPC says after player types "quest" when the quest cannot be repeated
	 */
	public void setCooldownReply(String reply){
		this.cooldownReply = reply;
	}
	
	
	/**
	 * A description of the quest for "travel log"
	 * 
	 * @param what the log shows about the quest/(this state)
	 */
	public void setQuestDescriptionForHistory(String notes){
		this.historyNote = notes;
	}
	
	@Override
	public void addToWorld(){
		
		// offer quest first time
    	getNPC().add(ConversationStates.ATTENDING,
    				ConversationPhrases.QUEST_MESSAGES,
    				new QuestNotStartedCondition(getQUEST_SLOT()),
    				ConversationStates.QUEST_OFFERED,
    				purposeQuest,
    				null);
    	
    	// accept quest response
    	getNPC().add(ConversationStates.QUEST_OFFERED,
		    		ConversationPhrases.YES_MESSAGES,
		    		null,
		    		ConversationStates.ATTENDING,
		    		yesToQuest + " " + questDetail,
					new SetQuestAction(getQUEST_SLOT(), getNextState()));		
    	
    	// reject quest response
    	getNPC().add(ConversationStates.QUEST_OFFERED,
		        	ConversationPhrases.NO_MESSAGES,
		        	null,
		        	ConversationStates.ATTENDING,
		        	noToQuest,
		        	null);
    	
    	if(CD_FOR_QUEST != -1)
    	{
	    	// repeat quest
	    	getNPC().add(ConversationStates.ATTENDING,
			            ConversationPhrases.QUEST_MESSAGES,
			            new AndCondition(
			            	new QuestCompletedCondition(getQUEST_SLOT()),
			            	new TimePassedCondition(getQUEST_SLOT(), 1, CD_FOR_QUEST)),
			            ConversationStates.QUEST_OFFERED,
			            purposeAgain,
			            null);
	    		    	    	
	    	// quest inactive    	
	    	getNPC().add(ConversationStates.ATTENDING,
			        	ConversationPhrases.QUEST_MESSAGES,
			        	new AndCondition(
			        		new QuestCompletedCondition(getQUEST_SLOT()),
			        		new NotCondition(new TimePassedCondition(getQUEST_SLOT(), 1, CD_FOR_QUEST))),
			        	ConversationStates.ATTENDING,
			        	cooldownReply,
			        	null);
    	}
		
	}
	
	@Override
	public String getHistoryNote(Player player){
		return historyNote;
	};
	
}
