package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.DefyFatePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 逆命（2费）
 * 升级前：6 伤；获得逆命
 * 升级后：9 伤
 */
public class DefyFate extends AbstractEasyCard {
    public final static String ID = makeID("DefyFate");

    public DefyFate() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        applyToSelf(new DefyFatePower(p));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
