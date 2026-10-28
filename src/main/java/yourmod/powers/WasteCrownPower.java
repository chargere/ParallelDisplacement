package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 搴熺墿鐜嬪啝 */
public class WasteCrownPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("WasteCrown");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public WasteCrownPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && UselessPower.getAmount(owner) >= 5) {
            flash();
            addToBot(new DrawCardAction(1));
            addToBot(new GainBlockAction(owner, owner, 5));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}