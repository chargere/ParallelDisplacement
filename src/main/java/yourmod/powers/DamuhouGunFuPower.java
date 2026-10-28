package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.damuhou.AbstractDamuhouCard;

import static yourmod.ModFile.makeID;

public class DamuhouGunFuPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouGunFu");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouGunFuPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("dexterity");
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        int combo = card instanceof AbstractDamuhouCard ? ((AbstractDamuhouCard) card).effectivePrintedCombo() : 1;
        if (card.type == AbstractCard.CardType.ATTACK && combo >= 3) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
