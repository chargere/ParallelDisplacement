package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.damuhou.AbstractDamuhouCard;

/** 半自动：人还在就打，人没了就把这下变成格挡。 */
public class DamuhouHalfHitOrBlockAction extends AbstractGameAction {
    private final AbstractDamuhouCard card;
    private final AbstractMonster clicked;
    private final AttackEffect fx;
    private final int missBlock;

    public DamuhouHalfHitOrBlockAction(AbstractDamuhouCard card, AbstractMonster clicked,
                                       AttackEffect fx, int missBlock) {
        this.card = card;
        this.clicked = clicked;
        this.fx = fx;
        this.missBlock = missBlock;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        AbstractMonster t = clicked;
        if (t == null || t.isDeadOrEscaped()) {
            if (missBlock > 0) {
                addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, missBlock));
            }
            isDone = true;
            return;
        }
        card.calculateCardDamage(t);
        addToTop(new DamageAction(t,
                new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), fx));
        isDone = true;
    }
}
