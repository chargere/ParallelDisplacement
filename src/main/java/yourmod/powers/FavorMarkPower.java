package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.relics.UnsentFlower;

import static yourmod.ModFile.makeID;

/** 眷顾：受到伤害时额外吃 3 点伤（由未送出的花限制每回合次数）。 */
public class FavorMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FavorMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FavorMarkPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, -1);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL || AbstractDungeon.player == null) {
            return damage;
        }
        AbstractRelic r = AbstractDungeon.player.getRelic(UnsentFlower.ID);
        if (r instanceof UnsentFlower && ((UnsentFlower) r).canProcBonus()) {
            return damage + 3;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
