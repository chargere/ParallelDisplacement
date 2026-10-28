package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.relics.PrismOpticSystem;

import static yourmod.ModFile.makeID;

/** 换肩 */
public class DamuhouShoulderSwap extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouShoulderSwap");

    public DamuhouShoulderSwap() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        PrismOpticSystem.swapIfPresent();
    }

    @Override
    public void upp() {
        upgradeBlock(4);
    }
}
