package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 双雷贯耳（1费）
 * 升级前：造成 6 点伤害 2 次
 * 升级后：造成 8 点伤害 2 次
 */
public class TwinThunder extends AbstractEasyCard {
    public final static String ID = makeID("TwinThunder");

    public TwinThunder() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
