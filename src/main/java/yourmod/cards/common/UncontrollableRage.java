package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.derived.AngerStatus;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.shuffleIn;

/**
 * 怒不可遏（1费）
 * 升级前：16 伤；将一张「愤怒」洗入抽牌堆
 * 升级后：21 伤
 */
public class UncontrollableRage extends AbstractEasyCard {
    public final static String ID = makeID("UncontrollableRage");

    public UncontrollableRage() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 16;
        cardsToPreview = new AngerStatus();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        shuffleIn(new AngerStatus());
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}
