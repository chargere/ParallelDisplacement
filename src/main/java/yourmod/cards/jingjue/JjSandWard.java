package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 幻沙障 */
public class JjSandWard extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandWard");

    public JjSandWard() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 6;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void applyPowers() {
        int saved = baseBlock;
        if (JingjueCombatState.gainedHourglassThisTurn) {
            baseBlock = upgraded ? 12 : 9;
        }
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
