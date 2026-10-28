package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 紫焰覆盖 */
public class DamuhouVioletCover extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouVioletCover");

    public DamuhouVioletCover() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 3;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHitsAll(magicNumber, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        printedCombo = magicNumber;
    }
}
