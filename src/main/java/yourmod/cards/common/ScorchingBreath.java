package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 炽热呼吸（1费）
 * 升级前：4 格挡；全体 1 烬火；被消耗时回复 3 生命
 * 升级后：6 格挡；回复 4 生命
 */
public class ScorchingBreath extends AbstractEasyCard {
    public final static String ID = makeID("ScorchingBreath");

    public ScorchingBreath() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 4;
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        forAllMonstersLiving(mo -> applyToEnemy(mo, new EmberFirePower(mo, magicNumber)));
    }

    @Override
    public void triggerOnExhaust() {
        addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, secondMagic));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeSecondMagic(1);
    }
}
