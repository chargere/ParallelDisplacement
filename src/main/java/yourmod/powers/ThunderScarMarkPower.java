package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 雷痕：永久负面，受到玩家普通伤害 +3 */
public class ThunderScarMarkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ThunderScarMark");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ThunderScarMarkPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, -1);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type, com.megacrit.cardcrawl.cards.AbstractCard card) {
        if (type == DamageInfo.DamageType.NORMAL && card != null) {
            return damage + 3;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
