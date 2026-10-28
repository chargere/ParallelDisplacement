package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 雮尘珠 */
public class PhoenixGallbladderRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjPhoenixGallbladder");
    public static final float EXECUTE_BONUS = 0.08f;

    public PhoenixGallbladderRelic() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        JingjueCombatState.gainHourglass(2);
        AbstractMonster m = Wiz.getRandomEnemy();
        if (m != null) {
            addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ImaginaryPower(m, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PhoenixGallbladderRelic();
    }
}
