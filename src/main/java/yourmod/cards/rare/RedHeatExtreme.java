package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.RedHeatExtremePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 红温·极意（1费）
 * 升级前：无法回复；本应回复改为全体失去生命×3
 * 升级后：固有
 */
public class RedHeatExtreme extends AbstractEasyCard {
    public final static String ID = makeID("RedHeatExtreme");

    public RedHeatExtreme() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new RedHeatExtremePower(p));
    }

    @Override
    public void upp() {
        isInnate = true;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
