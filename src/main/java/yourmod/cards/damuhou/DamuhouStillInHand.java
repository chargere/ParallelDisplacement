package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 还在手上 */
public class DamuhouStillInHand extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouStillInHand");

    public DamuhouStillInHand() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        exhaust = true;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamuhouCombatState.addThisTurnNextCombo(magicNumber);
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
