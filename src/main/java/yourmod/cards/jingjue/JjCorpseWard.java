package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 尸香障壁 */
public class JjCorpseWard extends AbstractJingjueCard {
    public static final String ID = makeID("JjCorpseWard");

    public JjCorpseWard() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        giveImaginary(m, magicNumber);
        giveWeak(m, 1);
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
