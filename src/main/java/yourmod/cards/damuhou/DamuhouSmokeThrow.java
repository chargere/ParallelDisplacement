package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouSmokePower;

import static yourmod.ModFile.makeID;

/** 烟幕投出 */
public class DamuhouSmokeThrow extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSmokeThrow");

    public DamuhouSmokeThrow() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new ApplyPowerAction(p, p, new DamuhouSmokePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
