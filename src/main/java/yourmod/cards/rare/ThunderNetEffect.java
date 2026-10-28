package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ThunderNetEffectPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 雷网效应（1费）
 * 升级前：虚无；静电敌人单体伤害改为自传导
 * 升级后：不再虚无
 */
public class ThunderNetEffect extends AbstractEasyCard {
    public final static String ID = makeID("ThunderNetEffect");

    public ThunderNetEffect() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ThunderNetEffectPower(p));
    }

    @Override
    public void upp() {
        isEthereal = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
