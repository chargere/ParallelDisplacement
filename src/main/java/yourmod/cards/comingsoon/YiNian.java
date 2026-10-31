package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.ComboSystem;

import static yourmod.ModFile.makeID;

/**
 * 一念：起始攻击（1 费，6/9 伤害）。
 * 出牌时把「攻」推进全局连携队列；连携效果（如「攻攻技」）由 ComboSystem 统一结算。
 */
public class YiNian extends AbstractEasyCard {
    public static final String ID = makeID("YiNian");

    public YiNian() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        baseDamage = 6;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        ComboSystem.record(p, this); // 进全局连携队列（攻）
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
