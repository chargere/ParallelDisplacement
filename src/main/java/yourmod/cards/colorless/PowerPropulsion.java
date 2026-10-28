package yourmod.cards.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.NextTurnWeakPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 动力推进（无色稀有 / 1费）
 * 获得 1 层无实体；下回合开始时获得 1 层虚弱与 1 层易伤；消耗
 * 升级后：不再消耗
 */
public class PowerPropulsion extends AbstractEasyCard {
    public final static String ID = makeID("PowerPropulsion");

    public PowerPropulsion() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new IntangiblePlayerPower(p, magicNumber));
        applyToSelf(new NextTurnWeakPower(p, secondMagic));
    }

    @Override
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
