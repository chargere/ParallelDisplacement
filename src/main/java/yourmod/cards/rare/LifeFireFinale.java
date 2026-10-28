package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.LifeFireFinaleAction;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 命火终章（4费）
 * 升级前：28 伤；击杀则 3能量/3力量/3敏捷/抽3；消耗
 * 升级后：34 伤
 */
public class LifeFireFinale extends AbstractEasyCard {
    public final static String ID = makeID("LifeFireFinale");

    public LifeFireFinale() {
        super(ID, 4, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 28;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new LifeFireFinaleAction(p, m, damage));
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}
