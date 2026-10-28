package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouMiddleCountsPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 无处可逃 */
public class DamuhouMiddleCounts extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouMiddleCounts");

    public DamuhouMiddleCounts() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouMiddleCountsPower(p));
    }

    @Override
    public void upp() {
        isInnate = true;
    }
}
