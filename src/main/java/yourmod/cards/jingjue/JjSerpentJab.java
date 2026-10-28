package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 蛇骨刺 */
public class JjSerpentJab extends AbstractJingjueCard {
    public static final String ID = makeID("JjSerpentJab");

    public JjSerpentJab() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 7;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        jjHit(m, hasImaginary(m) ? (upgraded ? 13 : 10) : damage, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, 0);
        if (hasImaginary(m)) {
            giveWeak(m, 1);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
