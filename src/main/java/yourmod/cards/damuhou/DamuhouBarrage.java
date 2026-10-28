package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** A8 弹幕压制 */
public class DamuhouBarrage extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBarrage");

    public DamuhouBarrage() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 4;
        printedCombo = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
