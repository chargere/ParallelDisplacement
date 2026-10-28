package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 蛇骨壁垒 */
public class JjBoneWall extends AbstractJingjueCard {
    public static final String ID = makeID("JjBoneWall");

    public JjBoneWall() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void applyPowers() {
        int saved = baseBlock;
        baseBlock = saved + JingjueCombatState.hourglass() * magicNumber;
        super.applyPowers();
        baseBlock = saved;
        isBlockModified = block != baseBlock;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
