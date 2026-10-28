package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 双面刃 */
public class DamuhouDoubleEdge extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouDoubleEdge");

    public DamuhouDoubleEdge() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 2;
        if (DamuhouCombatState.attacksPlayedThisTurn > 0) {
            hits += magicNumber;
        } else {
            addToBot(new ApplyPowerAction(p, p, new FrailPower(p, 1, false), 1));
        }
        comboHits(m, hits, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    @Override
    public void upp() {
        upgradeDamage(1);
    }
}
