package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 逆命：本回合首次受怪物伤害时反伤并上烬火，然后移除 */
public class DefyFatePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DefyFate");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered = false;

    public DefyFatePower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, -1);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!triggered && damageAmount > 0 && info.owner != null && info.owner instanceof AbstractMonster) {
            triggered = true;
            flash();
            AbstractMonster m = (AbstractMonster) info.owner;
            m.damage(new DamageInfo(owner, damageAmount, DamageInfo.DamageType.THORNS));
            AbstractDungeon.actionManager.addToTop(
                    new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                            m, owner, new EmberFirePower(m, damageAmount), damageAmount));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if (!triggered) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
