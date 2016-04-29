/**
 * 
 */
package games.stendhal.server.maps.quests.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.CollectRequestedItemsAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayRequiredItemsFromCollectionAction;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestToTimeStampAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.ItemCollection;

/**
 * @author Zhenzhong Xiao
 *
 *	Collecting items state used by stated quest logic
 */
public class StateCollectItems extends AbstractQuestState {
	
	private String didntBring = "Oh, that's a shame, do tell me when you find some. I'd still need [items].";
	private String finishReply = "I have never been this happy before!!!Thank you!!";
	private String ITEM_LIST;
	private List<ChatAction> completeAction = new LinkedList<ChatAction>();
	private int xp = 0;
	private List<String> conversationPhraseForRequiredItem;
	
	/**
	 * Build a state for items collection
	 *
	 * @param the NPC that player need to deliver the items to
	 * @param the name of this state
	 * @param next state (the state to go to after this state is finished)
	 * @param the QUEST_SLOT identifier
	 */
	public StateCollectItems(SpeakerNPC npc, String thisState, String endState, String QUEST_SLOT) {
		super(npc, thisState, endState, QUEST_SLOT);
		
		conversationPhraseForRequiredItem = ConversationPhrases.QUEST_MESSAGES;
		ITEM_LIST = thisState;
	}
	
	/**
	 * Set what NPC replies when the state end
	 *
	 * @param Reply from NPC when the state end
	 */
	public void setFinishReply(String reply){
		this.finishReply = reply;
	}
	
	public void setKarmaAction(int karma){
    	completeAction.add(new IncreaseKarmaAction(karma));
	}
	
	public void setXP(int xp){
		this.xp = xp;
	}
	
	/**
	 * Set the phrase to trigger NPC ask for required items
	 *
	 * @param A list of string as triggers
	 */
	public void setConversationPhraseForRequiredItem(List<String> list){
		conversationPhraseForRequiredItem = list;
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
		completeAction.add(new SayTextAction(finishReply));
    	// set up endState
		if(getNextState().equals("done")) 
		{
			// If this is the last state, set timestamp
			completeAction.add(new SetQuestToTimeStampAction(getQUEST_SLOT(), 1));
		}
		ChatAction fullCompleteAction = new MultipleActions(completeAction);

		
	
		// ask for required items
    	getNPC().add(ConversationStates.ATTENDING, 
    				conversationPhraseForRequiredItem,
		    		new QuestActiveCondition(getQUEST_SLOT()),
		    		ConversationStates.QUESTION_1, 
		    		null,
		    		new SayRequiredItemsFromCollectionAction(getQUEST_SLOT(), "I'd still need [items]. Have you brought any?"));
    	
    	// player says he has a required item with him
    	getNPC().add(ConversationStates.QUESTION_1,
    				ConversationPhrases.YES_MESSAGES,
    				new QuestActiveCondition(getQUEST_SLOT()),
    				ConversationStates.QUESTION_1, 
    				"Wonderful, which one have you brought?",
    				null);
    	
    	// player says he didn't bring any items
    	getNPC().add(ConversationStates.QUESTION_1, 
					ConversationPhrases.NO_MESSAGES,
					new QuestActiveCondition(getQUEST_SLOT()),
					ConversationStates.ATTENDING,
					null,
					new SayRequiredItemsFromCollectionAction(getQUEST_SLOT(), didntBring));
    	
    	// meet again during quest
    	getNPC().add(ConversationStates.IDLE, 
	    		ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(
					new QuestActiveCondition(getQUEST_SLOT()),
					new GreetingMatchesNameCondition(getNPC().getName())),
				ConversationStates.ATTENDING,
				"Hello again. If you've brought me the things I need, I'll happily take them!",
				null);
    	
    	// add triggers for the item names
    	final ItemCollection items = new ItemCollection();
    	items.addFromQuestStateString(ITEM_LIST);
    	for (final Map.Entry<String, Integer> item : items.entrySet()) {
    		getNPC().add(ConversationStates.QUESTION_1,
    			item.getKey(),
    			new QuestActiveCondition(getQUEST_SLOT()),
    			ConversationStates.QUESTION_1,
    			null,
    			new CollectRequestedItemsAction(item.getKey(),
    				getQUEST_SLOT(),
    				"Wonderful! Did you bring anything else with you?", "I already have that.",
    				fullCompleteAction,
    				ConversationStates.ATTENDING));
    	}
	}
	
	@Override
	public String getHistoryNote(Player player){
		String historyNote = null;
		String questState = player.getQuest(getQUEST_SLOT());
		final ItemCollection missingItems = new ItemCollection();
		if(getState().equals(ITEM_LIST))
		{
			missingItems.addFromQuestStateString(questState);
			historyNote = "I still need to bring " + getNPC().getName() + " " 
							+ Grammar.enumerateCollection(missingItems.toStringList()) + ".";
			return historyNote;
		}
		return null;
	};

}
