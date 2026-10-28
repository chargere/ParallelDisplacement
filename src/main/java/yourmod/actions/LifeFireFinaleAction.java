package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * 命火终章：伤害与击杀奖励同一 Action 内结算。
 * 击杀奖励必须在 {@code clearPostCombatActions} 之后再 {@code addToTop}，
 * 否则收掉最后一只怪时奖励会被清队列吞掉。
 */
public class LifeFireFinaleAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int damage;

    public LifeFireFinaleAction(AbstractPlayer player, AbstractMonster target, int damage) {
        this.player = player;
        this.target = target;
        this.damage = damage;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.FIRE;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        AbstractMonster m = (AbstractMonster) target;
        if (m != null && player != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.FIRE));
            m.damage(new DamageInfo(player, damage, DamageInfo.DamageType.NORMAL));

            boolean killed = !m.halfDead && (m.isDying || m.isDead || m.currentHealth <= 0);

            if (AbstractDungeon.getCurrRoom() != null
                    && AbstractDungeon.getCurrRoom().monsters != null
                    && AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (killed) {
                addToTop(new DrawCardAction(player, 3));
                addToTop(new ApplyPowerAction(player, player, new DexterityPower(player, 3), 3));
                addToTop(new ApplyPowerAction(player, player, new StrengthPower(player, 3), 3));
                addToTop(new GainEnergyAction(3));
            }
        }
        isDone = true;
    }
}
