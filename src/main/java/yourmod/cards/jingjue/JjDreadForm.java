package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.jingjue.DreadFormPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 惧魇形态：三费稀有，烧抽牌堆顶，消耗换沙漏与眼印。 */
public class JjDreadForm extends AbstractJingjueCard {
    public static final String ID = makeID("JjDreadForm");

    public JjDreadForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DreadFormPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
    }
}
