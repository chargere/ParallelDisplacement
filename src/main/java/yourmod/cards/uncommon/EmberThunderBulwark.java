package yourmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 焚雷壁垒（1费）
 * 升级前：8 格挡；弃1：攻击则全体2烬火；技能则全体1静电；否则1能量+抽1
 * 升级后：11 格挡
 */
public class EmberThunderBulwark extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderBulwark");

    public EmberThunderBulwark() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new SelectCardsInHandAction(1, "弃置", false, false, c -> true, cards -> {
            for (AbstractCard c : cards) {
                att(new DiscardSpecificCardAction(c));
                if (c.type == CardType.ATTACK) {
                    forAllMonstersLiving(mo -> applyToEnemy(mo, new EmberFirePower(mo, 2)));
                } else if (c.type == CardType.SKILL) {
                    forAllMonstersLiving(mo -> applyToEnemy(mo, new StaticMarkPower(mo, 1)));
                } else {
                    att(new GainEnergyAction(1));
                    att(new DrawCardAction(1));
                }
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
