package yourmod.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.powers.XingNianPower;

/**
 * 星念三形态（纯自动，随星念数跳）：
 * 溯 0~3（蓄势）/ 驻 4~7（稳定）/ 落 8~12（爆发）。
 * 形态只作读值，不存储；任何需要形态的地方都调用 getForm(星念)。
 */
public class FormUtil {
    public static final int SU = 0;   // 溯
    public static final int ZHU = 1;  // 驻
    public static final int LUO = 2;  // 落

    /** 由星念数推导当前形态（边界 7 归驻，落从 8 起） */
    public static int getForm(int xingNian) {
        if (xingNian <= 3) return SU;
        if (xingNian <= 7) return ZHU;
        return LUO;
    }

    public static String formName(int form) {
        switch (form) {
            case SU:  return "溯";
            case ZHU: return "驻";
            default:  return "落";
        }
    }

    /** 读取玩家当前星念数（无则 0） */
    public static int getXingNian(AbstractPlayer p) {
        AbstractPower pw = p.getPower(XingNianPower.POWER_ID);
        return pw == null ? 0 : pw.amount;
    }

    /**
     * 落形态：打出攻击牌获得 1 层临时力量（回合末回收）。
     * 非落形态或星念 Power 不存在时静默无操作。
     */
    public static void gainLuoTempStrength(AbstractPlayer p) {
        if (getForm(getXingNian(p)) != LUO) return;
        XingNianPower xn = (XingNianPower) p.getPower(XingNianPower.POWER_ID);
        if (xn != null) xn.gainLuoTempStrength();
    }
}
