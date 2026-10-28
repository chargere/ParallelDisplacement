package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonWindCharacter;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

public class WindDefend extends AbstractEasyCard {
    public static final String ID = makeID("WindDefend");

    public WindDefend() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF, ComingSoonWindCharacter.Enums.WIND_COLOR);
        baseBlock = 5;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
