package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 霜裂：霜寒上限提升，首次叠至上限对全体造成伤害 */
public class FrostCapRiftPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FrostCapRift");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FrostCapRiftPower(AbstractCreature owner, int aoeDamage) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, aoeDamage);
        DominicCombatState.frostCap = 15;
    }

    @Override
    public void onInitialApplication() {
        DominicCombatState.frostCap = 15;
    }

    @Override
    public void onRemove() {
        if (owner.getPower(POWER_ID) == null || owner.getPower(POWER_ID) == this) {
            DominicCombatState.frostCap = FrostColdPower.CAP;
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
