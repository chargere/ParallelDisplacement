package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 禁忌之瞳 */
public class JjForbiddenEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjForbiddenEye");

    public JjForbiddenEye() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        giveEye(m, magicNumber);
        giveVuln(m, 1);
        addToBot(new DrawCardAction(p, 1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
