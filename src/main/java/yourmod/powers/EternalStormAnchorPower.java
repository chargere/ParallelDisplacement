package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.actions.EternalStormRetainAction;

import static yourmod.ModFile.makeID;

/** 由永恒风暴的锚点挂上：回合结束手动选保留。 */
public class EternalStormAnchorPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EternalStormAnchorPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EternalStormAnchorPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        if (AbstractDungeon.player.hand.isEmpty()) {
            return;
        }
        if (AbstractDungeon.player.hasRelic("Runic Pyramid")) {
            return;
        }
        flash();
        addToBot(new EternalStormRetainAction());
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
