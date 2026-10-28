package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

/**
 * 焚天·永燃：燎原计数为 4/5/6 时，无视敌人烬火层数，无条件获得燎原加伤与计数推进，且不消耗烬火。
 */
public class EternalBurnPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EternalBurn");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EternalBurnPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        this.canGoNegative = true;
    }

    /** 是否处于永燃自由触发（计数 4/5/6 且持有本能力） */
    public static boolean isFreeProc(int scorchingCounter) {
        if (scorchingCounter < 4 || scorchingCounter > 6) {
            return false;
        }
        return hasEternalBurn();
    }

    public static boolean hasEternalBurn() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof EternalBurnPower) {
                return true;
            }
        }
        return AbstractDungeon.player.hasPower(POWER_ID);
    }

    /** @deprecated 使用 {@link #isFreeProc(int)} */
    public static boolean shouldSkipEmberConsume(int scorchingCounter) {
        return isFreeProc(scorchingCounter);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
