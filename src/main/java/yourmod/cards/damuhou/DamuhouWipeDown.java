package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 擦拭 */
public class DamuhouWipeDown extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouWipeDown");

    public DamuhouWipeDown() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, 2));
        addToBot(new SelectCardsAction(p.hand.group, 1, "选择弃置 1 张牌。", false, c -> true, cards -> {
            for (AbstractCard c : cards) {
                if (c.type == AbstractCard.CardType.ATTACK) {
                    DamuhouCombatState.addThisTurnNextCombo(magicNumber);
                } else if (c.type == AbstractCard.CardType.SKILL) {
                    DamuhouCombatState.nextSkillCostRefund += secondMagic;
                }
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}
