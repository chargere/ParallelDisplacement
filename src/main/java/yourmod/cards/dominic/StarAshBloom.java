package yourmod.cards.dominic;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.DominicCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.RefinedAshFormPower;
import yourmod.powers.RefinedAshPower;
import yourmod.powers.StarMarkPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

/** 星灰绽放：精灰形态满层衍生牌 */
@AutoAdd.Ignore
public class StarAshBloom extends AbstractEasyCard {
    public static final String ID = makeID("StarAshBloom");
    private int bloomDamage = 36;

    public StarAshBloom() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL_ENEMY, DominicCharacter.Enums.DOMINIC_COLOR);
        exhaust = true;
        baseDamage = 36;
        isMultiDamage = true;
        baseBlock = 16;
        baseMagicNumber = magicNumber = 2;
        bloomDamage = DominicCombatState.refinedAshBloomDamage;
        baseDamage = bloomDamage;
    }

    public void setBloomDamage(int dmg) {
        this.bloomDamage = dmg;
        this.baseDamage = dmg;
        this.damage = dmg;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new RemoveSpecificPowerAction(p, p, RefinedAshPower.POWER_ID));
        atb(new RemoveSpecificPowerAction(p, p, RefinedAshFormPower.POWER_ID));
        atb(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(bloomDamage, true),
                DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        for (AbstractMonster mo : getEnemies()) {
            atb(new ApplyPowerAction(mo, p, new StarMarkPower(mo, magicNumber), magicNumber));
        }
        blck();
    }

    @Override
    public void upp() {
    }
}
