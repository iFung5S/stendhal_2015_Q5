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

public class BreadForAdena extends StatedAbstractQuest {
	// QUEST_SLOT identifier (unique for each quest)
    public static final String QUEST_SLOT = "bread_for_adena";
    
    // All the name of the states in the quest
   	public static final String STATE_START = "start_state";
   	public static final String STATE_NEEDED_ITEMS = "bread = 2;";
   	public static final String STATE_BAKER = "baker";
   	public static final String STATE_DONE = "done";	
   	
    // Cool down time before the quest become repeatable
   	private static final int REQUIRED_MINUTES = 1;
   	
   	public static final String PW_ADENA = "done";
   	
   // Replies from each NPC at the end of each state
   	private static final String QUEST_DETAIL = "Please bring me two breads from #Erna the baker.";
   	private static final String BAKER_REPLY = "Here are all the bread that you need";
   	private static final String ADENA_REPLY = "Thank you for your help young one! Let me give you some apples as a reward";
   	
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
		fillQuestInfo("Bread collection for Adena",
	    				"Adena wants breads",
	 	     			true);
				
	    // All the NPCs involved
		SpeakerNPC npc1 = npcs.get("Adena");
		SpeakerNPC npc2 = npcs.get("erna");
				
		// Set the quest to be repeatable
		setRepeatable(REQUIRED_MINUTES);
		
		// Create the start state
		StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_NEEDED_ITEMS, QUEST_SLOT,
															QUEST_DETAIL, REQUIRED_MINUTES);
		statesList.add(startState);
		
		// Create the Collecting items state
		StateCollectItems collectingState = new StateCollectItems(npc2, STATE_NEEDED_ITEMS, STATE_BAKER, QUEST_SLOT);
		collectingState.setFinishReply(BAKER_REPLY);
		statesList.add(collectingState);
	
	
		// Give items state
		StatePaperChase paperChase = new StatePaperChase(npc1, STATE_BAKER, STATE_DONE, 
																QUEST_SLOT, PW_ADENA, ADENA_REPLY);
				// End of the quest reward (Add to the final state)
		paperChase.setXP(300);
	   paperChase.addRewardItem("apples", 10);
		statesList.add(paperChase);		    	

		// Add all the states into the World
		StatedAbstractQuest.addAllState(statesList);
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
	
	@Override
	public String getNPCName() {
	  return "Adena";
	}

	@Override
	public String getName() {
	
	  return "BreadForAdena" ;
	}
	
	@Override
		public int getMinLevel() {
		return 0;
	 	}
}