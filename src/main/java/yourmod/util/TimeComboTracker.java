package yourmod.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 时间旅人起始卡连携追踪：记录上一张打出的牌，供「一念 / 异位」判定连携条件。
 * - lastAttackTarget：上一张【攻击】牌的目标（用于一念的「同目标」判定）
 * - lastWasSkill：上一张牌是否为【技能】（用于异位的「上一张也是技能」判定）
 * 注意：技能牌不刷新 lastAttackTarget，因此一念只与「上一张攻击牌」对比，符合设计语义。
 */
public class TimeComboTracker {
    public static AbstractMonster lastAttackTarget = null;
    public static boolean lastWasSkill = false;

    /** 攻击牌打出后更新：记录目标，并标记「上一张不是技能」 */
    public static void recordAttack(AbstractMonster m) {
        lastAttackTarget = m;
        lastWasSkill = false;
    }

    /** 技能牌打出后更新：标记「上一张是技能」（不改攻击目标记录） */
    public static void recordSkill() {
        lastWasSkill = true;
    }

    /** 战斗开始时清空 */
    public static void reset() {
        lastAttackTarget = null;
        lastWasSkill = false;
    }
}
