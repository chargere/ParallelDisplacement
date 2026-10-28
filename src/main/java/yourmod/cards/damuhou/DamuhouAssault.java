package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** A7 强袭 */
public class DamuhouAssault extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouAssault");

    public DamuhouAssault() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 8;
        isMultiDamage = true;
        printedCombo = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHitsAll(1, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
