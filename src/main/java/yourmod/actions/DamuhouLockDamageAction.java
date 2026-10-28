package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.damuhou.AbstractDamuhouCard;
import yourmod.powers.BreachPower;
import yourmod.powers.DamuhouLeftoverPower;
import yourmod.util.DamuhouCombatState;
import yourmod.util.DamuhouFireMode;

/**
 * 全自动：这一发打当前锁定的瞄具端。人死了，下一发会锁到新站上来的人。
 */
public class DamuhouLockDamageAction extends AbstractGameAction {
    private final AbstractDamuhouCard card;
    private final AttackEffect fx;
    private final boolean alt;

    public DamuhouLockDamageAction(AbstractDamuhouCard card, AttackEffect fx) {
        this(card, fx, false);
    }

    public DamuhouLockDamageAction(AbstractDamuhouCard card, AttackEffect fx, boolean alt) {
        this.card = card;
        this.fx = fx;
        this.alt = alt;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        AbstractMonster t = DamuhouFireMode.sightedLiving();
        if (t == null) {
            isDone = true;
            return;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(DamuhouLeftoverPower.POWER_ID)
                && DamuhouCombatState.lockTargetThisCard != null
                && DamuhouCombatState.lockTargetThisCard != t) {
            int extra = ((DamuhouLeftoverPower) AbstractDungeon.player.getPower(DamuhouLeftoverPower.POWER_ID)).overflowBreach();
            if (extra > 0) {
                addToTop(new ApplyPowerAction(t, AbstractDungeon.player, new BreachPower(t, extra), extra));
            }
        }
        DamuhouCombatState.lockTargetThisCard = t;
        card.calculateCardDamage(t);
        int amt = alt ? card.secondDamage : card.damage;
        AbstractDungeon.actionManager.addToTop(new DamageAction(t,
                new DamageInfo(AbstractDungeon.player, amt, card.damageTypeForTurn), fx));
        isDone = true;
    }
}
