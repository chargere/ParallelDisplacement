package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 近身搏击：肘击换防，枪托多打。不再跟左右为难抢最左/最右。 */
public class DamuhouGunFuMelee extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouGunFuMelee");

    public DamuhouGunFuMelee() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseBlock = 5;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 4;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<com.megacrit.cardcrawl.cards.AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("肘击", "获得 " + block + " 点格挡，攻击 " + magicNumber + " 次。", () -> {
            blck();
            comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }));
        choices.add(new EasyModalChoiceCard("枪托", "攻击 " + secondMagic + " 次。", () ->
                comboHits(m, secondMagic, AbstractGameAction.AttackEffect.BLUNT_HEAVY)));
        addToBot(new EasyModalChoiceAction(choices, 1, "近身搏击"));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeSecondMagic(1);
    }
}
