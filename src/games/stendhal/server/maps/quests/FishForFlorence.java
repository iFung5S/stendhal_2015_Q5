package games.stendhal.server.maps.quests;

import java.util.LinkedList;
import java.util.List;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.quests.logic.AbstractQuestState;
import games.stendhal.server.maps.quests.logic.StateAskForQuest;
import games.stendhal.server.maps.quests.logic.StateCollectItems;
import games.stendhal.server.maps.quests.logic.StatePaperChase;
/**
 * 
 * @author Suhasini Suresh
 * 
 * Simple collection quest to test StatedAbstractQuest method
 *
 */

public class FishForFlorence extends StatedAbstractQuest {
	// QUEST_SLOT identifier (unique for each quest)
    public static final String QUEST_SLOT = "fish_for_florence";
    
    // All the name of the states in the quest
   	public static final String STATE_START = "start_state";
   	public static final String STATE_NEEDED_ITEMS = "salmon = 1;seabass = 2;cod = 1;";
   	public static final String STATE_FISHMONGER= "fishmonger";
   	public static final String STATE_DONE = "done";	
   	
    // Cool down time before the quest become repeatable
   	private static final int REQUIRED_MINUTES = 1;
   	
  	public static final String PW_FLORENCE = "done";
// Replies from each NPC at the end of each state
	private static final String QUEST_DETAIL = "Please bring me one salmon fish, 2 seabass and one cod from the fishmonger.";
	private static final String FISHMONGER_REPLY = "Here are all the fish that you need";
	private static final String FLORENCE_REPLY = "Thank you for your help young one! Let me make you a bowl of hot soup!";
   	
   // The list that stores all the states
   private  List<AbstractQuestState> statesList = new LinkedList<AbstractQuestState>();

	@Override
	public List<AbstractQuestState> getStateList() {
		return statesList;
	}

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public void addToWorld() {
		// Three arguments:
	// Name of the quest; 
		// Short description of the quest;
		// Whether the quest is repeatable or not;
		fillQuestInfo("Fish collection for Florence",
	    				"Florence wants fish.",
	 	     			true);
				
	    // All the NPCs involved
		SpeakerNPC npc1 = npcs.get("Florence Boullabaisse");
		//SpeakerNPC npc2 = npcs.get("Fishmonger");
		
		// Set the quest to be repeatable
		setRepeatable(REQUIRED_MINUTES);
		
		// Create the start state
		StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_NEEDED_ITEMS, QUEST_SLOT,
															QUEST_DETAIL, REQUIRED_MINUTES);
		statesList.add(startState);
		
		// Create the Collecting items state
		StateCollectItems collectingState = new StateCollectItems(npc1, STATE_NEEDED_ITEMS, STATE_FISHMONGER, QUEST_SLOT);
		collectingState.setFinishReply(FISHMONGER_REPLY);
		statesList.add(collectingState);
	
	
		// Give items state
		StatePaperChase paperChase = new StatePaperChase(npc1, STATE_FISHMONGER, STATE_DONE, 
																QUEST_SLOT, PW_FLORENCE, FLORENCE_REPLY);
				// End of the quest reward (Add to the final state)
				paperChase.setXP(300);
				paperChase.addRewardItem("fish soup", 1);
				statesList.add(paperChase);

				// Add all the states into the World
		    	StatedAbstractQuest.addAllState(statesList);
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_SURROUNDS;
	}
	
	@Override
	public String getNPCName() {
	  return "Florence";
	}
	@Override
	public String getName() {
	
	  return "Fish for Florence" ;
	}
	
	@Override
		public int getMinLevel() {
	 		return 0;
	 	}
}