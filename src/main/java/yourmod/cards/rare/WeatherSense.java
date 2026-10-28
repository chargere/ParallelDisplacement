package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 气象感应（0费）
 * 升级前：保留；按场上全体敌人烬火/静电【总层数】阈值触发（无印记回复5等）
 * 升级后：无印记时回复 8
 * 注：烬火≥5 的失血只打带烬火的敌人，不是全体。
 */
public class WeatherSense extends AbstractEasyCard {
    public final static String ID = makeID("WeatherSense");

    public WeatherSense() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        selfRetain = true;
        baseMagicNumber = magicNumber = 5;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int emberTotal = 0;
        int staticTotal = 0;
        boolean anyEmber = false;
        boolean anyStatic = false;
        for (AbstractMonster mo : getEnemies()) {
            int e = pwrAmt(mo, EmberFirePower.POWER_ID);
            int s = pwrAmt(mo, StaticMarkPower.POWER_ID);
            emberTotal += e;
            staticTotal += s;
            if (e > 0) anyEmber = true;
            if (s > 0) anyStatic = true;
        }

        // 烬火总层数（全体敌人合计）≥5：仅对带烬火的敌人各失去 12 生命
        if (emberTotal >= 5) {
            for (AbstractMonster mo : getEnemies()) {
                if (pwrAmt(mo, EmberFirePower.POWER_ID) > 0) {
                    atb(new DamageAction(mo, new DamageInfo(p, 12, DamageInfo.DamageType.HP_LOSS),
                            AbstractGameAction.AttackEffect.FIRE));
                }
            }
        }
        if (staticTotal >= 3) {
            atb(new GainEnergyAction(2));
        }
        if (anyEmber && anyStatic) {
            atb(new DrawCardAction(2));
        }
        if (emberTotal + staticTotal >= 8) {
            atb(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, 12));
        }
        if (!anyEmber && !anyStatic) {
            atb(new HealAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }
}
