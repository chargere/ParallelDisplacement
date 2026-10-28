package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.BlueprintCopyAction;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/** 未竟的工图：保留；复制手牌并本回合减费；消耗。 */
public class UnfinishedBlueprint extends AbstractEasyCard {
    public final static String ID = makeID("UnfinishedBlueprint");

    public UnfinishedBlueprint() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        selfRetain = true;
        exhaust = true;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new BlueprintCopyAction(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
