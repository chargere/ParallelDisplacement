package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 天雷滚滚我好怕（2费）
 * 升级前：14 点失去生命伤害；打出后永久+1伤
 * 升级后：18 点失去生命伤害
 */
public class ScaryThunder extends AbstractEasyCard {
    public final static String ID = makeID("ScaryThunder");

    public ScaryThunder() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 14;
        this.damageType = DamageInfo.DamageType.HP_LOSS;
        this.damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        this.baseDamage++;
        this.applyPowers();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                c.baseDamage++;
            }
        }
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
