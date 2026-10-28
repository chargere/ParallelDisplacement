package yourmod.cards.fvv;

import yourmod.FvvEggCharacter;
import yourmod.cards.AbstractEasyCard;

/**
 * fvv蛋蛋卡牌基类。
 */
public abstract class AbstractFvvCard extends AbstractEasyCard {
    public AbstractFvvCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, rarity, target, FvvEggCharacter.Enums.FVV_COLOR);
    }
}
