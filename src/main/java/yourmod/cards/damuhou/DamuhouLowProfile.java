package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouLowProfilePower;

import static yourmod.ModFile.makeID;

/** 低姿躲避 */
public class DamuhouLowProfile extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouLowProfile");

    public DamuhouLowProfile() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new ApplyPowerAction(p, p, new DamuhouLowProfilePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
