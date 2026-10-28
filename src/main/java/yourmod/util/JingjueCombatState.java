package yourmod.util;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.powers.jingjue.HourglassPower;
import yourmod.powers.jingjue.TimeDilationPower;
import yourmod.powers.jingjue.ErosionPower;
import yourmod.relics.jingjue.CrystalEyeRelic;
import yourmod.relics.jingjue.HourglassShardRelic;
import yourmod.relics.jingjue.PhoenixGallbladderRelic;
import yourmod.relics.jingjue.SnakeTokenRelic;

import java.util.ArrayList;
import java.util.HashMap;

/** 精绝王者战斗状态：沙漏、处决线、溅射、时滞软上限。默认平衡档。 */
public class JingjueCombatState {
    public static final int HOURGLASS_CAP = 9;
    public static final int DILATION_CAP = 9;
    public static final int DILATION_SOFT_CAP = 3;
    public static final int IMAGINARY_CAP = 5;
    public static final int IMAGINARY_ELITE_CAP = 3;
    public static final float IMAGINARY_INCOMING = 0.25f;
    public static final float IMAGINARY_OUTGOING = 0.25f;
    public static final float IMAGINARY_OUTGOING_CAP = 0.75f;
    public static final float EXECUTE_BASE = 0.05f;
    public static final float EXECUTE_PER_EYE = 0.05f;
    public static final float EXECUTE_CAP = 0.65f;
    public static final float ELITE_EXECUTE = 0.08f;
    public static final float BOSS_EXECUTE = 0.04f;

    public static int hourglassCapBonus;
    public static int dilationCapBonus;
    public static int executeLineBonusPct;
    public static int executeLineBonusThisTurn;
    public static int splashBonusPct;
    public static int splashThisTurnPct;
    public static boolean eyeMarkNoDecay;
    public static int eyeMarkPctOverride;
    public static boolean splashTriggersExecute;
    public static boolean firstHourglassDoubled;
    public static int energySavedThisTurn;
    public static boolean gainedHourglassThisTurn;
    public static boolean executedThisTurn;
    public static int damageTakenThisTurn;
    public static int nextTurnHeal;
    public static int nextTurnDrawPenalty;
    public static int keepEnergy;
    public static int keepBlock;
    public static boolean keepResources;
    public static boolean firstAttackRecorded;
    public static String firstAttackId;
    public static int firstAttackDamage;
    public static final ArrayList<String> attacksThisTurn = new ArrayList<>();
    public static final ArrayList<Integer> attackDamagesThisTurn = new ArrayList<>();
    public static final ArrayList<String> attacksLastTurn = new ArrayList<>();
    public static final ArrayList<Integer> attackDamagesLastTurn = new ArrayList<>();
    public static int corridorTriggers;
    public static int snakeBoneTriggers;
    public static int nextCardsDiscount;
    public static boolean firstTurnDrawPenalty;
    public static int shardTurnCounter;
    public static boolean swordResonance;
    public static boolean spearResonance;
    public static final HashMap<AbstractMonster, Integer> capturedIntents = new HashMap<>();
    public static final HashMap<AbstractMonster, Byte> storedMoves = new HashMap<>();

    public static void onBattleStart() {
        hourglassCapBonus = 0;
        dilationCapBonus = 0;
        executeLineBonusPct = 0;
        executeLineBonusThisTurn = 0;
        splashBonusPct = 0;
        splashThisTurnPct = 0;
        eyeMarkNoDecay = false;
        eyeMarkPctOverride = 0;
        splashTriggersExecute = false;
        firstHourglassDoubled = false;
        firstTurnDrawPenalty = false;
        shardTurnCounter = 0;
        swordResonance = false;
        spearResonance = false;
        nextTurnHeal = 0;
        nextTurnDrawPenalty = 0;
        keepEnergy = 0;
        keepBlock = 0;
        keepResources = false;
        attacksLastTurn.clear();
        attackDamagesLastTurn.clear();
        capturedIntents.clear();
        storedMoves.clear();
        gainedHourglassThisTurn = false;
        onPlayerTurnStart();
    }

