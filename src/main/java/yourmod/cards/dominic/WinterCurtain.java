package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class WinterCurtain extends AbstractDominicCard {
    public static final String ID = makeID("WinterCurtain");

    public WinterCurtain() {
        super(ID, 2, CardType.SKILL, CardRarity.BASIC, CardTarget.ENEMY);
        baseBlock = 15;
        baseMagicNumber = magicNumber = 2;
        setFrostPay(2, FrostPayType.DELAY);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(4);
        upgradeMagicNumber(1);
    }
}
