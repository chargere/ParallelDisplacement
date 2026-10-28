package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 时滞：下一张费用≥1 的牌 -1，软上限每回合 3。 */
public class TimeDilationPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjTimeDilation");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TimeDilationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("slow");
    }

    @Override
    public void onInitialApplication() {
        discountHand();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > JingjueCombatState.dilationCap()) {
            amount = JingjueCombatState.dilationCap();
        }
        discountHand();
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        discountHand();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (canDiscount() && card != null && card.costForTurn >= 1) {
            card.setCostForTurn(Math.max(0, card.costForTurn - 1));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0 || card == null || card.cost < 1 && card.cost != -1) {
            return;
        }
        if (card.costForTurn <= 0 && card.cost >= 1 || card.cost >= 1) {
            if (!canDiscount()) {
                return;
            }
            if (card.costForTurn < card.cost || card.cost == -1) {
                // already paid; consume if this card was eligible
            }
            if (JingjueCombatState.energySavedThisTurn >= JingjueCombatState.DILATION_SOFT_CAP) {
                return;
            }
            if (card.freeToPlay() || card.costForTurn <= 0 && card.cost == 0) {
                return;
            }
            if (card.cost >= 1) {
                flash();
                JingjueCombatState.energySavedThisTurn++;
                amount--;
                updateDescription();
                if (amount <= 0) {
                    addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, ID));
                } else {
                    discountHand();
                }
            }
        }
    }

    private boolean canDiscount() {
        return amount > 0 && JingjueCombatState.energySavedThisTurn < JingjueCombatState.DILATION_SOFT_CAP;
    }

    private void discountHand() {
        if (AbstractDungeon.player == null || !canDiscount()) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn >= 1) {
                c.setCostForTurn(Math.max(0, c.costForTurn - 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
