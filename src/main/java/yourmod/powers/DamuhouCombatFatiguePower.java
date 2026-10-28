package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouCombatFatiguePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouCombatFatigue");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int healedThisCombat;

    public DamuhouCombatFatiguePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("regen");
        healedThisCombat = 0;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK || DamuhouCombatState.attacksPlayedThisTurn % 3 != 0) {
            return;
        }
        int cap = amount * 3;
        int heal = Math.min(amount, cap - healedThisCombat);
        if (heal <= 0) {
            return;
        }
        flash();
        healedThisCombat += heal;
        addToBot(new HealAction(owner, owner, heal));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1] + (amount * 3) + strings.DESCRIPTIONS[2];
    }
}
