package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.JingjueCombatState;

public class JingjueDamageAction extends DamageAction {
    private final AbstractMonster monster;
    private final boolean splash;
    private final int splashPct;
    private final int printedDamage;
    private boolean followupQueued;

    public JingjueDamageAction(AbstractMonster target, DamageInfo info, AttackEffect effect,
                               boolean splash, int splashPct) {
        super(target, info, effect);
        this.monster = target;
        this.splash = splash;
        this.splashPct = splashPct;
        this.printedDamage = info.base;
    }

    @Override
    public void update() {
        super.update();
        if (!isDone || followupQueued || monster == null) {
            return;
        }
        followupQueued = true;
        if (!splash && !monster.isDead && !monster.isDying
                && monster.currentHealth > 0
                && monster.currentHealth <= JingjueCombatState.executeLine(monster)) {
            AbstractDungeon.actionManager.addToTop(new ExecuteAction(monster));
        }
        if (!splash && splashPct > 0 && printedDamage > 0) {
            AbstractDungeon.actionManager.addToTop(new SplashAction(monster, printedDamage, splashPct));
        }
        if (!splash) {
            JingjueCombatState.recordAttack(null, printedDamage);
        }
    }
}
