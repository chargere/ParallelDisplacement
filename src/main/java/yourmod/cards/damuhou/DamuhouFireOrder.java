package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 开火令 */
public class DamuhouFireOrder extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFireOrder");

    public DamuhouFireOrder() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
