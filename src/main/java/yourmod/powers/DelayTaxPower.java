package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/**
 * 迟滞：邻牌本回合费用 +X。
 */
public class DelayTaxPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DelayTax");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DelayTaxPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, 1);
    }

    public static void applyToNeighbors(int bump) {
        bumpCost(DominicCombatState.neighborLeft, bump);
        bumpCost(DominicCombatState.neighborRight, bump);
        DominicCombatState.clearNeighbors();
    }

    /** @deprecated */
    public static void applyToNeighbors() {
        applyToNeighbors(1);
    }

    private static void bumpCost(AbstractCard c, int bump) {
        if (c == null || bump <= 0) {
            return;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(c)) {
            if (c.costForTurn >= 0) {
                c.setCostForTurn(c.costForTurn + bump);
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
