package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 霜噬印记：主动耗霜时挂星痕；amount=每回合最多触发次数 */
public class FrostBiteMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FrostBiteMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FrostBiteMarkPower(AbstractCreature owner, int maxTriggers) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxTriggers);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
