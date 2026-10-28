package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

public class CrystalEyeRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjCrystalEye");

    public CrystalEyeRelic() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        Wiz.forAllMonstersLiving(m ->
                addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ImaginaryPower(m, 2), 2)));
        AbstractDungeon.player.drawPile.shuffle();
        AbstractDungeon.player.gameHandSize -= 1;
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.actionManager.turn == 1) {
            AbstractDungeon.player.gameHandSize += 1;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalEyeRelic();
    }
}
