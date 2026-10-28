package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 燎原战纹：每回合首次攻击施加 2 层烬火 */
public class FirstAttackEmberPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FirstAttackEmber");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean usedThisTurn = false;

    public FirstAttackEmberPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!usedThisTurn && card.type == AbstractCard.CardType.ATTACK && action.target instanceof AbstractMonster) {
            usedThisTurn = true;
            flash();
            AbstractMonster m = (AbstractMonster) action.target;
            addToBot(new ApplyPowerAction(m, owner, new EmberFirePower(m, 2), 2));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
