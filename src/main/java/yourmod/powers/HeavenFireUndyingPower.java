package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.util.Wiz;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/**
 * 天火·不灭：致命伤改为生命降至 10，清除负面，全体 8 烬火 + 4 静电；每场限一次。
 */
public class HeavenFireUndyingPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("HeavenFireUndying");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean used;

    public HeavenFireUndyingPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        this.used = false;
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (used || damageAmount <= 0 || owner.currentHealth <= 0) {
            return damageAmount;
        }
        if (damageAmount < owner.currentHealth) {
            return damageAmount;
        }
        used = true;
        flash();
        for (AbstractPower p : new ArrayList<>(owner.powers)) {
            if (p.type == PowerType.DEBUFF) {
                addToTop(new RemoveSpecificPowerAction(owner, owner, p.ID));
            }
        }
        for (AbstractMonster m : Wiz.getEnemies()) {
            addToBot(new ApplyPowerAction(m, owner, new EmberFirePower(m, 8), 8));
            addToBot(new ApplyPowerAction(m, owner, new StaticMarkPower(m, 4), 4));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        updateDescription();
        return Math.max(0, owner.currentHealth - 10);
    }

    @Override
    public void updateDescription() {
        description = used ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}
