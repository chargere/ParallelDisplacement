package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class WorldlyPearlPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjWorldlyPearl");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int healOnExecute;
    public int splashPct;

    public WorldlyPearlPower(AbstractCreature owner, int lineBonus, int heal, int splash) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, lineBonus);
        this.healOnExecute = heal;
        this.splashPct = splash;
        useVanillaIcon("regeneration");
        JingjueCombatState.executeLineBonusPct += lineBonus;
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1] + healOnExecute + strings.DESCRIPTIONS[2]);
    }
}
