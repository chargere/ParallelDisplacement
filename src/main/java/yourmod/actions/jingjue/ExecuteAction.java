package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import yourmod.powers.jingjue.HuntPower;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.powers.jingjue.SnakeBonePower;
import yourmod.powers.jingjue.TimeTurbulencePower;
import yourmod.powers.jingjue.WorldlyPearlPower;
import yourmod.relics.jingjue.CamelSkinRelic;
import yourmod.relics.jingjue.PhoenixGallbladderRelic;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;

public class ExecuteAction extends AbstractGameAction {
    private final AbstractMonster monster;

    public ExecuteAction(AbstractMonster monster) {
        this.monster = monster;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (monster == null || monster.isDeadOrEscaped()) {
            isDone = true;
            return;
        }
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(monster.hb.cX, monster.hb.cY, AttackEffect.FIRE));
        JingjueCombatState.executedThisTurn = true;
        JingjueCombatState.gainHourglass(1);
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(PhoenixGallbladderRelic.ID)) {
            AbstractDungeon.player.getRelic(PhoenixGallbladderRelic.ID).flash();
            JingjueCombatState.gainHourglass(1);
        }

        if (monster.type == AbstractMonster.EnemyType.BOSS) {
            int loss = Math.max(1, (int) (monster.maxHealth * JingjueCombatState.BOSS_EXECUTE));
            addToTop(new ApplyPowerAction(monster, AbstractDungeon.player, new WeakPower(monster, 1, false), 1));
            addToTop(new ApplyPowerAction(monster, AbstractDungeon.player, new VulnerablePower(monster, 1, false), 1));
            addToTop(new LoseHPAction(monster, AbstractDungeon.player, loss));
        } else if (monster.type == AbstractMonster.EnemyType.ELITE) {
            int loss = Math.max(1, (int) (monster.maxHealth * JingjueCombatState.ELITE_EXECUTE));
            addToTop(new ApplyPowerAction(monster, AbstractDungeon.player,
                    new ImaginaryPower(monster, 1), 1));
            addToTop(new LoseHPAction(monster, AbstractDungeon.player, loss));
        } else {
            TimeTurbulencePower.clear(monster);
            if (monster.hasPower(ImaginaryPower.POWER_ID)) {
                ((ImaginaryPower) monster.getPower(ImaginaryPower.POWER_ID)).clearStored();
            }
            monster.currentHealth = 0;
            monster.die();
        }

        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.hasPower(HuntPower.POWER_ID)) {
                addToTop(new DrawCardAction(AbstractDungeon.player, 1));
                JingjueCombatState.gainHourglass(1);
            }
            if (AbstractDungeon.player.hasRelic(CamelSkinRelic.ID)) {
                AbstractDungeon.player.getRelic(CamelSkinRelic.ID).flash();
                addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, CamelSkinRelic.HEAL));
            }
            if (AbstractDungeon.player.hasPower(WorldlyPearlPower.POWER_ID)) {
                WorldlyPearlPower wp = (WorldlyPearlPower) AbstractDungeon.player.getPower(WorldlyPearlPower.POWER_ID);
                addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, wp.healOnExecute));
                int splash = Math.max(1, monster.maxHealth * wp.splashPct / 100);
                Wiz.forAllMonstersLiving(m -> {
                    if (m != monster) {
                        addToTop(new LoseHPAction(m, AbstractDungeon.player, splash));
                    }
                });
            }
            if (AbstractDungeon.player.hasPower(yourmod.powers.jingjue.SnakeBonePower.POWER_ID)) {
                SnakeBonePower sb = (SnakeBonePower) AbstractDungeon.player.getPower(yourmod.powers.jingjue.SnakeBonePower.POWER_ID);
                int living = Wiz.getEnemies().size();
                if (living > 1) {
                    int share = Math.max(1, monster.maxHealth * sb.percent / 100 / (living - 1));
                    Wiz.forAllMonstersLiving(m -> {
                        if (m != monster) {
                            addToTop(new LoseHPAction(m, AbstractDungeon.player, share));
                        }
                    });
                }
            }
        }
        isDone = true;
    }
}
