package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 破绽当弹 */
public class DamuhouBreachRounds extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBreachRounds");

    public DamuhouBreachRounds() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1 + targetBreachStacks(m) / Math.max(1, magicNumber);
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
    }
}
