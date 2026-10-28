package yourmod.cards.derived;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/** 雨声：雨天的约定转化产物（不进卡池）。 */
@AutoAdd.Ignore
public class RainSound extends AbstractEasyCard {
    public final static String ID = makeID("RainSound");

    public RainSound() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        isEthereal = true;
        exhaust = true;
        tags.add(CardTags.HEALING);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new HealAction(p, p, magicNumber));
        atb(new DrawCardAction(1));
    }

    @Override
    public void upp() {
    }
}
