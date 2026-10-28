package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import yourmod.actions.DamageIgnoreBlockAction;
import yourmod.actions.EasyXCostAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

/**
 * 天火！（X费）
 * 升级前：全体造成 X 段、每段 8 伤；有烬火每段再 +4；
 * 有静电则每段先挂 1 弱/1 易，再以普通攻击结算且无视格挡（共 X 次，非一次性挂 X）
 * 升级后：每段 10 伤；烬火额外改为每段 +5
 * <p>
 * 演出：套用六火亡魂「地狱火」的 {@link ScreenOnFireEffect} 全屏焰幕。
 */
public class HeavenFire extends AbstractEasyCard {
    public final static String ID = makeID("HeavenFire");

    public HeavenFire() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 8;
        baseSecondMagic = secondMagic = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            int perHit = params[0];
            int emberBonus = params[1];
            if (effect > 0) {
                // 与 Hexaghost Inferno 相同：先全屏着火，再逐段砸伤
                atb(new SFXAction("ATTACK_FIRE"));
                atb(new VFXAction(p, new ScreenOnFireEffect(), 1.0F));
            }
            for (int i = 0; i < effect; i++) {
                for (AbstractMonster mo : getEnemies()) {
                    if (mo.hasPower(StaticMarkPower.POWER_ID)) {
                        atb(new ApplyPowerAction(mo, p, new WeakPower(mo, 1, false), 1));
                        atb(new ApplyPowerAction(mo, p, new VulnerablePower(mo, 1, false), 1));
                    }
                }
                for (AbstractMonster mo : getEnemies()) {
                    int dmgAmt = perHit;
                    if (mo.hasPower(EmberFirePower.POWER_ID)) {
                        dmgAmt += emberBonus;
                    }
                    DamageInfo info = new DamageInfo(p, dmgAmt, DamageInfo.DamageType.NORMAL);
                    if (mo.hasPower(StaticMarkPower.POWER_ID)) {
                        atb(new DamageIgnoreBlockAction(mo, info, AbstractGameAction.AttackEffect.FIRE));
                    } else {
                        atb(new AbstractGameAction() {
                            @Override
                            public void update() {
                                info.applyPowers(p, mo);
                                addToTop(new DamageAction(mo, info, AttackEffect.FIRE));
                                isDone = true;
                            }
                        });
                    }
                }
                if (i < effect - 1) {
                    atb(new WaitAction(0.15F));
                }
            }
            return true;
        }, magicNumber, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        upgradeSecondMagic(1);
    }
}
