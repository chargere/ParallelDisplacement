package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 急停 */
public class DamuhouHardStop extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouHardStop");

    public DamuhouHardStop() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (p.hand.isEmpty()) {
            return;
        }
        addToBot(new SelectCardsAction(p.hand.group, 1, "选择弃置 1 张牌。", false, c -> true, cards -> {
            for (AbstractCard c : cards) {
                if (c.type == AbstractCard.CardType.ATTACK) {
                    DamuhouCombatState.addThisTurnNextCombo(magicNumber);
                }
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
