package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import yourmod.relics.UnfinishedStarforge;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/**
 * 星灭：本回合不能再打出攻击牌。
 */
public class StarExtinguishPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("StarExtinguish");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public StarExtinguishPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, 1);
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            com.megacrit.cardcrawl.characters.AbstractPlayer p =
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) owner;
            com.megacrit.cardcrawl.powers.AbstractPower warm = p.getPower(AfterSheatheWarmthPower.POWER_ID);
            if (warm != null) {
                warm.flash();
                addToBot(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, warm.amount));
            }
            com.megacrit.cardcrawl.powers.AbstractPower doctrine = p.getPower(BladeDoctrinePower.POWER_ID);
            if (doctrine != null) {
                doctrine.flash();
                addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                        p, p, new NextTurnEnergyPower(p, 1), 1));
            }
            if (p.hasRelic(UnfinishedStarforge.ID)
                    && !DominicCombatState.unfinishedStarforgeTriggeredThisCombat) {
                DominicCombatState.unfinishedStarforgeTriggeredThisCombat = true;
                com.megacrit.cardcrawl.relics.AbstractRelic relic = p.getRelic(UnfinishedStarforge.ID);
                if (relic != null) {
                    relic.flash();
                }
                addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                        p, p, new NextTurnEnergyPower(p, 1), 1));
            }
        }
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            card.cantUseMessage = strings.DESCRIPTIONS[1];
            return false;
        }
        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
