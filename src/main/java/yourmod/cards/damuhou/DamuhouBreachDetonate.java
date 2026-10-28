package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;

import static yourmod.ModFile.makeID;

/** 破绽引爆 */
public class DamuhouBreachDetonate extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouBreachDetonate");

    public DamuhouBreachDetonate() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || !m.hasPower(BreachPower.POWER_ID)) {
            return;
        }
        int stacks = m.getPower(BreachPower.POWER_ID).amount;
        addToBot(new RemoveSpecificPowerAction(m, p, BreachPower.POWER_ID));
        if (stacks > 0) {
            addToBot(new DamageAction(m, new DamageInfo(p, stacks * magicNumber, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
