package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class GlacierCorpseRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjGlacierCorpse");

    public GlacierCorpseRelic() {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 4));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GlacierCorpseRelic();
    }
}
