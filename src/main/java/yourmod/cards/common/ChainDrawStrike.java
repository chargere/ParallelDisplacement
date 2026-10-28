package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 连续抽击（1费）
 * 升级前：造成 4 点伤害 2 次；抽1
 * 升级后：造成 4 点伤害 3 次；抽1
 */
public class ChainDrawStrike extends AbstractEasyCard {
    public final static String ID = makeID("ChainDrawStrike");

    public ChainDrawStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
        addToBot(new DrawCardAction(1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
