package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.relics.jingjue.CausalSpindleRelic;
import yourmod.util.JingjueCombatState;
import yourmod.util.JingjueEtherealStore;

public class EtherealizeAction extends AbstractGameAction {
    private final AbstractCard card;
    private final int turns;
    private final boolean empower;

    public EtherealizeAction(AbstractCard card, int turns, boolean empower) {
        this.card = card;
        this.turns = Math.max(1, turns);
        this.empower = empower;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (card != null && AbstractDungeon.player != null) {
            AbstractDungeon.player.hand.removeCard(card);
            JingjueEtherealStore.add(card, turns, empower);
            if (AbstractDungeon.player.hasRelic(CausalSpindleRelic.ID)) {
                AbstractDungeon.player.getRelic(CausalSpindleRelic.ID).flash();
                JingjueCombatState.gainDilation(1);
            }
        }
        isDone = true;
    }
}
