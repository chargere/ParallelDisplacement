package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 宸茶涓嶅洖 */
public class NoSkillsPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NoSkills");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NoSkillsPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, 1);
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card != null && card.type == AbstractCard.CardType.SKILL) {
            card.cantUseMessage = strings.DESCRIPTIONS[1];
            return false;
        }
        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}