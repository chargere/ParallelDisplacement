package yourmod.unlock;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlockBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import yourmod.CharacterFile;
import yourmod.cards.common.RitualOffering;
import yourmod.cards.common.ThunderForgeShard;
import yourmod.cards.rare.ArmyEdict;
import yourmod.cards.rare.FormerName;
import yourmod.cards.rare.HeavenFireUndying;
import yourmod.cards.rare.SilentVigil;
import yourmod.cards.uncommon.EmberThunderDuet;
import yourmod.cards.uncommon.LaoZhengFled;
import yourmod.cards.uncommon.UnfinishedBlueprint;
import yourmod.relics.ConductEcho;
import yourmod.relics.EternalStormAnchor;
import yourmod.relics.FurnaceBreath;
import yourmod.relics.PlagueEmberAsh;
import yourmod.relics.RainyPromise;
import yourmod.relics.UnsentFlower;

/**
 * 待解锁内容（仅新卡/新遗物；旧内容开局可用）。
 * <p>
 * 顺序：卡 → 遗物 → 卡 → 遗物 → 卡（档位 0～4）。
 * 进度走原版角色解锁条（通关得分）。
 * <p>
 * 注意：AutoAdd.setDefaultSeen(true) 会对每张卡调用 {@link UnlockTracker#unlockCard}，
 * 必须在注册卡牌后调用 {@link #enforceLocks()} 纠正。
 */
public final class HongwenUnlocks {
    private HongwenUnlocks() {
    }

    /** 档位 → 卡牌 ID（仅卡牌档） */
    private static final String[][] CARD_BUNDLES = {
            {RitualOffering.ID, ThunderForgeShard.ID, EmberThunderDuet.ID},       // 0
            null,                                                                   // 1 遗物
            {UnfinishedBlueprint.ID, LaoZhengFled.ID, ArmyEdict.ID},              // 2
            null,                                                                   // 3 遗物
            {SilentVigil.ID, FormerName.ID, HeavenFireUndying.ID}                 // 4
    };

    /** 档位 → 遗物 ID（仅遗物档） */
    private static final String[][] RELIC_BUNDLES = {
            null,                                                                   // 0 卡
            {FurnaceBreath.ID, ConductEcho.ID, UnsentFlower.ID},                  // 1
            null,                                                                   // 2 卡
            {PlagueEmberAsh.ID, RainyPromise.ID, EternalStormAnchor.ID},          // 3
            null                                                                    // 4 卡
    };

    public static void register() {
        AbstractPlayer.PlayerClass cls = CharacterFile.Enums.THE_TODO;

        addCardBundle(cls, 0, CARD_BUNDLES[0]);
        addRelicBundle(cls, 1, RELIC_BUNDLES[1]);
        addCardBundle(cls, 2, CARD_BUNDLES[2]);
        addRelicBundle(cls, 3, RELIC_BUNDLES[3]);
        addCardBundle(cls, 4, CARD_BUNDLES[4]);

        // 若 SetUnlocks 晚于 EditCards，这里再纠正一次存档里被 unlockCard 洗掉的锁
        enforceLocks();
    }

    /**
     * 按角色当前解锁档位强制锁定/解锁五组内容。
     * 应在 AutoAdd 注册卡牌之后调用（避免 setDefaultSeen→unlockCard 把锁洗掉）。
     */
    public static void enforceLocks() {
        AbstractPlayer.PlayerClass cls = CharacterFile.Enums.THE_TODO;
        int unlockedLevel = UnlockTracker.getUnlockLevel(cls);

        for (int level = 0; level < CARD_BUNDLES.length; level++) {
            boolean shouldBeUnlocked = level < unlockedLevel;
            if (CARD_BUNDLES[level] != null) {
                for (String id : CARD_BUNDLES[level]) {
                    if (shouldBeUnlocked) {
                        unlockCardFully(id);
                    } else {
                        lockCard(id);
                    }
                }
            }
            if (RELIC_BUNDLES[level] != null) {
                for (String id : RELIC_BUNDLES[level]) {
                    if (shouldBeUnlocked) {
                        unlockRelic(id);
                    } else {
                        lockRelic(id);
                    }
                }
            }
        }

        // 非锁定内容：仅标记已遇见，绝不调用 unlockCard
        String prefix = yourmod.ModFile.modID + ":";
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.cardID != null && c.cardID.startsWith(prefix) && !isUnlockGated(c.cardID)) {
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }
        // 锁定卡也标记遇见，图鉴显示「锁定」而非「未知」
        for (int level = 0; level < CARD_BUNDLES.length; level++) {
            if (CARD_BUNDLES[level] == null) {
                continue;
            }
            for (String id : CARD_BUNDLES[level]) {
                UnlockTracker.markCardAsSeen(id);
            }
        }
        for (int level = 0; level < RELIC_BUNDLES.length; level++) {
            if (RELIC_BUNDLES[level] == null) {
                continue;
            }
            for (String id : RELIC_BUNDLES[level]) {
                UnlockTracker.markRelicAsSeen(id);
            }
        }
    }

    private static boolean isUnlockGated(String id) {
        for (String[] bundle : CARD_BUNDLES) {
            if (bundle == null) {
                continue;
            }
            for (String s : bundle) {
                if (s.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void lockCard(String id) {
        UnlockTracker.unlockPref.putInteger(id, 0);
        UnlockTracker.unlockPref.flush();
        if (!UnlockTracker.lockedCards.contains(id)) {
            UnlockTracker.lockedCards.add(id);
        }
    }

    /** 解锁卡时同样删光可能存在的重复锁定条目 */
    private static void unlockCardFully(String id) {
        UnlockTracker.unlockCard(id);
        while (UnlockTracker.lockedCards.remove(id)) {
            // keep removing duplicates
        }
    }

    private static void lockRelic(String id) {
        UnlockTracker.unlockPref.putInteger(id, 0);
        UnlockTracker.unlockPref.flush();
        if (!UnlockTracker.lockedRelics.contains(id)) {
            UnlockTracker.lockedRelics.add(id);
        }
    }

    private static void unlockRelic(String id) {
        UnlockTracker.unlockPref.putInteger(id, 2);
        UnlockTracker.unlockPref.flush();
        // isRelicLocked 只查 lockedRelics；addRelic 可重复插入，必须删光
        while (UnlockTracker.lockedRelics.remove(id)) {
            // keep removing duplicates
        }
        UnlockTracker.markRelicAsSeen(id);
    }

    private static void addCardBundle(AbstractPlayer.PlayerClass cls, int level, String[] ids) {
        // CustomUnlockBundle 构造时已 UnlockTracker.addCard，勿再加一次（会重复进 lockedCards）
        BaseMod.addUnlockBundle(new CustomUnlockBundle(ids[0], ids[1], ids[2]), cls, level);
    }

    private static void addRelicBundle(AbstractPlayer.PlayerClass cls, int level, String[] ids) {
        // CustomUnlockBundle 构造时已 UnlockTracker.addRelic，勿再加一次（会重复进 lockedRelics）
        BaseMod.addUnlockBundle(
                new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC, ids[0], ids[1], ids[2]),
                cls, level);
    }
}
