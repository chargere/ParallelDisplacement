package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** A10 反击射击 */
public class DamuhouCounterShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCounterShot");

    public DamuhouCounterShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1;
        if (DamuhouCombatState.playerDamagedThisTurn) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
