package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.cards.dominic.FrostPayType;

import static yourmod.ModFile.makeID;

/** 星锻未完成稿：带星灭霜偿的攻击 +amount 伤害 */
public class UnfinishedStarforgePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("UnfinishedStarforge");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public UnfinishedStarforgePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        AbstractCard c = null;
        if (AbstractDungeon.player != null) {
            c = AbstractDungeon.player.cardInUse;
            if (c == null) {
                c = AbstractDungeon.player.hoveredCard;
            }
        }
        if (c instanceof AbstractDominicCard
                && c.type == AbstractCard.CardType.ATTACK
                && ((AbstractDominicCard) c).frostPayType == FrostPayType.STAR_EXTINGUISH) {
            return damage + amount;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
