package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.powers.jingjue.DapuFormPower;
import yourmod.powers.jingjue.SnakeBonePower;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;

public class SplashAction extends AbstractGameAction {
    private final AbstractMonster source;
    private final int base;
    private final int pct;

    public SplashAction(AbstractMonster source, int base, int pct) {
        this.source = source;
        this.base = base;
        this.pct = pct;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        int dmg = base * pct / 100;
        if (dmg <= 0) {
            isDone = true;
            return;
        }
        int hits = 0;
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m == source) {
                continue;
            }
            hits++;
            DamageInfo info = new DamageInfo(AbstractDungeon.player, dmg, DamageInfo.DamageType.NORMAL);
            addToTop(new JingjueDamageAction(m, info, AttackEffect.SLASH_HORIZONTAL, true, 0));
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(DapuFormPower.POWER_ID)
                    && DapuFormPower.isIce()) {
                addToTop(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                        m, AbstractDungeon.player, new WeakPower(m, 1, false), 1));
            }
            if (JingjueCombatState.splashTriggersExecute || (AbstractDungeon.player != null
                    && AbstractDungeon.player.hasPower(SnakeBonePower.POWER_ID))) {
                if (m.currentHealth - dmg <= JingjueCombatState.executeLine(m)) {
                    addToTop(new ExecuteAction(m));
                }
            }
        }
        if (hits >= 3 && AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(yourmod.powers.jingjue.SerpentForcePower.POWER_ID)
                && JingjueCombatState.snakeBoneTriggers < 5) {
            JingjueCombatState.snakeBoneTriggers++;
            JingjueCombatState.gainHourglass(1);
        }
        isDone = true;
    }
}
