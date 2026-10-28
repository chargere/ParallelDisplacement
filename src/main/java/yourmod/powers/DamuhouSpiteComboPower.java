package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 自信颗秒留下的连击：恼羞成怒 */
public class DamuhouSpiteComboPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouSpiteCombo");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouSpiteComboPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("anger");
    }

    @Override
    public void onAfterUseCard(com.megacrit.cardcrawl.cards.AbstractCard card,
                               com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type == com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
