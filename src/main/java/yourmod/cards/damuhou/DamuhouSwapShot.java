package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.relics.PrismOpticSystem;
import yourmod.util.DamuhouFireMode;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 换边点射：先打再翻 / 先翻再打。 */
public class DamuhouSwapShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSwapShot");

    public DamuhouSwapShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<com.megacrit.cardcrawl.cards.AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("先打再翻", "攻击 " + magicNumber + " 次，然后对调瞄准方向。", () -> {
            comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            PrismOpticSystem.swapIfPresent();
        }));
        choices.add(new EasyModalChoiceCard("先翻再打", "先对调瞄准方向，再攻击 " + magicNumber + " 次。", () -> {
            PrismOpticSystem.swapIfPresent();
            if (DamuhouFireMode.isFullAuto()) {
                AbstractMonster t = DamuhouFireMode.sightedLiving();
                if (t != null) {
                    calculateCardDamage(t);
                }
            }
            comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }));
        addToBot(new EasyModalChoiceAction(choices, 1, "换边点射"));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        printedCombo = magicNumber;
    }
}
