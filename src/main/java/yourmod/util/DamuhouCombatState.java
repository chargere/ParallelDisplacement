package yourmod.util;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;
import yourmod.relics.VioletAurora;

/**
 * 达穆候战斗临时状态：破绽窗口、连击增幅、本回合节奏。
 */
public class DamuhouCombatState {
    public static boolean trackingAttackCard = false;
    public static int attacksPlayedThisTurn;
    public static int skillsPlayedThisTurn;
    public static int cardsPlayedThisTurn;
    public static boolean playerDamagedThisTurn;
    public static int thisTurnNextCombo;
    public static int nextAttackComboPenalty;
    public static int nextHalfAutoDamage;
    public static int nextAttackHealOnKill;
    public static int nextCardAttackCombo;
    public static int nextSkillCostRefund;
    public static int firstCardCostRefund;
    public static int nthCardCostRefund;
    public static int nthCardIndex;
    public static int firstAttackCombo;
    public static int thisTurnFirstAttackCombo;
    public static int firstAttackKillEnergy;
    public static AbstractMonster focusTarget;
    public static int focusBaseCombo;
    public static boolean ammoRationActive;
    public static boolean holdNextCardAttackCombo;
    public static boolean holdNextSkillRefund;
    public static AbstractMonster lockTargetThisCard;

    public static void onBattleStart() {
        trackingAttackCard = false;
        thisTurnNextCombo = 0;
        nextAttackComboPenalty = 0;
        nextHalfAutoDamage = 0;
        nextAttackHealOnKill = 0;
        nextCardAttackCombo = 0;
        nextSkillCostRefund = 0;
        firstCardCostRefund = 0;
        nthCardCostRefund = 0;
        nthCardIndex = 0;
        firstAttackCombo = 0;
        thisTurnFirstAttackCombo = 0;
        firstAttackKillEnergy = 0;
        focusTarget = null;
        focusBaseCombo = 0;
        ammoRationActive = false;
        holdNextCardAttackCombo = false;
        holdNextSkillRefund = false;
        lockTargetThisCard = null;
        onPlayerTurnStart();
    }

    public static void onPlayerTurnStart() {
        attacksPlayedThisTurn = 0;
        skillsPlayedThisTurn = 0;
        cardsPlayedThisTurn = 0;
        playerDamagedThisTurn = false;
        thisTurnNextCombo = 0;
        nextAttackComboPenalty = 0;
        nextHalfAutoDamage = 0;
        nextAttackHealOnKill = 0;
        nextCardAttackCombo = 0;
        nextSkillCostRefund = 0;
        firstCardCostRefund = 0;
        nthCardCostRefund = 0;
        nthCardIndex = 0;
        firstAttackCombo = 0;
        thisTurnFirstAttackCombo = 0;
        firstAttackKillEnergy = 0;
        holdNextCardAttackCombo = false;
        holdNextSkillRefund = false;
        lockTargetThisCard = null;
        focusTarget = null;
        focusBaseCombo = 0;
    }

    public static void armNextCardAttackCombo(int n) {
        nextCardAttackCombo += n;
        holdNextCardAttackCombo = true;
    }

    public static void armNextSkillRefund(int n) {
        nextSkillCostRefund += n;
        holdNextSkillRefund = true;
    }

    public static void setNthCardDiscount(int index) {
        if (nthCardIndex == 0 || index < nthCardIndex) {
            nthCardIndex = index;
        }
        nthCardCostRefund = 1;
    }

    public static void addThisTurnNextCombo(int n) {
        thisTurnNextCombo += n;
    }

    public static int consumeComboBonus() {
        int n = thisTurnNextCombo + nextCardAttackCombo;
        thisTurnNextCombo = 0;
        nextCardAttackCombo = 0;
        n -= nextAttackComboPenalty;
        nextAttackComboPenalty = 0;
        return n;
    }

    public static int consumeHalfAutoDamage() {
        if (DamuhouFireMode.isFullAuto()) {
            return 0;
        }
        int n = nextHalfAutoDamage;
        nextHalfAutoDamage = 0;
        return n;
    }

    public static int focusExtra(AbstractMonster t) {
        if (t == null || focusTarget == null || t != focusTarget) {
            return 0;
        }
        int extra = focusBaseCombo;
        if (t.hasPower(BreachPower.POWER_ID)) {
            extra += t.getPower(BreachPower.POWER_ID).amount / 3;
        }
        return extra;
    }

    public static void beginAttackCard() {
        trackingAttackCard = true;
        lockTargetThisCard = null;
    }

    public static void recordAttackHit(AbstractMonster m) {
        if (!trackingAttackCard || m == null || m.isDeadOrEscaped()) {
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || !p.hasRelic(VioletAurora.ID)) {
            return;
        }
        int extra = 0;
        if (p.hasPower(yourmod.powers.DamuhouVioletGlowPower.POWER_ID)) {
            extra = p.getPower(yourmod.powers.DamuhouVioletGlowPower.POWER_ID).amount;
        }
        int give = VioletAurora.BREACH_PER_HIT + extra;
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(m, p, new BreachPower(m, give), give));
    }

    public static void endAttackCard() {
        trackingAttackCard = false;
    }
}
