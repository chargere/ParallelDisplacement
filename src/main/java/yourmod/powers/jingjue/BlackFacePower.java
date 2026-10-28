package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class BlackFacePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjBlackFace");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BlackFacePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("artifact");
    }

    @Override
    public void onInitialApplication() {
        if (owner.hasPower(WeakPower.POWER_ID)) {
            addToBot(new ReducePowerAction(owner, owner, WeakPower.POWER_ID, 99));
        }
        if (owner.hasPower(VulnerablePower.POWER_ID)) {
            addToBot(new ReducePowerAction(owner, owner, VulnerablePower.POWER_ID, 99));
        }
    }

    @Override
    public void atStartOfTurn() {
        flash();
        JingjueCombatState.gainHourglass(amount);
        addToBot(new LoseHPAction(owner, owner, 2));
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