    public static void onPlayerTurnStart() {
        energySavedThisTurn = 0;
        firstHourglassDoubled = false;
        executedThisTurn = false;
        damageTakenThisTurn = 0;
        splashThisTurnPct = 0;
        executeLineBonusThisTurn = 0;
        corridorTriggers = 0;
        snakeBoneTriggers = 0;
        nextCardsDiscount = 0;
        firstAttackRecorded = false;
        firstAttackId = null;
        firstAttackDamage = 0;
        attacksLastTurn.clear();
        attackDamagesLastTurn.clear();
        attacksLastTurn.addAll(attacksThisTurn);
        attackDamagesLastTurn.addAll(attackDamagesThisTurn);
        attacksThisTurn.clear();
        attackDamagesThisTurn.clear();
        shardTurnCounter++;
    }

    public static boolean isJingjue() {
        return AbstractDungeon.player instanceof yourmod.JingjueCharacter;
    }

    public static int hourglassCap() {
        return HOURGLASS_CAP + hourglassCapBonus;
    }

    public static int dilationCap() {
        return DILATION_CAP + dilationCapBonus;
    }

    public static int hourglass() {
        if (AbstractDungeon.player == null) {
            return 0;
        }
        AbstractPower p = AbstractDungeon.player.getPower(HourglassPower.POWER_ID);
        return p == null ? 0 : p.amount;
    }

