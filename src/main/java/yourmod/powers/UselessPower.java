package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.util.FvvCombatState;

import static yourmod.ModFile.makeID;

/** 没用 */
public class UselessPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Useless");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static boolean muteNotify = false;

    public UselessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        if (amount <= 0 || owner == null || owner.isDead) {
            return;
        }
        flash();
        FvvCombatState.applyUselessStatTax(owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        FvvCombatState.restoreUselessStatTax(owner);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }

    public static int getAmount(AbstractCreature c) {
        if (c == null) {
            return 0;
        }
        AbstractPower p = c.getPower(POWER_ID);
        return p == null ? 0 : Math.max(0, p.amount);
    }

    public static int getEffective(AbstractCreature c) {
        int am = getAmount(c);
        if (c != null && c.hasPower(WasteCrownPower.POWER_ID)) {
            return Math.max(am, 10);
        }
        return am;
    }

    public static void gain(AbstractCreature target, AbstractCreature source, int amount) {
        if (target == null || amount <= 0) {
            return;
        }
        if (tryBornConvert(target)) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(target, source == null ? target : source, new UselessPower(target, amount), amount));
    }

    public static void gainTop(AbstractCreature target, AbstractCreature source, int amount) {
        if (target == null || amount <= 0) {
            return;
        }
        if (tryBornConvert(target)) {
            return;
        }
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(target, source == null ? target : source, new UselessPower(target, amount), amount));
    }

    /** 不触发胎教/委员等二次通知的叠层（内部加成用） */
    public static void gainSilent(AbstractCreature target, int amount) {
        if (target == null || amount <= 0) {
            return;
        }
        muteNotify = true;
        try {
            AbstractPower existing = target.getPower(POWER_ID);
            if (existing == null) {
                UselessPower p = new UselessPower(target, amount);
                target.powers.add(p);
                FvvCombatState.uselessGainedThisCombat += amount;
            } else {
                existing.amount += amount;
                existing.updateDescription();
                FvvCombatState.uselessGainedThisCombat += amount;
            }
        } finally {
            muteNotify = false;
        }
    }

    private static boolean tryBornConvert(AbstractCreature target) {
        AbstractPower born = target.getPower(BornWithItPower.POWER_ID);
        return born instanceof BornWithItPower && ((BornWithItPower) born).tryConvertToDraw();
    }

    public static int spendAll(AbstractCreature c) {
        int am = getAmount(c);
        if (am > 0 && c != null) {
            c.powers.removeIf(p -> POWER_ID.equals(p.ID));
        }
        return am;
    }

    public static int spend(AbstractCreature c, int amount) {
        if (c == null || amount <= 0) {
            return 0;
        }
        AbstractPower p = c.getPower(POWER_ID);
        if (p == null) {
            return 0;
        }
        int spent = Math.min(p.amount, amount);
        p.amount -= spent;
        if (p.amount <= 0) {
            c.powers.remove(p);
        } else {
            p.updateDescription();
        }
        return spent;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        notifyGained(stackAmount);
    }

    @Override
    public void onInitialApplication() {
        notifyGained(amount);
    }

    private void notifyGained(int am) {
        if (muteNotify || owner == null || am <= 0) {
            return;
        }
        FvvCombatState.uselessGainedThisCombat += am;
        AbstractPower admit = owner.getPower(AdmitCommitteePower.POWER_ID);
        if (admit instanceof AdmitCommitteePower) {
            ((AdmitCommitteePower) admit).onUselessGained(am);
        }
        AbstractPower fetal = owner.getPower(FetalFailPower.POWER_ID);
        if (fetal instanceof FetalFailPower) {
            ((FetalFailPower) fetal).onUselessGained();
        }
    }
}
