package yourmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.dominic.FrostPayType;

import java.util.ArrayList;

/**
 * 多米尼克本场战斗临时状态。
 */
public class DominicCombatState {
    public static int frostGainedThisTurn = 0;
    public static AbstractCard neighborLeft = null;
    public static AbstractCard neighborRight = null;

    /** 本回合承受霜偿代价次数 */
    public static int frostPayAcceptedThisTurn = 0;
    /** 本回合取消霜偿次数 */
    public static int frostPayCancelledThisTurn = 0;
    /** 上一张牌是否取消了霜偿（供让路等） */
    public static boolean lastFrostPayCancelled = false;
    /** 本回合是否因冻裂失去过敏捷 */
    public static boolean lostDexThisTurn = false;
    /** 当前霜偿结算前是否已失敏（逆旅等） */
    public static boolean lostDexBeforeFrostPay = false;
    /** 本回合是否已用偿金优惠 */
    public static boolean frostPayDiscountUsedThisTurn = false;
    /** 霜噬印记本回合触发次数 */
    public static int frostBiteMarkTriggersThisTurn = 0;
    /** 债册：下回合抽牌（本回合最多记一次） */
    public static int debtLedgerDrawNextTurn = 0;
    public static int debtLedgerTriggersThisTurn = 0;
    /** 冰尘倍率（稀有冰尘风暴用，默认3） */
    public static int iceDustMultiplier = 3;
    /** 冰尘强制目标（冰尘风暴）；回合结束前保持 */
    public static AbstractMonster iceDustForcedTarget = null;
    /** 霜寒上限（霜裂可改为15） */
    public static int frostCap = 10;
    /** 极光帷幄：本回合剩余取消折扣次数 */
    public static int auroraCommandDiscountsLeft = 0;
    /** 精灰形态：本回合因消耗获精灰次数 */
    public static int ashExhaustGainThisTurn = 0;
    /** 本场是否已生成星灰绽放 */
    public static boolean ashBloomSpawned = false;
    /** 星灰绽放伤害 */
    public static int refinedAshBloomDamage = 36;
    /** 霜裂：本回合是否已触发顶满 AOE */
    public static boolean frostCapRiftTriggeredThisTurn = false;
    /** 星尘沉淀：本回合触发次数 */
    public static int stardustSettleTriggersThisTurn = 0;
    /** 顺路一剑：本回合是否已打出第一张攻击 */
    public static boolean firstAttackPlayedThisTurn = false;
    /** 倒影怀表：本回合是否仍可用取消耗霜 -1 */
    public static boolean mirrorWatchCancelAvailableThisTurn = false;
    /** 合上的星图：本场是否已触发 */
    public static boolean closedStarChartUsedThisCombat = false;
    /** 舱门上的霜：本场已触发回血次数 */
    public static int hatchFrostHealsThisCombat = 0;
    /** 弃笔：预见弃牌叠霜 */
    public static boolean abandonedQuillActive = false;
    public static int abandonedQuillFrostGained = 0;
    /** 星锻未完成稿：本场是否已触发星灭能量 */
    public static boolean unfinishedStarforgeTriggeredThisCombat = false;
    /** 星图最后一行：剩余归墟改写次数 */
    public static int lastLineChartRemaining = 0;
    /** 观测停笔：本回合是否已延迟 */
    public static boolean observationHaltUsedThisTurn = false;
    public static FrostPayType observationHaltPendingType = null;
    public static int observationHaltPendingValue = 0;
    /** 你怎么知道赢不了：本场是否已触发 */
    public static boolean howDoYouKnowTriggeredThisCombat = false;
    /** 同路余温：每张带霜偿牌额外格挡（0=未激活） */
    public static int sharedWarmthBlockPerCard = 0;
    /** 本回合打出带霜偿牌次数 */
    public static int frostPayCardsPlayedThisTurn = 0;
    /** 本回合进入消耗堆的牌 */
    public static final ArrayList<AbstractCard> exhaustedThisTurn = new ArrayList<>();

    public static void onBattleStart() {
        frostGainedThisTurn = 0;
        iceDustMultiplier = 3;
        iceDustForcedTarget = null;
        frostCap = 10;
        ashBloomSpawned = false;
        refinedAshBloomDamage = 36;
        exhaustedThisTurn.clear();
        clearNeighbors();
        resetTurnFlags();
        debtLedgerDrawNextTurn = 0;
        closedStarChartUsedThisCombat = false;
        hatchFrostHealsThisCombat = 0;
        unfinishedStarforgeTriggeredThisCombat = false;
        howDoYouKnowTriggeredThisCombat = false;
        lastLineChartRemaining = 0;
        observationHaltPendingType = null;
        observationHaltPendingValue = 0;
        sharedWarmthBlockPerCard = 0;
    }

    public static void onTurnStart() {
        frostGainedThisTurn = 0;
        exhaustedThisTurn.clear();
        clearNeighbors();
        resetTurnFlags();
    }

    private static void resetTurnFlags() {
        frostPayAcceptedThisTurn = 0;
        frostPayCancelledThisTurn = 0;
        lastFrostPayCancelled = false;
        lostDexThisTurn = false;
        lostDexBeforeFrostPay = false;
        frostPayDiscountUsedThisTurn = false;
        frostBiteMarkTriggersThisTurn = 0;
        debtLedgerTriggersThisTurn = 0;
        iceDustMultiplier = 3;
        iceDustForcedTarget = null;
        auroraCommandDiscountsLeft = 0;
        ashExhaustGainThisTurn = 0;
        frostCapRiftTriggeredThisTurn = false;
        stardustSettleTriggersThisTurn = 0;
        firstAttackPlayedThisTurn = false;
        abandonedQuillActive = false;
        abandonedQuillFrostGained = 0;
        observationHaltUsedThisTurn = false;
        frostPayCardsPlayedThisTurn = 0;
        sharedWarmthBlockPerCard = 0;
    }

    public static void clearNeighbors() {
        neighborLeft = null;
        neighborRight = null;
    }
}
