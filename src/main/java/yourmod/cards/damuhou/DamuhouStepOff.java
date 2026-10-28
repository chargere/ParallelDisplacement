package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouStepOffPower;

import static yourmod.ModFile.makeID;

/** 步出线 */
public class DamuhouStepOff extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouStepOff");

    public DamuhouStepOff() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new ApplyPowerAction(p, p, new DamuhouStepOffPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
