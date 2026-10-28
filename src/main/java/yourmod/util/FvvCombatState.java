package yourmod.util;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.powers.AfkPower;

/**
 * fvv蛋蛋本场战斗状态。
 */
public class FvvCombatState {
    public static int damageDealtThisTurn = 0;
    public static int cardsPlayedThisTurn = 0;
    public static int blockGainedThisTurn = 0;
    public static int uselessGainedThisCombat = 0;
    public static AbstractCard.CardType lastCardType = null;
    /** 打出当前牌之前的上一张牌类型（供连招卡使用） */
    public static AbstractCard.CardType previousCardType = null;
    public static boolean playedSkillThisTurn = false;
    public static boolean playedZeroCostThisTurn = false;
    public static int lowPlayStreak = 0;
    public static int cardsPlayedLastTurn = 0;
    public static boolean turnTrackingStarted = false;
    /** 本回合已扣除、回合结束需归还的力量/敏捷。花掉 没用 后仍归还，避免永久掉点。 */
    public static int uselessStatTax = 0;

    public static void onBattleStart() {
        damageDealtThisTurn = 0;
        cardsPlayedThisTurn = 0;
        blockGainedThisTurn = 0;
        uselessGainedThisCombat = 0;
        lastCardType = null;
        previousCardType = null;
        playedSkillThisTurn = false;
        playedZeroCostThisTurn = false;
        lowPlayStreak = 0;
        cardsPlayedLastTurn = 0;
        turnTrackingStarted = false;
        uselessStatTax = 0;
    }

    public static void applyUselessStatTax(AbstractCreature owner, int stacks) {
        restoreUselessStatTax(owner);
        if (owner == null || stacks <= 0) {
            return;
        }
        uselessStatTax = stacks;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new StrengthPower(owner, -stacks), -stacks));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, -stacks), -stacks));
    }

    public static void restoreUselessStatTax(AbstractCreature owner) {
        if (uselessStatTax <= 0 || owner == null) {
            uselessStatTax = 0;
            return;
        }
        int n = uselessStatTax;
        uselessStatTax = 0;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new StrengthPower(owner, n), n));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, n), n));
    }

    public static void onPlayerTurnStart() {
        if (turnTrackingStarted) {
            cardsPlayedLastTurn = cardsPlayedThisTurn;
            int threshold = getAfkPlayLimit();
            if (cardsPlayedLastTurn <= threshold) {
                lowPlayStreak++;
            } else {
                lowPlayStreak = 0;
            }
        }
        turnTrackingStarted = true;
        damageDealtThisTurn = 0;
        cardsPlayedThisTurn = 0;
        blockGainedThisTurn = 0;
        lastCardType = null;
        previousCardType = null;
        playedSkillThisTurn = false;
        playedZeroCostThisTurn = false;
    }

    private static int getAfkPlayLimit() {
        if (AbstractDungeon.player != null) {
            AbstractPower p = AbstractDungeon.player.getPower(AfkPower.POWER_ID);
            if (p instanceof AfkPower) {
                return ((AfkPower) p).playLimit;
            }
        }
        return 1;
    }

    public static void onCardPlayed(AbstractCard c) {
        if (c == null) {
            return;
        }
        previousCardType = lastCardType;
        cardsPlayedThisTurn++;
        lastCardType = c.type;
        if (c.type == AbstractCard.CardType.SKILL) {
            playedSkillThisTurn = true;
        }
        if (c.costForTurn == 0 || c.freeToPlayOnce) {
            playedZeroCostThisTurn = true;
        }
    }
}
