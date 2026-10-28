package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.relics.PrismOpticSystem;

import static yourmod.ModFile.makeID;

/**
 * 棱镜倍伤标记：该敌人受到来自玩家的伤害 ×1.5。
 */
public class PrismAmpPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("PrismAmp");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public PrismAmpPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, -1);
        useVanillaIcon("accuracy");
        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * PrismOpticSystem.BUFF;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
