package yourmod.cards.fvv;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.UselessPower;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

public class KeyboardWarrior extends AbstractFvvCard {
    public static final String ID = makeID("KeyboardWarrior");

    public KeyboardWarrior() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmgRandom(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        UselessPower.gain(p, p, 1);
        if (UselessPower.getAmount(p) >= 1) {
            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(new EasyModalChoiceCard("Again", "Spend 1 Useless, hit random again.", () -> {
                UselessPower.spend(p, 1);
                dmgRandom(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            }));
            choices.add(new EasyModalChoiceCard("Skip", "Do nothing.", () -> {}));
            atb(new EasyModalChoiceAction(choices));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(1);
    }
}
