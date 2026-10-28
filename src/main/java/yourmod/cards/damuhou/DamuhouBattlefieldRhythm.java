package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouBattlefieldRhythmPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 战场节奏 */
public class DamuhouBattlefieldRhythm extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBattlefieldRhythm");

    public DamuhouBattlefieldRhythm() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DamuhouBattlefieldRhythmPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
    }
}
