package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 雷网效应：静电传导改为打回同一目标（自己电自己），约等于对带印记目标双倍伤害。
 * 作为源目标不消耗印记；作为被传导的「自己」仍消耗 1 层。不再扩散到其他敌人。
 */
public class ThunderNetEffectPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ThunderNetEffect");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ThunderNetEffectPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
