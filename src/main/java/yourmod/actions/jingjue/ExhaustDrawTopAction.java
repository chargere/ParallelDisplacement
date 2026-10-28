package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.util.Wiz;

/** 消耗抽牌堆顶：攻击打出伤害，技能拿格挡，能力立刻获得；并往弃牌堆塞一张同类型牌。 */
public class ExhaustDrawTopAction extends AbstractGameAction {
    public ExhaustDrawTopAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            isDone = true;
            return;
        }
        if (p.drawPile.isEmpty()) {
            if (!p.discardPile.isEmpty()) {
                addToTop(new ExhaustDrawTopAction());
                addToTop(new EmptyDeckShuffleAction());
            }
            isDone = true;
            return;
        }
        AbstractCard c = p.drawPile.getTopCard();
        p.drawPile.moveToExhaustPile(c);
        if (c.type == AbstractCard.CardType.ATTACK) {
            AbstractMonster m = Wiz.getRandomEnemy();
            if (m != null) {
                c.calculateCardDamage(m);
                int d = Math.max(0, c.damage);
                if (d > 0) {
                    addToTop(new JingjueDamageAction(m, new DamageInfo(p, d, DamageInfo.DamageType.NORMAL),
                            AttackEffect.SLASH_HORIZONTAL, false, 0));
                }
            }
            addSameTypeToDiscard(c.type);
        } else if (c.type == AbstractCard.CardType.SKILL) {
            if (c.baseBlock > 0) {
                c.applyPowers();
                if (c.block > 0) {
                    addToTop(new GainBlockAction(p, p, c.block));
                }
            }
            addSameTypeToDiscard(c.type);
        } else if (c.type == AbstractCard.CardType.POWER) {
            playBurnedPower(p, c);
            addSameTypeToDiscard(c.type);
        }
        isDone = true;
    }

    private void addSameTypeToDiscard(AbstractCard.CardType type) {
        if (type != AbstractCard.CardType.ATTACK && type != AbstractCard.CardType.SKILL
                && type != AbstractCard.CardType.POWER) {
            return;
        }
        AbstractCard gen = AbstractDungeon.returnTrulyRandomCardInCombat(type);
        if (gen != null) {
            addToBot(new MakeTempCardInDiscardAction(gen.makeCopy(), 1));
        }
    }

    private void playBurnedPower(AbstractPlayer p, AbstractCard burned) {
        AbstractCard play = burned.makeStatEquivalentCopy();
        play.freeToPlayOnce = true;
        play.purgeOnUse = true;
        play.exhaustOnUseOnce = false;
        play.energyOnUse = Math.max(0, EnergyPanel.totalCount);
        AbstractMonster m = Wiz.getRandomEnemy();
        play.use(p, m);
    }
}
