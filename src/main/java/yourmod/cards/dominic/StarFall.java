package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

public class StarFall extends AbstractDominicCard {
    public static final String ID = makeID("StarFall");

    public StarFall() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 14;
        baseSecondDamage = 6;
        isMultiDamage = true;
        setFrostPay(3, FrostPayType.DELAY, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        int marked = 0;
        AbstractMonster lowest = null;
        for (AbstractMonster mo : getEnemies()) {
            if (mo.hasPower(StarMarkPower.POWER_ID)) {
                marked++;
            }
            if (lowest == null || mo.currentHealth < lowest.currentHealth) {
                lowest = mo;
            }
        }
        if (marked > 0 && lowest != null) {
            int total = secondDamage * marked;
            atb(new DamageAction(lowest, new DamageInfo(p, total, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeSecondDamage(2);
    }
}
