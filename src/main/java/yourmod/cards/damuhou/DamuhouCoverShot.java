package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 掩护射击 */
public class DamuhouCoverShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCoverShot");

    public DamuhouCoverShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1;
        if (!DamuhouCombatState.playerDamagedThisTurn) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeDamage(5);
        printedCombo = 0;
    }
}
