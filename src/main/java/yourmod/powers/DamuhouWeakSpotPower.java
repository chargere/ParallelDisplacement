package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class DamuhouWeakSpotPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouWeakSpot");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean usedThisTurn;

    public DamuhouWeakSpotPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("accuracy");
        usedThisTurn = false;
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (usedThisTurn || damageAmount <= 0 || !(target instanceof AbstractMonster) || target == owner) {
            return;
        }
        if (info.type != DamageInfo.DamageType.NORMAL) {
            return;
        }
        usedThisTurn = true;
        flash();
        addToBot(new ApplyPowerAction(target, owner, new BreachPower(target, amount), amount));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
