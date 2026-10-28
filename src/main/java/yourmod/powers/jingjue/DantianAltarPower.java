package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class DantianAltarPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjDantianAltar");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final int mult;

    public DantianAltarPower(AbstractCreature owner, int mult) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, mult);
        this.mult = mult;
        useVanillaIcon("combust");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        int sand = JingjueCombatState.hourglass();
        int dmg = (sand / 2) * amount;
        if (dmg > 0) {
            flash();
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player,
                    DamageInfo.createDamageMatrix(dmg, true), DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE));
        }
        if (sand > 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, HourglassPower.POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]);
    }
}
