package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

/** 星尘沉淀：冰尘时全体星痕，每回合最多 amount2 次 */
public class StardustSettlePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("StardustSettle");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public StardustSettlePower(AbstractCreature owner, int marksPerTrigger, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, marksPerTrigger);
        this.amount2 = maxPerTurn;
        this.isTwoAmount = true;
        updateDescription();
    }

    public void onIceDust() {
        if (DominicCombatState.stardustSettleTriggersThisTurn >= amount2) {
            return;
        }
        DominicCombatState.stardustSettleTriggersThisTurn++;
        flash();
        if (owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            for (com.megacrit.cardcrawl.monsters.AbstractMonster m : getEnemies()) {
                atb(new ApplyPowerAction(m, p, new StarMarkPower(m, amount), amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + amount2 + strings.DESCRIPTIONS[2];
    }
}
