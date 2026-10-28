package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 静默：下一次攻击额外造成 层数×倍率 伤害，然后移除。 */
public class SilencePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Silence");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int mult;

    public SilencePower(AbstractCreature owner, int amount, int mult) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        this.mult = mult;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type == DamageInfo.DamageType.NORMAL && card != null && card.type == AbstractCard.CardType.ATTACK) {
            return damage + amount * mult;
        }
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card != null && card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + (amount * mult) + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2] + mult + strings.DESCRIPTIONS[3];
    }
}
