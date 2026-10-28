package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.powers.UnfinishedStarforgePower;

import static yourmod.ModFile.makeID;

/** 星锻未完成稿：星灭攻击 +8；首次星灭下回合 +1 能量（见 StarExtinguishPower） */
public class UnfinishedStarforge extends AbstractEasyRelic {
    public static final String ID = makeID("UnfinishedStarforge");
    private static final int BONUS_DAMAGE = 8;

    public UnfinishedStarforge() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new UnfinishedStarforgePower(AbstractDungeon.player, BONUS_DAMAGE), BONUS_DAMAGE));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UnfinishedStarforge();
    }
}
