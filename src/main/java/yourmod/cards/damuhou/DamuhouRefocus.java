package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.relics.PrismOpticSystem;

import static yourmod.ModFile.makeID;

/** 重新对焦 */
public class DamuhouRefocus extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouRefocus");

    public DamuhouRefocus() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        PrismOpticSystem.chooseIfPresent();
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
