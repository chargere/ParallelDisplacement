package yourmod.cards.dominic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.MemoryBurnoutPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class MemoryBurnout extends AbstractDominicCard {
    public static final String ID = makeID("MemoryBurnout");

    public MemoryBurnout() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        isInnate = true;
        exhaust = true;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new MemoryBurnoutPower(p));
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
