package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.TimeComboTracker;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 一念：起始攻击（1 费，6/9 伤害）。
 * 连携：若上一张攻击牌的目标与本次相同，则为其附加一层随机负面
 * （易伤 / 虚弱 / 力量下降 / 敏捷下降 / 脆弱[绑缚占位]）。
 */
public class YiNian extends AbstractEasyCard {
    public static final String ID = makeID("YiNian");

    public YiNian() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        baseDamage = 6;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (TimeComboTracker.lastAttackTarget != null
                && TimeComboTracker.lastAttackTarget == m
                && !m.isDeadOrEscaped()) {
            applyRandomDebuff(m);
        }
        TimeComboTracker.recordAttack(m);
    }

    private void applyRandomDebuff(AbstractMonster m) {
        int r = AbstractDungeon.cardRandomRng.random(0, 4);
        switch (r) {
            case 0: Wiz.applyToEnemy(m, new VulnerablePower(m, 1, false)); break;
            case 1: Wiz.applyToEnemy(m, new WeakPower(m, 1, false)); break;
            case 2: Wiz.applyToEnemy(m, new StrengthPower(m, -1)); break;   // 力量下降
            case 3: Wiz.applyToEnemy(m, new DexterityPower(m, -1)); break;  // 敏捷下降
            case 4: Wiz.applyToEnemy(m, new FrailPower(m, 1, false)); break; // 绑缚（暂以脆弱占位）
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
