package games.stendhal.server.maps.quests;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.CollectRequestedItemsAction;
import games.stendhal.server.entity.npc.action.EquipRandomAmountOfItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayRequiredItemsFromCollectionAction;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SetQuestToTimeStampAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.ItemCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * QUEST: Pie collection for Peter
 * 
 * PARTICIPANTS:
 * <ul>
 * <li>Peter (Goatherd in 0_semos_mountain_n2_w)</li>
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li>Peter introduces himself and asks for a variety of pies.</li>
 * <li>You collect the items.</li>
 * <li>Peter sees your items, asks for them then thanks you.</li>
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li>XP: 500</li>
 * <li><1-5> Milk</li>
 * <li><2-8> Cheese</li>
 * </ul>
 * 
 * REPETITIONS:
 * <ul>
 * <li>After 1 day</li>
 * </ul>
 * 
 * @author Zhenzhong Xiao
 */
public class PieCollection extends AbstractQuest {

	/**
	 * NOTE: Reward has not been set, nor has the XP.
	 * left them default here, but in the JUnit test
	 * called reward item "REWARD" temporarily
	 */
	
    public static final String QUEST_SLOT = "pies_peter";
    
    /** 
     * The delay between repeating quests.
     * 1 day
     */
	private static final int REQUIRED_MINUTES = 1440;
    
    /**
	 * Required items for the quest.
	 */
	protected static final String NEEDED_ITEMS = "apple pie=1;cherry pie=1;pie=1;fish pie=1";
 
    @Override
    public void addToWorld() {
        fillQuestInfo("Pie collection for Peter",
				"The goatherd in semo mountain, Peter, searches for different pies for his stomach.",
				true);
        prepareQuestStep();
        prepareBringingStep();
    }
 
    @Override
    public String getSlotName() {
        return QUEST_SLOT;
    }
 
    @Override
    public String getName() {
        return "PieCollection";
    }
    
 	@Override
 	public int getMinLevel() {
 		return 0;
 	}
 	
 	@Override
 	public boolean isRepeatable(final Player player) {
 		return new AndCondition(
 					new QuestStateStartsWithCondition(QUEST_SLOT, "done;"),
 					new TimePassedCondition(QUEST_SLOT, 1, REQUIRED_MINUTES)).fire(player, null, null);
 	}
 	
 	@Override
 	public String getRegion() {
 		return Region.SEMOS_SURROUNDS;
 	}
 
 	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("Peter asked me for all kind of hot pies for his stomach.");
		final String questState = player.getQuest(QUEST_SLOT);
		
