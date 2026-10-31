package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.ComboSystem;

import static yourmod.ModFile.makeID;

/**
 * 异位：起始技能（1 费，5/7 格挡）。
 * 出牌时把「技」推进全局连携队列；连携效果由 ComboSystem 统一结算。
 */
public class YiWei extends AbstractEasyCard {
    public static final String ID = makeID("YiWei");

    public YiWei() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        baseBlock = 5;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        ComboSystem.record(p, this); // 进全局连携队列（技）
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
