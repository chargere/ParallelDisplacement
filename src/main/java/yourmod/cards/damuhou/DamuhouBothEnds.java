package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouFireMode;

import static yourmod.ModFile.makeID;

/** 两端点射：最左最右都打，不是抉择。 */
public class DamuhouBothEnds extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBothEnds");

    public DamuhouBothEnds() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.NONE);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster left = DamuhouFireMode.leftmostLiving();
        AbstractMonster right = DamuhouFireMode.rightmostLiving();
        if (left != null) {
            calculateCardDamage(left);
            directHits(left, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
        if (right != null && right != left) {
            calculateCardDamage(right);
            directHits(right, magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        printedCombo = magicNumber;
    }
}
