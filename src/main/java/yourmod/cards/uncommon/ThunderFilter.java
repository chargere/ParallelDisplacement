package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 雷纹滤网（1费）
 * 升级前：预见5；弃掉的每张对随机敌人 1 静电；消耗
 * 升级后：预见7
 */
public class ThunderFilter extends AbstractEasyCard {
    public final static String ID = makeID("ThunderFilter");

    public ThunderFilter() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        HongwenHooks.staticPerDiscardLeft = magicNumber;
        atb(new ScryAction(magicNumber));
        atb(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                HongwenHooks.staticPerDiscardLeft = 0;
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
