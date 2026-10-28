package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 短点射 */
public class DamuhouShortBurst extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouShortBurst");

    public DamuhouShortBurst() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 1;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 2;
        if (DamuhouCombatState.skillsPlayedThisTurn > 0) {
            hits += magicNumber;
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
