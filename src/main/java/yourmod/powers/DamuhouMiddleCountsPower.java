package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.relics.PrismOpticSystem;

import static yourmod.ModFile.makeID;

public class DamuhouMiddleCountsPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouMiddleCounts");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouMiddleCountsPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        useVanillaIcon("accuracy");
    }

    @Override
    public void onInitialApplication() {
        PrismOpticSystem relic = PrismOpticSystem.get();
        if (relic != null) {
            relic.refreshMarksNow();
        }
    }

    @Override
    public void atStartOfTurn() {
        PrismOpticSystem relic = PrismOpticSystem.get();
        if (relic != null) {
            relic.refreshMarksNow();
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
