package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.HeavenFireUndyingPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 天火·不灭：致命保命一次；升级前虚无。 */
public class HeavenFireUndying extends AbstractEasyCard {
    public final static String ID = makeID("HeavenFireUndying");

    public HeavenFireUndying() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new HeavenFireUndyingPower(p));
    }

    @Override
    public void upp() {
        isEthereal = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
