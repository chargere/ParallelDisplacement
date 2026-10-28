package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static yourmod.ModFile.makeID;

/** 我很神秘 */
public class DamuhouMysteriousPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouMysterious");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final int THRESHOLD = 13;
    private int energySpent;

    public DamuhouMysteriousPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("intangible");
        isTwoAmount = true;
        amount2 = 0;
        energySpent = 0;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        int spent = 0;
        if (card != null && !card.freeToPlay()) {
            spent = Math.max(0, card.energyOnUse);
        }
        if (spent <= 0) {
            return;
        }
        energySpent += spent;
        while (energySpent >= THRESHOLD) {
            energySpent -= THRESHOLD;
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, amount), amount));
        }
        amount2 = energySpent;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + THRESHOLD + strings.DESCRIPTIONS[1]
                + amount + strings.DESCRIPTIONS[2] + energySpent + strings.DESCRIPTIONS[3];
    }
}
