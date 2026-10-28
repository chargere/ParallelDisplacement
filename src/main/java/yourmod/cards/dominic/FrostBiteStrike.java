package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FrostBiteStrike extends AbstractDominicCard {
    public static final String ID = makeID("FrostBiteStrike");

    public FrostBiteStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 4;
        baseSecondDamage = 4;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        int spend = Math.min(3, FrostColdPower.getFrost(p));
        if (spend > 0) {
            FrostColdPower.spendFrostActive(p, spend);
            int bonus = secondDamage * spend;
            atb(new DamageAction(m, new DamageInfo(p, bonus, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondDamage(1);
    }
}
