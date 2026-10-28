package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.relics.ThreeDayHatch;

/**
 * 三天的舱门：回合结束选择至多 1 张手牌保留。
 */
public class ThreeDayHatchRetainAction extends AbstractGameAction {
    private final AbstractPlayer p;

    public ThreeDayHatchRetainAction() {
        this.p = AbstractDungeon.player;
        setValues(p, p, 1);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {
            if (p.hand.isEmpty() || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                isDone = true;
                return;
            }
            if (p.hasRelic("Runic Pyramid")) {
                isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(
                    "选择1张保留的牌",
                    1,
                    true,
                    true,
                    false,
                    false,
                    false);
            addToBot(new WaitAction(0.25F));
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (!c.isEthereal) {
                    c.retain = true;
                }
                p.hand.addToTop(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();

            ThreeDayHatch relic = (ThreeDayHatch) p.getRelic(ThreeDayHatch.ID);
            if (relic != null) {
                relic.flash();
            }
        }
        tickDuration();
    }
}
