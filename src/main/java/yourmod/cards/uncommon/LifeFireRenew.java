package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 命火续燃（1费）
 * 升级前：回复5；回合结束仍在手中再回复3；虚无；消耗
 * 升级后：回复7；回合结束再回复4
 */
public class LifeFireRenew extends AbstractEasyCard {
    public final static String ID = makeID("LifeFireRenew");

    public LifeFireRenew() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 3;
        exhaust = true;
        isEthereal = true;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new HealAction(p, p, magicNumber));
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (AbstractDungeon.player.hand.contains(this)) {
            atb(new HealAction(AbstractDungeon.player, AbstractDungeon.player, secondMagic));
        }
        super.triggerOnEndOfPlayerTurn();
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        upgradeSecondMagic(1);
    }
}
