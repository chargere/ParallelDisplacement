package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

public class FrostBreath extends AbstractDominicCard {
    public static final String ID = makeID("FrostBreath");

    public FrostBreath() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 6;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        for (AbstractMonster mo : getEnemies()) {
            atb(new ApplyPowerAction(mo, p, new WeakPower(mo, secondMagic, false), secondMagic));
        }
        if (FrostColdPower.getFrost(p) >= 6) {
            atb(new GainBlockAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}
