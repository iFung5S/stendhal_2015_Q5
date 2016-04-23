/**
 * 
 */
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
 * @author Zhenzhong Xiao
 * 
 * Example quest of StatedAbstractQuest method
 *
 */
public class GordonCollection extends StatedAbstractQuest {
	
	// QUEST_SLOT identifier (unique for each quest)
	public static final String QUEST_SLOT = "gordon_collection";
	
	// All the name of the states in the quest
	public static final String STATE_START = "start_state";
	public static final String STATE_NEEDED_ITEMS = "pie=1;fish pie=1";
	public static final String STATE_FIND_Carmen = "find Carmen";
	public static final String STATE_FIND_Monogenes = "find Monogenes";
	public static final String STATE_FIND_Sato = "find Sato";
	public static final String STATE_FIND_Tad = "find Tad";
	public static final String STATE_FIND_GORDON = "find gordon";
	public static final String STATE_COMPLETE = "done";
	
	// Keywords for Paper Chase stage (the word that player needs to say to NPC) 
	public static final String PW_Carmen = "water";
	public static final String PW_Monogenes = "water";
	public static final String PW_Sato = "water";
	public static final String PW_Tad = "water";
	public static final String PW_GORDON = "water";
	
	// Cold down time before the quest become repeatable
	private static final int REQUIRED_MINUTES = 1;
	
	// Replies from each NPC at the end of each state
	private static final String QUEST_DETAIL = "Please bring me one pie and one fish pie.";
	private static final String START_PAPERCHASE = "Please go and tell Carmen: " + PW_Carmen;
	private static final String Carmen_REPLY = "Please go and tell Monogenes: " + PW_Monogenes;
	private static final String Monogenes_REPLY = "Please go and tell Sato: " + PW_Sato;
	private static final String Sato_REPLY = "Please go and tell Tad: " + PW_Tad;
	private static final String Tad_REPLY = "Please go and tell Gordon: " + PW_GORDON;
	private static final String GORDON_REPLY = "Thanks for your help!! You just save my life!";
	
	// The list that stores all the states
	private  List<AbstractQuestState> statesList = new LinkedList<AbstractQuestState>();

	
	@Override
	public String getName() {
		return "GordonCollection";
	}

	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}
	@Override
	public String getNPCName() {
		return "Gordon";
	}
	
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public List<AbstractQuestState> getStateList(){
		return statesList;
	}
	
	@Override
 	public int getMinLevel() {
 		return 0;
 	}

	
	@Override
	public void addToWorld() {
		// Three arguments:
		// Name of the quest; 
		// Short description of the quest;
		// Whether the quest is repeatable or not;
		fillQuestInfo("Pie collection for Gordon",
				"Gordon wants pies.",
				true);
		
		// All the NPC involves
		SpeakerNPC npc1 = npcs.get("Gordon");
		SpeakerNPC npc2 = npcs.get("Carmen");
		SpeakerNPC npc3 = npcs.get("Monogenes");
		SpeakerNPC npc4 = npcs.get("Sato");
		SpeakerNPC npc5 = npcs.get("Tad");
		
		// Set the quest to be repeatable
		setRepeatable(REQUIRED_MINUTES);
		
		// Create the start state
		StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_NEEDED_ITEMS, QUEST_SLOT,
															QUEST_DETAIL, REQUIRED_MINUTES);
		statesList.add(startState);
		
		// Create the Collecting items state
		StateCollectItems collectingState = new StateCollectItems(npc1, STATE_NEEDED_ITEMS, STATE_FIND_Carmen, QUEST_SLOT);
		collectingState.setFinishReply(START_PAPERCHASE);
		statesList.add(collectingState);
		
		// Create the Paper Chase state 1
		StatePaperChase paperChase1 = new StatePaperChase(npc2, STATE_FIND_Carmen, STATE_FIND_Monogenes, 
														QUEST_SLOT, PW_Carmen, Carmen_REPLY);
		statesList.add(paperChase1);
		
		// Create the Paper Chase state 2
		StatePaperChase paperChase2 = new StatePaperChase(npc3, STATE_FIND_Monogenes, STATE_FIND_Sato, 
														QUEST_SLOT, PW_Monogenes, Monogenes_REPLY);
		statesList.add(paperChase2);

		// Create the Paper Chase state 3
		StatePaperChase paperChase3 = new StatePaperChase(npc4, STATE_FIND_Sato, STATE_FIND_Tad, 
														QUEST_SLOT, PW_Sato, Sato_REPLY);
		statesList.add(paperChase3);

		// Create the Paper Chase state 4
		StatePaperChase paperChase4 = new StatePaperChase(npc5, STATE_FIND_Tad, STATE_FIND_GORDON, 
														QUEST_SLOT, PW_Tad, Tad_REPLY);
		statesList.add(paperChase4);
		
		StatePaperChase paperChase5 = new StatePaperChase(npc1, STATE_FIND_GORDON, STATE_COMPLETE, 
														QUEST_SLOT, PW_GORDON, GORDON_REPLY);
		// End of the quest reward (Add to the final state)
		paperChase5.setXP(200);
		paperChase5.addRewardItem("milk", 2);
		statesList.add(paperChase5);
    	
    	// Add all the states into the World
    	StatedAbstractQuest.addAllState(statesList);
	}

}
