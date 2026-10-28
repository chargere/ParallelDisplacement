package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 璺宠繃涓嬪洖鍚?*/
public class SkipNextTurnPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SkipNextTurn");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SkipNextTurnPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, 1);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new PressEndTurnButtonAction());
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}