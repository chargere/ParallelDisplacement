package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class ReverseWalkerPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjReverseWalker");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ReverseWalkerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("brutality");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        JingjueCombatState.gainHourglass(amount);
        addToBot(new LoseHPAction(owner, owner, 1));
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
