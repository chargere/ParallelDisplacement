package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import yourmod.powers.FourthExistencePower;

/**
 * 第四种存在 · 生命缩放：
 * 最大生命值的增减（increaseMaxHp / decreaseMaxHealth）没有 Power 钩子，
 * 只能在这里拦截原方法，把数值 ÷4（向下取整）。
 * 治疗走 AbstractPower.onHeal（由 FourthExistencePower 覆盖），此处不再处理，避免除两次。
 */
public class ExistenceHpScalePatch {

    private static boolean appliesTo(AbstractCreature c) {
        return c != null && c.hasPower(FourthExistencePower.POWER_ID);
    }

    /** 增加最大生命：÷4 */
    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp", paramtypez = {int.class, boolean.class})
    public static class IncreaseMaxHp {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __instance, @ByRef int[] amount, boolean showEffect) {
            if (appliesTo(__instance) && amount[0] > 0) {
                amount[0] = Math.floorDiv(amount[0], FourthExistencePower.HP_SCALE_DIVISOR);
            }
        }
    }

    /** 减少最大生命：÷4（对存在力而言是减益变小） */
    @SpirePatch(clz = AbstractCreature.class, method = "decreaseMaxHealth", paramtypez = {int.class})
    public static class DecreaseMaxHealth {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __instance, @ByRef int[] amount) {
            if (appliesTo(__instance) && amount[0] > 0) {
                amount[0] = Math.floorDiv(amount[0], FourthExistencePower.HP_SCALE_DIVISOR);
            }
        }
    }
}
