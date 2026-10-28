package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/** 牢郑跑路了：移除目标全部烬火/静电，每层格挡并抽牌（有上限）。 */
public class LaoZhengFled extends AbstractEasyCard {
    public final static String ID = makeID("LaoZhengFled");

    public LaoZhengFled() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseBlock = 4;
        baseMagicNumber = magicNumber = 5;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        int stacks = 0;
        AbstractPower ember = m.getPower(EmberFirePower.POWER_ID);
        AbstractPower stat = m.getPower(StaticMarkPower.POWER_ID);
        if (ember != null) {
            stacks += ember.amount;
            atb(new RemoveSpecificPowerAction(m, p, EmberFirePower.POWER_ID));
        }
        if (stat != null) {
            stacks += stat.amount;
            atb(new RemoveSpecificPowerAction(m, p, StaticMarkPower.POWER_ID));
        }
        if (stacks > 0) {
            atb(new GainBlockAction(p, p, block * stacks));
            atb(new DrawCardAction(Math.min(stacks, magicNumber)));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}
