package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;
import yourmod.powers.EternalStormAnchorPower;

import static yourmod.ModFile.makeID;

/**
 * 永恒风暴的锚点（Boss）：回合结束手动选择任意张保留；每保留 2 张失去 1 生命。
 */
public class EternalStormAnchor extends AbstractEasyRelic {
    public static final String ID = makeID("EternalStormAnchor");

    public EternalStormAnchor() {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY, CharacterFile.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new EternalStormAnchorPower(AbstractDungeon.player)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EternalStormAnchor();
    }
}
