package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.powers.AbstractEasyPower;

import static yourmod.ModFile.makeID;

/** 达普鬼虫：业火 / 寒冰。amount 奇=业火，偶=寒冰。 */
public class DapuFormPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjDapuForm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DapuFormPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1);
        useVanillaIcon("demonform");
    }

    public void toggle() {
        amount = amount == 1 ? 2 : 1;
        updateDescription();
        flash();
    }

    public static boolean isFire() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasPower(POWER_ID)
                && AbstractDungeon.player.getPower(POWER_ID).amount == 1;
    }

    public static boolean isIce() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasPower(POWER_ID)
                && AbstractDungeon.player.getPower(POWER_ID).amount == 2;
    }

    @Override
    public void updateDescription() {
        locDesc(amount == 1 ? strings.DESCRIPTIONS[0] : strings.DESCRIPTIONS[1]);
    }
}
