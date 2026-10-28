package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class SkirmishSlowRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjSkirmishSlow");

    public SkirmishSlowRelic() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        JingjueCombatState.hourglassCapBonus += 3;
        JingjueCombatState.dilationCapBonus += 3;
    }

    @Override
    public void atTurnStart() {
        flash();
        JingjueCombatState.gainDilation(1);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SkirmishSlowRelic();
    }
}
