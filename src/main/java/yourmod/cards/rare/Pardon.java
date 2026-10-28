package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 赦免（3费）
 * 升级前：30 格挡；回复8（本回合受过伤害则回复16）；消耗
 * 升级后：35 格挡；回复10 / 受过伤则回复20
 */
public class Pardon extends AbstractEasyCard {
    public final static String ID = makeID("Pardon");

    public Pardon() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 30;
        baseMagicNumber = magicNumber = 8;
        baseSecondMagic = secondMagic = 16;
        exhaust = true;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        int heal = HongwenHooks.tookDamageThisTurn ? secondMagic : magicNumber;
        atb(new HealAction(p, p, heal));
    }

    @Override
    public void upp() {
        upgradeBlock(5);
        upgradeMagicNumber(2);
        upgradeSecondMagic(4);
    }
}
