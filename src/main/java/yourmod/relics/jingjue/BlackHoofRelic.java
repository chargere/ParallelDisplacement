package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

public class BlackHoofRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjBlackHoof");

    public BlackHoofRelic() {
        super(ID, RelicTier.COMMON, LandingSound.HEAVY, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        Wiz.forAllMonstersLiving(m ->
                addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ImaginaryPower(m, 1), 1)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlackHoofRelic();
    }
}
