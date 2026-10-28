package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static yourmod.ModFile.makeID;

/**
 * 冻裂：立即失去敏捷，回合结束时还原。
 */
public class FreezeCrackPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FreezeCrack");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FreezeCrackPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    public static void apply(AbstractCreature owner, int amount) {
        if (owner == null || amount <= 0) {
            return;
        }
        yourmod.util.DominicCombatState.lostDexThisTurn = true;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, -amount), -amount));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new FreezeCrackPower(owner, amount), amount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
