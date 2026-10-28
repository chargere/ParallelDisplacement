package yourmod.unlock;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlockBundle;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import yourmod.DominicCharacter;
import yourmod.cards.dominic.AbandonedQuill;
import yourmod.cards.dominic.ClearAPath;
import yourmod.cards.dominic.FinalSheatheMark;
import yourmod.cards.dominic.FrostPath;
import yourmod.cards.dominic.HowDoYouKnow;
import yourmod.cards.dominic.LastLineOfChart;
import yourmod.cards.dominic.SharedWarmth;
import yourmod.cards.dominic.StarAshScavenge;
import yourmod.cards.dominic.StillSheathed;
import yourmod.relics.ClosedStarChart;
import yourmod.relics.HatchFrost;
import yourmod.relics.ObservationHalt;
import yourmod.relics.SwordTasselAlong;
import yourmod.relics.ThreeDayHatch;
import yourmod.relics.UnfinishedStarforge;

/**
 * 霜星旅者待解锁内容（五档：卡→遗物→卡→遗物→卡）。
 */
public final class DominicUnlocks {
    private DominicUnlocks() {
    }

    private static final String[][] CARD_BUNDLES = {
            {StarAshScavenge.ID, FrostPath.ID, SharedWarmth.ID},
            null,
            {AbandonedQuill.ID, StillSheathed.ID, ClearAPath.ID},
            null,
            {HowDoYouKnow.ID, FinalSheatheMark.ID, LastLineOfChart.ID}
    };

    private static final String[][] RELIC_BUNDLES = {
            null,
            {ClosedStarChart.ID, HatchFrost.ID, SwordTasselAlong.ID},
            null,
            {ThreeDayHatch.ID, UnfinishedStarforge.ID, ObservationHalt.ID},
            null
    };

    public static void register() {
        AbstractPlayer.PlayerClass cls = DominicCharacter.Enums.THE_DOMINIC;

        addCardBundle(cls, 0, CARD_BUNDLES[0]);
        addRelicBundle(cls, 1, RELIC_BUNDLES[1]);
        addCardBundle(cls, 2, CARD_BUNDLES[2]);
        addRelicBundle(cls, 3, RELIC_BUNDLES[3]);
        addCardBundle(cls, 4, CARD_BUNDLES[4]);

        enforceLocks();
    }

    public static void enforceLocks() {
        AbstractPlayer.PlayerClass cls = DominicCharacter.Enums.THE_DOMINIC;
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

        String prefix = yourmod.ModFile.modID + ":";
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.cardID != null && c.cardID.startsWith(prefix) && !isUnlockGated(c.cardID)) {
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }
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
        while (UnlockTracker.lockedRelics.remove(id)) {
            // keep removing duplicates
        }
        UnlockTracker.markRelicAsSeen(id);
    }

    private static void addCardBundle(AbstractPlayer.PlayerClass cls, int level, String[] ids) {
        BaseMod.addUnlockBundle(new CustomUnlockBundle(ids[0], ids[1], ids[2]), cls, level);
    }

    private static void addRelicBundle(AbstractPlayer.PlayerClass cls, int level, String[] ids) {
        BaseMod.addUnlockBundle(
                new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC, ids[0], ids[1], ids[2]),
                cls, level);
    }
}
