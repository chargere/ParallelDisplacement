package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 雷纹护盾（1费）
 * 升级前：6 格挡；被弃置则 13 格挡并对全体 1 静电；消耗
 * 升级后：9 格挡；被弃置则 16 格挡
 */
public class ThunderShieldSkill extends AbstractEasyCard {
    public final static String ID = makeID("ThunderShieldSkill");

    public ThunderShieldSkill() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 13;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void triggerOnManualDiscard() {
        addToTop(new com.megacrit.cardcrawl.actions.common.GainBlockAction(
                com.megacrit.cardcrawl.dungeons.AbstractDungeon.player,
                com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, magicNumber));
        forAllMonstersLiving(mo -> applyToEnemy(mo, new StaticMarkPower(mo, 1)));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(3);
    }
}
