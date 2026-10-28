package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 定点清除 */
public class DamuhouMarkedClear extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouMarkedClear");

    public DamuhouMarkedClear() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int saved = damage;
        if (targetHasBreach(m)) {
            damage += magicNumber;
        }
        comboHits(m, 2, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        damage = saved;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
