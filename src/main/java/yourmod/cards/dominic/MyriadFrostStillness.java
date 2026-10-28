package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.powers.FreezeCrackPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

public class MyriadFrostStillness extends AbstractDominicCard {
    public static final String ID = makeID("MyriadFrostStillness");

    public MyriadFrostStillness() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 20;
        baseMagicNumber = magicNumber = 1;
        selfRetain = true;
        setFrostPay(4, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        for (AbstractMonster mo : getEnemies()) {
            atb(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber));
        }
        if (!DominicCombatState.lastFrostPayCancelled) {
            FreezeCrackPower.apply(p, 2);
        }
    }

    @Override
    public void upp() {
        upgradeBlock(5);
    }
}