		if ("rejected".equals(questState)) {
			// quest rejected
			res.add("I decided not find Peter any pie, someone else will help him.");
		} else if (!player.isQuestCompleted(QUEST_SLOT)) {
			// not yet finished
			final ItemCollection missingItems = new ItemCollection();
			missingItems.addFromQuestStateString(questState);
			res.add("I still need to bring Peter " + Grammar.enumerateCollection(missingItems.toStringList()) + ".");
		} else if (isRepeatable(player)) {
			// may be repeated now
			res.add("It's been a while since I brought Peter hot pies, I wonder if the he is hungry again?");
        } else {
        	// not (currently) repeatable
        	res.add("I brought Peter lots of pies, he don't want to see any pie for now.");
		}
		return res;
	}
    
    public void prepareQuestStep() {
    	SpeakerNPC npc = npcs.get("Peter");
    	
    	// various quest introductions
    	
    	// offer quest first time
    	npc.add(ConversationStates.ATTENDING,
    		ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, "pie"),
    		new AndCondition(
    			new QuestNotStartedCondition(QUEST_SLOT),
    			new QuestNotInStateCondition(QUEST_SLOT, "rejected")),
    		ConversationStates.QUEST_OFFERED,
    		"There are not much hot food around here. I really want to eat some pies. Could you please get me some pies?",
    		null);
    	
    	// ask for quest again after rejected
    	npc.add(ConversationStates.ATTENDING, 
    		ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, "food"),
    		new QuestInStateCondition(QUEST_SLOT, "rejected"),
    		ConversationStates.QUEST_OFFERED, 
    		"Are you willing to find me some hot pies yet?",
    		null);
    	
    	// repeat quest
    	npc.add(ConversationStates.ATTENDING,
            ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, "food"),
            new AndCondition(
            	new QuestCompletedCondition(QUEST_SLOT),
            	new TimePassedCondition(QUEST_SLOT, 1, REQUIRED_MINUTES)),
            ConversationStates.QUEST_OFFERED,
            "I'm sorry to say that I am hungry again. " +
            "Would you be kind enough to find me some more?",
            null);
    	    	
    	// quest inactive    	
    	npc.add(ConversationStates.ATTENDING,
        	ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, "food"),
        	new AndCondition(
        		new QuestCompletedCondition(QUEST_SLOT),
        		new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, REQUIRED_MINUTES))),
        	ConversationStates.ATTENDING,
        	"I am sooooooooo full, let me take a rest....",
        	null);
    	
    	// end of quest introductions
    	
    	
    	// introduction chat    	
    	npc.add(ConversationStates.ATTENDING,
        	"food",
        	new AndCondition(
        		new QuestNotStartedCondition(QUEST_SLOT),
        		new QuestNotInStateCondition(QUEST_SLOT, "rejected")),
        	ConversationStates.ATTENDING,
        	"I WANT FOOD!!!! Save me please!! I love #pies...",
        	null);
    	
    	// accept quest response
    	npc.add(ConversationStates.QUEST_OFFERED,
    		ConversationPhrases.YES_MESSAGES,
    		null,
    		ConversationStates.QUESTION_1,
    		null,
			new MultipleActions(
				new SetQuestAction(QUEST_SLOT, NEEDED_ITEMS),
				new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "You are so kind!! I'd like all kind of pie: [items].")));
    	
    	// reject quest response
    	npc.add(ConversationStates.QUEST_OFFERED,
        	ConversationPhrases.NO_MESSAGES,
        	null,
        	ConversationStates.ATTENDING,
        	"Ok, just let me starve to death then...",
        	new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
    	
    	// meet again during quest
    	npc.add(ConversationStates.IDLE, 
    		ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(
				new QuestActiveCondition(QUEST_SLOT),
				new GreetingMatchesNameCondition(npc.getName())),
			ConversationStates.ATTENDING,
			"Hello again. If you've brought me some hot #pies as my food, I'll happily take them!",
			null);

    }
    
    
    private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Peter");
		
		// ask for required items
    	npc.add(ConversationStates.ATTENDING, 
    		ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, "pie"),
    		new QuestActiveCondition(QUEST_SLOT),
    		ConversationStates.QUESTION_2, 
    		null,
    		new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I'd still like [items]. Have you brought any?"));
    	
    	// player says he didn't bring any items
		npc.add(ConversationStates.QUESTION_2, 
			ConversationPhrases.NO_MESSAGES,
			new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_1,
			null,
			new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Oh, that's a shame, do tell me when you find some. I'd still like [items]."));
    	
    	// player says he has a required item with him
		npc.add(ConversationStates.QUESTION_2,
			ConversationPhrases.YES_MESSAGES,
			new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_2, 
			"Wonderful, which pie have you brought?",
			null);
    	
		// set up next step
    	ChatAction completeAction = new  MultipleActions(
			new SetQuestAction(QUEST_SLOT, "done"),
			new SayTextAction("I have never been this happy before!!! If you want, you can have closer look of me goat."
								+ " Here are some rewards for you."),
			new IncreaseXPAction(500),
			new IncreaseKarmaAction(5),
			new EquipRandomAmountOfItemAction("milk", 1, 5),
			new EquipRandomAmountOfItemAction("cheese", 2, 8),
			new SetQuestToTimeStampAction(QUEST_SLOT, 1)
		);
    	
    	// add triggers for the item names
    	final ItemCollection items = new ItemCollection();
    	items.addFromQuestStateString(NEEDED_ITEMS);
    	for (final Map.Entry<String, Integer> item : items.entrySet()) {
    		npc.add(ConversationStates.QUESTION_2,
    			item.getKey(),
    			new QuestActiveCondition(QUEST_SLOT),
    			ConversationStates.QUESTION_2,
    			null,
    			new CollectRequestedItemsAction(item.getKey(),
    				QUEST_SLOT,
    				"Wonderful! Did you bring anything else with you?", "I already have enough of those.",
    				completeAction,
    				ConversationStates.ATTENDING));
    	}
    }

	@Override
	public String getNPCName() {
		return "Peter";
	}
}