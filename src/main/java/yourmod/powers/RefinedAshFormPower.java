package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.dominic.StarAshBloom;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 精灰形态：霜寒减半转精灰；消耗获精灰；满10给星灰绽放 */
public class RefinedAshFormPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("RefinedAshForm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int EXHAUST_GAIN_CAP = 3;

    public RefinedAshFormPower(AbstractCreature owner, int bloomDamage) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, bloomDamage);
        DominicCombatState.refinedAshBloomDamage = bloomDamage;
    }

    public void gainAshFromFrostLoss(int lost) {
        if (lost <= 0) {
            return;
        }
        flash();
        gainAsh(lost);
    }

    public void onCardExhausted() {
        if (DominicCombatState.ashExhaustGainThisTurn >= EXHAUST_GAIN_CAP) {
            return;
        }
        DominicCombatState.ashExhaustGainThisTurn++;
        flash();
        gainAsh(1);
    }

    private void gainAsh(int amt) {
        AbstractPower ash = owner.getPower(RefinedAshPower.POWER_ID);
        if (ash == null) {
            addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                    owner, owner, new RefinedAshPower(owner, Math.min(RefinedAshPower.CAP, amt)), amt));
            if (amt >= RefinedAshPower.CAP) {
                trySpawnBloom();
            }
        } else {
            int before = ash.amount;
            ash.stackPower(amt);
            ash.updateDescription();
            if (before < RefinedAshPower.CAP && ash.amount >= RefinedAshPower.CAP) {
                trySpawnBloom();
            }
        }
    }

    private void trySpawnBloom() {
        if (DominicCombatState.ashBloomSpawned) {
            return;
        }
        DominicCombatState.ashBloomSpawned = true;
        StarAshBloom bloom = new StarAshBloom();
        bloom.setBloomDamage(amount > 0 ? amount : DominicCombatState.refinedAshBloomDamage);
        addToBot(new MakeTempCardInHandAction(bloom, 1));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
