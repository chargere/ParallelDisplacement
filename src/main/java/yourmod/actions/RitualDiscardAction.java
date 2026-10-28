package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;
import yourmod.util.Wiz;

/** 祭礼：选 1 张弃掉，按类型结算。 */
public class RitualDiscardAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final int ember;
    private final int staticAmt;
    private final int drawOnJunk;
    private boolean opened;

    public RitualDiscardAction(AbstractPlayer p, int ember, int staticAmt, int drawOnJunk) {
        this.p = p;
        this.ember = ember;
        this.staticAmt = staticAmt;
        this.drawOnJunk = drawOnJunk;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DISCARD;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            if (p.hand.isEmpty()) {
                isDone = true;
                return;
            }
            if (p.hand.size() == 1) {
                resolve(p.hand.getBottomCard());
                isDone = true;
                return;
            }
            opened = true;
            AbstractDungeon.handCardSelectScreen.open("选择弃掉一张牌", 1, false, false);
            tickDuration();
            return;
        }
        if (opened && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                resolve(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void resolve(AbstractCard c) {
        p.hand.addToTop(c);
        p.hand.moveToDiscardPile(c);
        if (c.type == AbstractCard.CardType.ATTACK) {
            for (AbstractMonster m : Wiz.getEnemies()) {
                AbstractDungeon.actionManager.addToTop(
                        new ApplyPowerAction(m, p, new EmberFirePower(m, ember), ember));
            }
        } else if (c.type == AbstractCard.CardType.SKILL) {
            for (AbstractMonster m : Wiz.getEnemies()) {
                AbstractDungeon.actionManager.addToTop(
                        new ApplyPowerAction(m, p, new StaticMarkPower(m, staticAmt), staticAmt));
            }
        } else if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(drawOnJunk));
        }
    }
}
