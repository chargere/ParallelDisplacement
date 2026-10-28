package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 开局：快速瞄准 */
public class DamuhouQuickAim extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouQuickAim");

    public DamuhouQuickAim() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamuhouCombatState.addThisTurnNextCombo(magicNumber);
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}
