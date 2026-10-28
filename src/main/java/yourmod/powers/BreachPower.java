package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 破绽：每层使该敌人本回合受到的伤害 +10%。可叠加，玩家回合结束清除。
 */
public class BreachPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Breach");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final float PER_STACK = 0.10f;

    public BreachPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (amount > 0 && type != DamageInfo.DamageType.HP_LOSS) {
            return damage * (1.0f + PER_STACK * amount);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        int pct = Math.round(PER_STACK * 100f) * Math.max(0, amount);
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1] + pct + strings.DESCRIPTIONS[2];
    }
}
