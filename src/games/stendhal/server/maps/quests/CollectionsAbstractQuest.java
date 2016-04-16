package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.List;

public abstract class CollectionsAbstractQuest extends AbstractQuest{

	@Override
	public abstract String getSlotName();

	@Override
	public abstract void addToWorld();

	@Override
	public List<String> getHistory(final Player player) {
		return null;
	}
	
	@Override
	public abstract String getName();
	
	@SuppressWarnings("unused")
	private void step_1(final String name, final String questSlot,final String stateStart, final String priorMessage, final String completionMessage, final String inQuestMessage ) {
		final SpeakerNPC npc = npcs.get(name);
		
		// Before starting quest
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestNotStartedCondition(questSlot),
				ConversationStates.QUEST_OFFERED, 
				priorMessage,
				null);
		
		// During quest
        npc.add(ConversationStates.ATTENDING,
                ConversationPhrases.QUEST_MESSAGES,
                new QuestInStateCondition(questSlot, 0, stateStart),
                ConversationStates.ATTENDING,
                inQuestMessage,
                null);

		
		// After completing quest
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestCompletedCondition(questSlot),
				ConversationStates.ATTENDING,
				completionMessage,
				null);

	}
	
	public abstract List<String> getNeededItems();
	
	public void rewardPlayer(final Player player, double karma, int xp, final Item item) {
		item.setBoundTo(player.getName());
		player.equipOrPutOnGround(item);
		player.addKarma(karma);
		player.addXP(xp);
	}
	
	
	

}
