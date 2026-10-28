package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;

/** 雷烬双咏：基础伤；有烬火/静电可各再打一段。 */
public class EmberThunderDuet extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderDuet");

    public EmberThunderDuet() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (m != null && m.hasPower(EmberFirePower.POWER_ID)) {
            dmg(m, AbstractGameAction.AttackEffect.FIRE);
        }
        if (m != null && m.hasPower(StaticMarkPower.POWER_ID)) {
            dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
