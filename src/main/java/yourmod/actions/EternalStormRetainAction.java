package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.relics.EternalStormAnchor;

/**
 * 永恒风暴的锚点：手动选择要保留的手牌；每保留 2 张失去 1 生命。
 */
public class EternalStormRetainAction extends AbstractGameAction {
    private final AbstractPlayer p;

    public EternalStormRetainAction() {
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
            // anyNumber=true, canPickZero=true：可选任意张，也可一张不留
            AbstractDungeon.handCardSelectScreen.open(
                    "选择要保留的牌（每 2 张失去 1 生命）",
                    p.hand.size(),
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
            int kept = 0;
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (!c.isEthereal) {
                    c.retain = true;
                }
                p.hand.addToTop(c);
                kept++;
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();

            int loss = kept / 2;
            if (loss > 0) {
                EternalStormAnchor relic = (EternalStormAnchor) p.getRelic(EternalStormAnchor.ID);
                if (relic != null) {
                    relic.flash();
                }
                addToTop(new LoseHPAction(p, p, loss));
            }
        }
        tickDuration();
    }
}
