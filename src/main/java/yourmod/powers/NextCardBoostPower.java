package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 浠婃棩鍋滄洿锛歛mount==1 涓嬩竴寮狅紝amount==2 鏈洖鍚?*/
public class NextCardBoostPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NextCardBoost");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NextCardBoostPower(AbstractCreature owner, int mode) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, mode);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.5f;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.5f;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount == 1) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && amount >= 2) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = amount >= 2 ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}