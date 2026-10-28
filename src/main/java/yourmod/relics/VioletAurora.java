package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DamuhouCharacter;
import yourmod.powers.BreachPower;
import yourmod.util.DamuhouCombatState;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * violetaurora：攻击牌每段伤害给 2 层破绽；玩家回合结束清除。
 */
public class VioletAurora extends AbstractEasyRelic {
    public static final String ID = makeID("VioletAurora");
    public static final int BREACH_PER_HIT = 2;

    public VioletAurora() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, DamuhouCharacter.Enums.DAMUHOU_COLOR);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount <= 0 || !(target instanceof AbstractMonster)) {
            return;
        }
        DamuhouCombatState.recordAttackHit((AbstractMonster) target);
    }

    @Override
    public void onPlayerEndTurn() {
        Wiz.forAllMonstersLiving(m -> {
            if (m.hasPower(BreachPower.POWER_ID)) {
                addToBot(new RemoveSpecificPowerAction(m, AbstractDungeon.player, BreachPower.POWER_ID));
            }
        });
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VioletAurora();
    }
}
