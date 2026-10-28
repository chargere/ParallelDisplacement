package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/**
 * 顺路一剑：每回合第一张攻击 +amount；若该攻击带霜偿且承受代价再 +amount。
 * 预览只显示基础 +amount；承受追加在实际造成伤害时（霜偿已结算）补上。
 */
public class SwordAlongTheWayPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SwordAlongTheWay");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private AbstractCard buffedCard;

    public SwordAlongTheWayPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        buffedCard = null;
        DominicCombatState.firstAttackPlayedThisTurn = false;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL || AbstractDungeon.player == null) {
            return damage;
        }
        AbstractCard inUse = AbstractDungeon.player.cardInUse;
        if (inUse == null) {
            AbstractCard hovered = AbstractDungeon.player.hoveredCard;
            if (hovered != null && hovered.type == AbstractCard.CardType.ATTACK && buffedCard == null) {
                return damage + amount;
            }
            return damage;
        }
        if (buffedCard != null && buffedCard != inUse) {
            return damage;
        }
        if (inUse.type != AbstractCard.CardType.ATTACK) {
            return damage;
        }
        buffedCard = inUse;
        DominicCombatState.firstAttackPlayedThisTurn = true;
        if (inUse instanceof AbstractDominicCard
                && ((AbstractDominicCard) inUse).frostPayCost > 0
                && !DominicCombatState.lastFrostPayCancelled) {
            return damage + amount;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + amount + strings.DESCRIPTIONS[2];
    }
}
