package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.util.DamuhouFireMode;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 左右为难 */
public class DamuhouLeftOrRight extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouLeftOrRight");

    public DamuhouLeftOrRight() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.NONE);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 4;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<com.megacrit.cardcrawl.cards.AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("打最左", "额外攻击 " + magicNumber + " 次。", () -> {
            int saved = damage;
            directHits(DamuhouFireMode.leftmostLiving(), 2 + magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            damage = saved;
        }));
        choices.add(new EasyModalChoiceCard("打最右", "伤害增加 " + secondMagic + " 点。", () -> {
            int saved = damage;
            damage += secondMagic;
            directHits(DamuhouFireMode.rightmostLiving(), 2, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            damage = saved;
        }));
        addToBot(new EasyModalChoiceAction(choices, 1, "左右为难"));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(2);
    }
}
