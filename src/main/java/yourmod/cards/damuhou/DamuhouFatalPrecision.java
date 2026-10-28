package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 致命精度 */
public class DamuhouFatalPrecision extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFatalPrecision");

    public DamuhouFatalPrecision() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 30;
        printedCombo = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1;
        int saved = damage;
        if (targetHpUnderPct(m, magicNumber)) {
            hits += 2;
            damage *= 2;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        damage = saved;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(10);
    }
}
