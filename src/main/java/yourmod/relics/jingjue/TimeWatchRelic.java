package yourmod.relics.jingjue;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 右键消耗 5 沙漏，生命回到本回合开始时并获得 1 能量。 */
public class TimeWatchRelic extends AbstractEasyRelic implements ClickableRelic {
    public static final String ID = makeID("JjTimeWatch");
    private int recordedHp;

    public TimeWatchRelic() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player != null) {
            recordedHp = AbstractDungeon.player.currentHealth;
            grayscale = false;
        }
    }

    @Override
    public void onRightClick() {
        if (grayscale || JingjueCombatState.hourglass() < 5) {
            return;
        }
        if (!JingjueCombatState.consumeHourglass(5)) {
            return;
        }
        flash();
        grayscale = true;
        if (AbstractDungeon.player.currentHealth < recordedHp) {
            AbstractDungeon.player.heal(recordedHp - AbstractDungeon.player.currentHealth);
        }
        AbstractDungeon.player.gainEnergy(1);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TimeWatchRelic();
    }
}
