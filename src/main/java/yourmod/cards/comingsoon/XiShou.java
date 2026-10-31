package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 吸收：起始攻击（2 费，11 伤害）。
 * 出手前若敌人生命低于自己的存在力，则将其斩杀，并回复 1 点存在力、提升 1 点存在力上限。
 * 奖励为时间旅人自身机制的存在力变化，直接操作字段，不受 ÷4 与「掉 1」影响。
 * 出牌时把「攻」推进全局连携队列（落形态临时力量由 ComboSystem 统一处理）。
 */
public class XiShou extends AbstractEasyCard {
    public static final String ID = makeID("XiShou");

    public XiShou() {
        super(ID, 2, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        baseDamage = 11;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.currentHealth < p.currentHealth) {
            // 斩杀：致死伤害，走正常死亡流程
            Wiz.atb(new DamageAction(m, new DamageInfo(p, 99999, DamageInfo.DamageType.HP_LOSS),
                    AbstractGameAction.AttackEffect.NONE));
            // 奖励：回 1 存在力 + 上限 +1（直接字段操作，绕过 ÷4 / 掉1）
            p.maxHealth += 1;
            p.currentHealth = Math.min(p.currentHealth + 1, p.maxHealth);
            p.healthBarUpdatedEvent();
        } else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        ComboSystem.record(p, this); // 进全局连携队列（攻）
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
