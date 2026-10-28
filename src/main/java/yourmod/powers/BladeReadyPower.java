package yourmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.cards.dominic.FrostPayType;

import static yourmod.ModFile.makeID;

/** 开刃预备：每回合第一张带星灭霜偿的攻击伤害 +amount */
public class BladeReadyPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("BladeReady");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean usedThisTurn;

    public BladeReadyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        usedThisTurn = false;
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (usedThisTurn || type != DamageInfo.DamageType.NORMAL) {
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
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!usedThisTurn
                && card instanceof AbstractDominicCard
                && card.type == AbstractCard.CardType.ATTACK
                && ((AbstractDominicCard) card).frostPayType == FrostPayType.STAR_EXTINGUISH) {
            usedThisTurn = true;
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
