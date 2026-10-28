package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 幻沙·三重 */
public class JjSandTriple extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandTriple");

    public JjSandTriple() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 4;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            blck();
        }
    }

    @Override
    public void upp() {
        upgradeBlock(1);
    }
}
