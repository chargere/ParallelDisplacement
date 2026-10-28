package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 空仓 */
public class DamuhouEmptyMag extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouEmptyMag");

    public DamuhouEmptyMag() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> dump = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c.type == AbstractCard.CardType.ATTACK) {
                dump.add(c);
            }
        }
        for (AbstractCard c : dump) {
            p.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
        }
        int n = dump.size();
        if (n > 0) {
            addToBot(new GainEnergyAction(n));
            DamuhouCombatState.addThisTurnNextCombo(n);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
