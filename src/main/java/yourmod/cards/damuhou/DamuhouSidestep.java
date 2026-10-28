package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.AutoAdd;

import static yourmod.ModFile.makeID;

/** 卡池占位。正式卡组写好后删。 */
@AutoAdd.Ignore
public class DamuhouSidestep extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSidestep");

    public DamuhouSidestep() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
