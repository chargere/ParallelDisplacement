package yourmod.util;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import yourmod.powers.XingNianPower;

import java.util.LinkedList;
import java.util.List;

import static yourmod.util.Wiz.*;

/**
 * 全局连携系统（时间旅人核心机制之一）。
 *
 * 每打出一张牌，把它的「牌型」(攻 / 技 / 能) 推进一个【跨回合累计】的队列；
 * 若队列末尾凑齐某个预定义模式（如「攻攻技」= 攻击·攻击·技能），则触发该模式
 * 绑定的全部效果，并【把这截序列从队列消费掉】，腾出来续接后续出牌。
 *
 * 设计要点：
 *  - 队列在同一场战斗内跨回合保留（用于凑长连携）；新战斗开始时必须 reset() 清空。
 *  - 匹配只看队列【末尾】，命中即从尾部删除该模式长度，可连锁触发。
 *  - 效果由模式统一决定，不挂在单张卡上，避免「某张卡的特殊效果」。
 */
public class ComboSystem {
    public enum CType { ATTACK, SKILL, POWER }

    /** 跨战斗累计的出牌序列（同一战斗内跨回合不清） */
    private static final LinkedList<CType> queue = new LinkedList<>();

    /** 内置模式：攻攻技 = 攻击·攻击·技能 */
    private static final CType[] PATTERN_GGZ = {CType.ATTACK, CType.ATTACK, CType.SKILL};

    /** 新战斗开始时清空队列（避免上场残留） */
    public static void reset() {
        queue.clear();
    }

    /** 打出一张牌时调用：推进牌型 → 落形态临时力量 → 结算连携 */
    public static void record(AbstractPlayer p, AbstractCard card) {
        CType t = toType(card);
        if (t == null) return;
        queue.addLast(t);

        // 落形态：打出攻击牌获得 1 层临时力量（下回合开始由 XingNianPower 回收）
        if (t == CType.ATTACK) {
            FormUtil.gainLuoTempStrength(p);
        }

        resolve(p);
    }

    private static CType toType(AbstractCard c) {
        switch (c.type) {
            case ATTACK: return CType.ATTACK;
            case SKILL:  return CType.SKILL;
            case POWER:  return CType.POWER;
            default:     return null;
        }
    }

    /** 末尾匹配：命中模式则触发并从尾部消费，循环连锁 */
    private static void resolve(AbstractPlayer p) {
        boolean again;
        do {
            again = false;
            if (endsWith(PATTERN_GGZ)) {
                trigger(p, PATTERN_GGZ);
                for (int i = 0; i < PATTERN_GGZ.length; i++) queue.pollLast();
                again = true; // 可能连锁触发下一个模式
            }
            // 后续在此追加更多模式分支
        } while (again);
    }

    private static boolean endsWith(CType[] pat) {
        if (queue.size() < pat.length) return false;
        int start = queue.size() - pat.length;
        for (int i = 0; i < pat.length; i++) {
            if (queue.get(start + i) != pat[i]) return false;
        }
        return true;
    }

    /** 模式触发：按模式绑定效果结算 */
    private static void trigger(AbstractPlayer p, CType[] pat) {
        if (pat == PATTERN_GGZ) {
            boolean zhu = FormUtil.getForm(FormUtil.getXingNian(p)) == FormUtil.ZHU;
            int xnGain = zhu ? 3 : 2;   // 驻形态额外 +1 星念
            int energy = zhu ? 2 : 1;   // 驻形态额外 +1 能量
            addXingNian(p, xnGain);
            atb(new GainEnergyAction(energy));
        }
    }

    /** 增加星念（封顶 CAP；无则新建 Power） */
    private static void addXingNian(AbstractPlayer p, int amt) {
        XingNianPower xn = (XingNianPower) p.getPower(XingNianPower.POWER_ID);
        if (xn != null) {
            if (xn.amount < XingNianPower.CAP) {
                xn.amount = Math.min(xn.amount + amt, XingNianPower.CAP);
                xn.updateDescription();
            }
        } else {
            applyToSelf(new XingNianPower(p, Math.min(amt, XingNianPower.CAP)));
        }
    }

    /** 供 UI / 调试：当前队列牌型快照 */
    public static List<CType> snapshot() {
        return new LinkedList<>(queue);
    }
}
