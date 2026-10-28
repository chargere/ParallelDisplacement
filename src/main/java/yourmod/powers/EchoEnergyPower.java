package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 烬雷回响：打出攻击牌获得 1 能量，最多 amount 次 */
public class EchoEnergyPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EchoEnergy");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EchoEnergyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount > 0 && card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot(new GainEnergyAction(1));
            amount--;
            updateDescription();
            if (amount <= 0) {
                addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this.ID));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
