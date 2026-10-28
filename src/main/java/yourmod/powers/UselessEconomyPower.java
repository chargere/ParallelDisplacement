package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 搴熺墿缁忔祹锛歛mount==1 浠呮墦鍑伙紝amount==2 鍏ㄩ儴鏀诲嚮 */
public class UselessEconomyPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("UselessEconomy");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public UselessEconomyPower(AbstractCreature owner, int mode) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, mode);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL || card == null) {
            return damage;
        }
        boolean ok = amount >= 2
                ? card.type == AbstractCard.CardType.ATTACK
                : card.hasTag(AbstractCard.CardTags.STRIKE);
        if (ok) {
            return damage + UselessPower.getEffective(owner);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = amount >= 2 ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}