    public static void gainHourglass(int n) {
        if (AbstractDungeon.player == null || n <= 0) {
            return;
        }
        if (!firstHourglassDoubled && AbstractDungeon.player.hasPower(yourmod.powers.jingjue.SkirmishStonePower.POWER_ID)) {
            n += 1;
            firstHourglassDoubled = true;
        }
        int have = hourglass();
        int add = Math.min(n, hourglassCap() - have);
        if (add <= 0) {
            return;
        }
        gainedHourglassThisTurn = true;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new HourglassPower(AbstractDungeon.player, add), add));
    }

    public static boolean consumeHourglass(int n) {
        if (n <= 0) {
            return true;
        }
        if (hourglass() < n) {
            return false;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SnakeTokenRelic.ID)
                && AbstractDungeon.cardRandomRng.randomBoolean(0.3f)) {
            AbstractDungeon.player.getRelic(SnakeTokenRelic.ID).flash();
            return true;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, HourglassPower.POWER_ID, n));
        return true;
    }

    public static void gainDilation(int n) {
        if (AbstractDungeon.player == null || n <= 0) {
            return;
        }
        int have = 0;
        AbstractPower p = AbstractDungeon.player.getPower(TimeDilationPower.POWER_ID);
        if (p != null) {
            have = p.amount;
        }
        int add = Math.min(n, dilationCap() - have);
        if (add <= 0) {
            return;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new TimeDilationPower(AbstractDungeon.player, add), add));
    }

    /** 处决线比例。传入 null / 非怪物时不含眼印，即当前基础侵蚀度。 */
    public static float executeLineRatio(AbstractMonster t) {
        float perEye = eyeMarkPctOverride > 0 ? eyeMarkPctOverride / 100f : EXECUTE_PER_EYE;
        int eyes = 0;
        if (t != null && t.hasPower(yourmod.powers.jingjue.EyeMarkPower.POWER_ID)) {
            eyes = t.getPower(yourmod.powers.jingjue.EyeMarkPower.POWER_ID).amount;
        }
        float pct = EXECUTE_BASE + perEye * eyes;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(PhoenixGallbladderRelic.ID)) {
            pct += PhoenixGallbladderRelic.EXECUTE_BONUS;
        }
        pct += executeLineBonusPct / 100f;
        pct += executeLineBonusThisTurn / 100f;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(yourmod.relics.jingjue.GlacierCorpseRelic.ID)) {
            pct *= 2f;
        }
        if (pct > EXECUTE_CAP) {
            pct = EXECUTE_CAP;
        }
        return pct;
    }

    public static int executeLinePct(AbstractMonster t) {
        return Math.max(1, Math.round(executeLineRatio(t) * 100f));
    }

    public static int executeLine(AbstractMonster t) {
        if (t == null) {
            return 1;
        }
        return Math.max(1, (int) (t.maxHealth * executeLineRatio(t)));
    }

    public static void syncErosionPowers() {
        if (AbstractDungeon.player == null) {
            return;
        }
        applyErosion(AbstractDungeon.player, executeLinePct(null));
        Wiz.forAllMonstersLiving(m -> applyErosion(m, executeLinePct(m)));
    }

    private static void applyErosion(com.megacrit.cardcrawl.core.AbstractCreature c, int pct) {
        if (c == null || c.isDead || c.halfDead) {
            return;
        }
        pct = Math.max(1, pct);
        if (c.hasPower(ErosionPower.POWER_ID)) {
            ((ErosionPower) c.getPower(ErosionPower.POWER_ID)).setPct(pct);
        } else {
            c.addPower(new ErosionPower(c, pct));
        }
    }

    public static int splashPercent(int printed) {
        int n = printed + splashBonusPct + splashThisTurnPct;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(yourmod.relics.jingjue.DapuAmberRelic.ID)) {
            n += yourmod.relics.jingjue.DapuAmberRelic.BONUS;
        }
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(yourmod.powers.jingjue.DapuFormPower.POWER_ID)
                && yourmod.powers.jingjue.DapuFormPower.isFire()) {
            n = n + n / 2;
        }
        return Math.max(0, n);
    }

    public static boolean isEliteOrBoss(AbstractMonster m) {
        return m != null && (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS);
    }

    /** 精英/首领意图绝不进时空乱流。首领房整场也不存，避免类型没标 BOSS 时把大额意图存进去。 */
    public static boolean blocksTimeTurbulence(AbstractMonster m) {
        if (m == null || isEliteOrBoss(m)) {
            return true;
        }
        return AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss;
    }

    public static void captureVisibleIntents() {
        capturedIntents.clear();
        if (AbstractDungeon.getMonsters() == null) {
            return;
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && !m.isDeadOrEscaped()) {
                capturedIntents.put(m, readIntentTotal(m));
            }
        }
    }

    public static boolean isAttackIntent(AbstractMonster m) {
        if (m == null || m.intent == null) {
            return false;
        }
        switch (m.intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return true;
            default:
                return false;
        }
    }

    public static void storeSkippedMove(AbstractMonster m) {
        if (m == null || isAttackIntent(m)) {
            return;
        }
        storedMoves.put(m, m.nextMove);
    }

    public static void replayStoredMove(AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped() || !storedMoves.containsKey(m)) {
            return;
        }
        byte saved = storedMoves.remove(m);
        byte current = m.nextMove;
        m.nextMove = saved;
        m.takeTurn();
        m.nextMove = current;
    }

    public static int takeCapturedIntent(AbstractMonster m) {
        if (capturedIntents.containsKey(m)) {
            Integer captured = capturedIntents.remove(m);
            return captured == null ? 0 : Math.max(0, captured);
        }
        return readIntentTotal(m);
    }

    public static int readIntentTotal(AbstractMonster monster) {
        if (monster == null) {
            return 0;
        }
        int perHit = monster.getIntentDmg();
        if (perHit <= 0) {
            perHit = monster.getIntentBaseDmg();
        }
        if (perHit <= 0) {
            Integer raw = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentDmg");
            if (raw != null) {
                perHit = raw;
            }
        }
        if (perHit <= 0) {
            Integer base = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentBaseDmg");
            if (base != null) {
                perHit = base;
            }
        }
        Boolean isMulti = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "isMultiDmg");
        Integer multi = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentMultiAmt");
        int hits = (isMulti != null && isMulti && multi != null && multi > 1) ? multi : 1;
        return Math.max(0, perHit) * hits;
    }

    public static int imaginaryCap(AbstractMonster m) {
        return isEliteOrBoss(m) ? IMAGINARY_ELITE_CAP : IMAGINARY_CAP;
    }

    public static void recordAttack(AbstractCard card, int dmg) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        if (!firstAttackRecorded) {
            firstAttackRecorded = true;
            firstAttackId = card.cardID;
            firstAttackDamage = dmg;
        }
        attacksThisTurn.add(card.cardID);
        attackDamagesThisTurn.add(dmg);
    }
}
