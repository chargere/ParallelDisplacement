package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.cards.dominic.AbstractDominicCard;

import static yourmod.ModFile.makeID;

/** 顺路的剑穗：每回合首张带霜偿攻击结算后获得4格挡 */
public class SwordTasselAlong extends AbstractEasyRelic {
    public static final String ID = makeID("SwordTasselAlong");
    private static final int BLOCK = 4;
    private boolean firstAttackWithFrostPayThisTurn = true;

    public SwordTasselAlong() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public void atTurnStart() {
        firstAttackWithFrostPayThisTurn = true;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!firstAttackWithFrostPayThisTurn) {
            return;
        }
        if (card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        if (!(card instanceof AbstractDominicCard)) {
            return;
        }
        AbstractDominicCard dc = (AbstractDominicCard) card;
        if (dc.frostPayCost <= 0) {
            return;
        }
        firstAttackWithFrostPayThisTurn = false;
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SwordTasselAlong();
    }
}
