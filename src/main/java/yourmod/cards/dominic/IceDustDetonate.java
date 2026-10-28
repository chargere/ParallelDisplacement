package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.powers.StarMarkPower;
import yourmod.util.DominicHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class IceDustDetonate extends AbstractDominicCard {
    public static final String ID = makeID("IceDustDetonate");

    public IceDustDetonate() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 7;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (m != null) {
            AbstractPower mark = m.getPower(StarMarkPower.POWER_ID);
            if (mark != null && mark.amount > 0) {
                int stacks = mark.amount;
                atb(new RemoveSpecificPowerAction(m, p, StarMarkPower.POWER_ID));
                for (int i = 0; i < stacks; i++) {
                    DominicHooks.onStarMarkConsumed(p);
                }
                atb(new DamageAction(m, new DamageInfo(p, magicNumber * stacks, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}
