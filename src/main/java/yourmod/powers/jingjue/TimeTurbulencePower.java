package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 时空乱流：虚数跳过的意图在此倒计时，到点集中释放。 */
public class TimeTurbulencePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjTimeTurbulence");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int delayTurns;
    private boolean released;

    public TimeTurbulencePower(AbstractCreature owner, int storedDamage, int delayTurns) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, storedDamage);
        this.delayTurns = Math.max(0, delayTurns);
        this.amount2 = this.delayTurns;
        this.isTwoAmount = true;
        this.released = false;
        useVanillaIcon("timewarp");
        updateDescription();
    }

    public static TimeTurbulencePower get(AbstractCreature owner) {
        if (owner == null || !owner.hasPower(POWER_ID)) {
            return null;
        }
        return (TimeTurbulencePower) owner.getPower(POWER_ID);
    }

    public static int storedOn(AbstractCreature owner) {
        TimeTurbulencePower p = get(owner);
        return p == null ? 0 : Math.max(0, p.amount);
    }

    public static void storeImmediate(AbstractCreature owner, int dmg, int delayTurns) {
        if (owner == null || owner.isDeadOrEscaped() || dmg <= 0) {
            return;
        }
        if (owner instanceof AbstractMonster && JingjueCombatState.blocksTimeTurbulence((AbstractMonster) owner)) {
            return;
        }
        TimeTurbulencePower existing = get(owner);
        if (existing != null) {
            existing.addPacket(dmg, delayTurns);
            existing.flash();
            return;
        }
        TimeTurbulencePower created = new TimeTurbulencePower(owner, dmg, delayTurns);
        owner.addPower(created);
    }

    public static void clear(AbstractCreature owner) {
        TimeTurbulencePower p = get(owner);
        if (p != null) {
            p.amount = 0;
            p.delayTurns = 0;
            p.released = true;
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    public void addPacket(int dmg, int delayTurns) {
        if (dmg > 0) {
            amount += dmg;
        }
        this.delayTurns = Math.max(this.delayTurns, Math.max(0, delayTurns));
        amount2 = this.delayTurns;
        released = false;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        released = false;
        updateDescription();
    }

    public boolean shouldRelease() {
        return !released && delayTurns <= 0 && amount > 0;
    }

    public void releaseStored() {
        if (released) {
            return;
        }
        if (owner instanceof AbstractMonster && JingjueCombatState.blocksTimeTurbulence((AbstractMonster) owner)) {
            clear(owner);
            return;
        }
        int dmg = amount;
        released = true;
        amount = 0;
        delayTurns = 0;
        amount2 = 0;
        if (dmg > 0 && AbstractDungeon.player != null && !owner.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
            AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player,
                    new DamageInfo(owner, dmg, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SMASH));
        } else {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        if (shouldRelease()) {
            releaseStored();
        }
    }

    @Override
    public void atEndOfRound() {
        if (released) {
            return;
        }
        if (delayTurns > 0) {
            delayTurns--;
            amount2 = delayTurns;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (delayTurns <= 0) {
            locDesc("本回合造成 #b" + amount + " 点伤害。");
        } else if (delayTurns == 1) {
            locDesc("1 回合后造成 #b" + amount + " 点伤害。");
        } else {
            locDesc(delayTurns + " 回合后造成 #b" + amount + " 点伤害。");
        }
    }
}
