package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.relics.PrismOpticSystem;

import static yourmod.ModFile.makeID;

/** 回旋 */
public class DamuhouSwivel extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSwivel");

    public DamuhouSwivel() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        PrismOpticSystem.swapIfPresent();
        addToBot(new DrawCardAction(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
