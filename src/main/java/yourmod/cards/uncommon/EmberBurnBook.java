package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 烬火焚书（1费）
 * 升级前：抽4；每抽到攻击则全体 2 烬火；消耗
 * 升级后：每抽到攻击则全体 3 烬火
 */
public class EmberBurnBook extends AbstractEasyCard {
    public final static String ID = makeID("EmberBurnBook");

    public EmberBurnBook() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(4, new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (c.type == CardType.ATTACK) {
                        forAllMonstersLiving(mo -> applyToEnemy(mo, new EmberFirePower(mo, magicNumber)));
                    }
                }
                isDone = true;
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
