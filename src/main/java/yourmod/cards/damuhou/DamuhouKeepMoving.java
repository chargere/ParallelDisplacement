package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouKeepMovingPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 大步流星 */
public class DamuhouKeepMoving extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouKeepMoving");

    public DamuhouKeepMoving() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouKeepMovingPower(p, upgraded));
    }

    @Override
    public void upp() {
    }
}
