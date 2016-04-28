package games.stendhal.server.maps.quests;

	import java.util.List;
	import java.util.LinkedList;

	import games.stendhal.server.entity.npc.SpeakerNPC;
	import games.stendhal.server.maps.Region;
	import games.stendhal.server.maps.quests.logic.AbstractQuestState;
	import games.stendhal.server.maps.quests.logic.StateAskForQuest;
	import games.stendhal.server.maps.quests.logic.StatePaperChase;
	/**
	 * 
	 * @author Suhasini Suresh
	 * 
	 * Simple paper chase quest to test StatedAbstractQuest method
	 *
	 */

	public class PaperChaseForAlexander extends StatedAbstractQuest {
		// QUEST_SLOT identifier (unique for each quest)
	    public static final String QUEST_SLOT = "paper_chase_for_alexander";
	    
	    // All the name of the states in the quest
	   	public static final String STATE_START = "start_state";
	   	public static final String STATE_FIND_Coralia = "find Coralia";
	   	public static final String STATE_FIND_Dale = "find Dale";
	   	public static final String STATE_FIND_David = "find David";
	   	public static final String STATE_FIND_Alexander = "find Alexander";
	   	public static final String STATE_DONE = "done";	
	   	
	   	
	 // Keywords for Paper Chase stage (the word that player needs to say to NPC) 
	   	public static final String PW_Coralia = "fruits";
	   	public static final String PW_Dale = "hot";
	   	public static final String PW_David = "swimsuit";
	   	public static final String PW_Alexander = "stop";
	   	
	   // Replies from each NPC at the end of each state
	   	private static final String QUEST_DETAIL = "Pass around messages from person to person, please";
	   	
	   	private static final String START_PAPERCHASE = "Please go and tell Coralia I like her " + PW_Coralia;
	   	private static final String Coralia_REPLY = "Please go and tell Dale he is " + PW_Dale;
	  	private static final String Dale_REPLY = "Please go and tell David I like his " + PW_David;
	   	private static final String David_REPLY = "Please go and tell Alexander to " + PW_Alexander;
	   	private static final String Alexander_REPLY = "Thanks for your help!";
	   
	   	
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
			fillQuestInfo("Paper chase for Alexander",
		    				"Alexander wants information passed around",
		 	     			false);
					
		    // All the NPCs involved
			SpeakerNPC npc1 = npcs.get("Alexander");
			SpeakerNPC npc2 = npcs.get("Coralia");
			SpeakerNPC npc3 = npcs.get("Dale");
			SpeakerNPC npc4 = npcs.get("David");
	
			// Create the start state
			StateAskForQuest startState = new StateAskForQuest(npc1, STATE_START, STATE_FIND_Coralia, QUEST_SLOT,
																QUEST_DETAIL);
			statesList.add(startState);
			startState.setYesToQuest(START_PAPERCHASE);
			
			// Create the Paper Chase state 1
			StatePaperChase paperChase1 = new StatePaperChase(npc2, STATE_FIND_Coralia, STATE_FIND_Dale, 
																QUEST_SLOT, PW_Coralia, Coralia_REPLY);
			statesList.add(paperChase1);
					
			// Create the Paper Chase state 2
			StatePaperChase paperChase2 = new StatePaperChase(npc3, STATE_FIND_Dale, STATE_FIND_David, 
																	QUEST_SLOT, PW_Dale, Dale_REPLY);
			statesList.add(paperChase2);
			
			// Create the Paper Chase state 3
			StatePaperChase paperChase3 = new StatePaperChase(npc4, STATE_FIND_David, STATE_FIND_Alexander, 
																	QUEST_SLOT, PW_David, David_REPLY);
			statesList.add(paperChase3);
		
			// Give items state
			StatePaperChase paperChase = new StatePaperChase(npc1, STATE_FIND_Alexander, STATE_DONE, 
																	QUEST_SLOT, PW_Alexander, Alexander_REPLY);
			// End of the quest reward (Add to the final state)
			paperChase.setXP(500);
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
		  return "Alexander";
		}

		@Override
		public String getName() {
		
		  return "PaperChaseForAlexander" ;
		}
		
		@Override
			public int getMinLevel() {
			return 0;
		 	}
	}
