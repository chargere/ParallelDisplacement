package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 第四种存在：时间旅人的专属被动（由起始遗物「风色x落陨」赋予）。
 * - 生命被称作「存在力」（显示层：名字 + 血量图标 -> 星云），**上限不变**；
 * - 失去生命时，只会失去 1 点存在力（攻击、自伤、其他来源一律适用），
 *   因此伤害数值不再重要，重要的是「被命中多少次」。
 * 图标暂借原版「无形」图标占位（语义接近：超脱于常规生命形式）。
 */
public class FourthExistencePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FourthExistence");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /** 存在力上限 */
    public static final int EXISTENCE_MAX = 21;

    /** 与生命值相关的增益 / 减益，对存在力一律除以这个值（向下取整） */
    public static final int HP_SCALE_DIVISOR = 4;

    public FourthExistencePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        useVanillaIcon("intangible");
    }

    /** 被攻击时：无论伤害多少，存在力只 -1 */
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        return damageAmount > 0 ? 1 : damageAmount;
    }

    /** 非攻击来源失去生命（自伤、诅咒等）：同样只 -1 */
    @Override
    public int onLoseHp(int damageAmount) {
        return damageAmount > 0 ? 1 : damageAmount;
    }

    /**
     * 治疗：一切与生命值相关的增益 / 减益，对存在力一律 ÷4（向下取整）。
     * 例：治疗 9 → 2 点；治疗 3 → 0 点。
     */
    @Override
    public int onHeal(int healAmount) {
        return healAmount > 0 ? Math.floorDiv(healAmount, HP_SCALE_DIVISOR) : healAmount;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
