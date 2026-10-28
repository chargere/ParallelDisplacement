package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** A2 抵近压制 */
public class DamuhouCloseSuppress extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCloseSuppress");

    public DamuhouCloseSuppress() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1;
        if (targetHpOverHalf(m)) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
