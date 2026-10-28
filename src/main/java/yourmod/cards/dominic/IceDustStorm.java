package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

public class IceDustStorm extends AbstractDominicCard {
    public static final String ID = makeID("IceDustStorm");

    public IceDustStorm() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        exhaust = true;
        baseMagicNumber = magicNumber = 5;
        setFrostPay(3, FrostPayType.FREEZE_CRACK, 3);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        DominicCombatState.iceDustMultiplier = magicNumber;
        DominicCombatState.iceDustForcedTarget = m;
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
