package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import yourmod.powers.FrostColdPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class MeteorShatter extends AbstractDominicCard {
    public static final String ID = makeID("MeteorShatter");

    public MeteorShatter() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 6;
        isMultiDamage = true;
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        int count = 0;
        for (AbstractMonster mo : Wiz.getEnemies()) {
            atb(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber));
            count++;
        }
        if (count > 0) {
            FrostColdPower.gainFrost(p, count);
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
