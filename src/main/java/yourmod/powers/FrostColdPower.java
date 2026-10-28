package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.util.DominicCombatState;
import yourmod.util.DominicHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 霜寒：回合开始转化为临时力量/敏捷后减半；上限由 getCap()，溢出转冰尘伤害并挂星痕。
 */
public class FrostColdPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FrostCold");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final int CAP = 10;

    public FrostColdPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        priority = 10;
    }

    public static int getCap() {
        return Math.max(1, DominicCombatState.frostCap);
    }

    public static int getFrost(AbstractCreature c) {
        AbstractPower p = c.getPower(POWER_ID);
        return p == null ? 0 : p.amount;
    }

    /** 获得霜寒；超过上限的部分转为冰尘。返回实际叠上层数。 */
    public static int gainFrost(AbstractCreature owner, int amount) {
        if (owner == null || amount <= 0) {
            return 0;
        }
        DominicCombatState.frostGainedThisTurn += amount;
        AbstractPower existing = owner.getPower(POWER_ID);
        int current = existing == null ? 0 : existing.amount;
        int cap = getCap();
        int room = Math.max(0, cap - current);
        int applied = Math.min(room, amount);
        int overflow = amount - applied;

        if (applied > 0) {
            atb(new ApplyPowerAction(owner, owner, new FrostColdPower(owner, applied), applied));
            if (current < cap && current + applied >= cap) {
                DominicHooks.onFrostReachedCap(owner);
            }
        }
        if (overflow > 0) {
            triggerIceDust(owner, overflow);
        }
        return applied;
    }

    /** 立即获得霜寒（用于遗物开战等需同步层数的场景）。 */
    public static void gainFrostImmediate(AbstractCreature owner, int amount) {
        if (owner == null || amount <= 0) {
            return;
        }
        DominicCombatState.frostGainedThisTurn += amount;
        AbstractPower existing = owner.getPower(POWER_ID);
        int current = existing == null ? 0 : existing.amount;
        int cap = getCap();
        int room = Math.max(0, cap - current);
        int applied = Math.min(room, amount);
        int overflow = amount - applied;
        if (applied > 0) {
            if (existing == null) {
                owner.addPower(new FrostColdPower(owner, applied));
            } else {
                existing.stackPower(applied);
                existing.updateDescription();
            }
            if (current < cap && current + applied >= cap) {
                DominicHooks.onFrostReachedCap(owner);
            }
        }
        if (overflow > 0) {
            triggerIceDustImmediate(owner, overflow);
        }
    }

    public static void spendFrost(AbstractCreature owner, int amount) {
        if (owner == null || amount <= 0) {
            return;
        }
        AbstractPower p = owner.getPower(POWER_ID);
        if (p == null) {
            return;
        }
        p.reducePower(amount);
        if (p.amount <= 0) {
            owner.powers.remove(p);
        } else {
            p.updateDescription();
        }
    }

    /** 主动耗霜（会触发霜噬印记等） */
    public static void spendFrostActive(AbstractCreature owner, int amount) {
        spendFrost(owner, amount);
        if (owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            DominicHooks.onActiveFrostSpend(
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) owner, amount);
        }
    }

    public static boolean canSpend(AbstractCreature owner, int amount) {
        return getFrost(owner) >= amount;
    }

    public static void triggerIceDust(AbstractCreature owner, int overflow) {
        int damage = overflow * DominicCombatState.iceDustMultiplier;
        AbstractMonster forced = DominicCombatState.iceDustForcedTarget;
        if (forced != null && !forced.isDeadOrEscaped()) {
            atb(new DamageAction(forced, new DamageInfo(owner, damage, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            atb(new ApplyPowerAction(forced, owner, new StarMarkPower(forced, 1), 1));
        } else {
            atb(new DamageRandomEnemyAction(new DamageInfo(owner, damage, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster m = null;
                    if (AbstractDungeon.getMonsters() != null) {
                        m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    }
                    if (m != null && !m.isDeadOrEscaped()) {
                        atb(new ApplyPowerAction(m, owner, new StarMarkPower(m, 1), 1));
                    }
                    isDone = true;
                }
            });
        }
        if (owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            DominicHooks.onIceDust((com.megacrit.cardcrawl.characters.AbstractPlayer) owner);
        }
    }

    private static void triggerIceDustImmediate(AbstractCreature owner, int overflow) {
        int damage = overflow * DominicCombatState.iceDustMultiplier;
        AbstractMonster m = DominicCombatState.iceDustForcedTarget;
        if (m == null || m.isDeadOrEscaped()) {
            if (AbstractDungeon.getMonsters() != null) {
                m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }
        }
        if (m != null && !m.isDeadOrEscaped()) {
            m.damage(new DamageInfo(owner, damage, DamageInfo.DamageType.THORNS));
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(m, owner, new StarMarkPower(m, 1), 1));
        }
        if (owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            DominicHooks.onIceDust((com.megacrit.cardcrawl.characters.AbstractPlayer) owner);
        }
    }

    @Override
    public void atStartOfTurn() {
        if (amount <= 0) {
            return;
        }
        int str = amount;
        int dex = amount / 2;
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, str), str));
        addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, str), str));
        if (dex > 0) {
            addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, dex), dex));
            addToBot(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, dex), dex));
        }

        int before = amount;
        int newAmount;
        if (owner.getPower(EternalAuroraPower.POWER_ID) != null) {
            newAmount = Math.max(0, amount - 2);
        } else {
            newAmount = amount / 2;
        }
        int lost = before - newAmount;

        AbstractPower ashForm = owner.getPower(RefinedAshFormPower.POWER_ID);
        if (ashForm instanceof RefinedAshFormPower && lost > 0) {
            ((RefinedAshFormPower) ashForm).gainAshFromFrostLoss(lost);
        }

        if (newAmount <= 0) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
        } else {
            this.amount = newAmount;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
