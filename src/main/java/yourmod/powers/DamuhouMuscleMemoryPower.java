package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;
import yourmod.util.DamuhouFireMode;

import static yourmod.ModFile.makeID;

public class DamuhouMuscleMemoryPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouMuscleMemory");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouMuscleMemoryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("accuracy");
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK || DamuhouCombatState.attacksPlayedThisTurn != 1) {
            return;
        }
        flash();
        if (DamuhouFireMode.isFullAuto()) {
            addToBot(new DrawCardAction(owner, amount));
        } else {
            addToBot(new GainBlockAction(owner, owner, amount * 2));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + (amount * 2) + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
    }
}
