package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

/**
 * 以普通攻击结算（吃易伤等），但本段伤害无视格挡且不消耗格挡。
 */
public class DamageIgnoreBlockAction extends AbstractGameAction {
    private final DamageInfo info;

    public DamageIgnoreBlockAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.target = target;
        this.info = info;
        this.attackEffect = effect;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (target == null || info == null) {
            isDone = true;
            return;
        }
        final AbstractCreature t = target;
        // 结算前重算，使本段刚挂上的易伤等生效
        if (info.owner != null) {
            info.applyPowers(info.owner, t);
        }
        final int savedBlock = t.currentBlock;
        t.currentBlock = 0;
        // addToTop 后进先出：先结算伤害，再恢复格挡
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (!t.isDead && !t.isDying) {
                    t.currentBlock = savedBlock;
                }
                isDone = true;
            }
        });
        addToTop(new DamageAction(t, info, attackEffect));
        isDone = true;
    }
}
