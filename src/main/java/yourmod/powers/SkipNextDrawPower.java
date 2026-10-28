package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 璺宠繃涓嬫鎶界墝锛堢敱 patch 娑堣垂锛?*/
public class SkipNextDrawPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SkipNextDraw");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SkipNextDrawPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}