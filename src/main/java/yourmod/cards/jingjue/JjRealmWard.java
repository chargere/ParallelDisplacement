package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.jingjue.ImaginaryPower;

import static yourmod.ModFile.makeID;

/** 行境结界 */
public class JjRealmWard extends AbstractJingjueCard {
    public static final String ID = makeID("JjRealmWard");

    public JjRealmWard() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 10;
        baseMagicNumber = magicNumber = 4;
    }

    private int imaginaryCount() {
        if (AbstractDungeon.getMonsters() == null) {
            return 0;
        }
        int n = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (mo != null && !mo.isDeadOrEscaped() && mo.hasPower(ImaginaryPower.POWER_ID)) {
                n++;
            }
        }
        return n;
    }

    @Override
    public void applyPowers() {
        int saved = baseBlock;
        baseBlock = saved + imaginaryCount() * magicNumber;
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
        upgradeMagicNumber(1);
    }
}
