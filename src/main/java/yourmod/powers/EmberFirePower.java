package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

/**
 * 烬火：挂在敌人身上的负面。
 * 受到单体攻击且层数足够时，配合「燎原猛攻」使伤害 +4×X。
 * （永燃 4/5/6 的无条件加伤在燎原猛攻的 atDamageGive 里处理，避免重复加算。）
 */
public class EmberFirePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EmberFire");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EmberFirePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) {
            yourmod.util.HongwenHooks.onEmberApplied((com.megacrit.cardcrawl.monsters.AbstractMonster) owner, amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (owner instanceof com.megacrit.cardcrawl.monsters.AbstractMonster) {
            yourmod.util.HongwenHooks.onEmberApplied((com.megacrit.cardcrawl.monsters.AbstractMonster) owner, stackAmount);
        }
    }

    /** 算伤预览/结算：层数够则临时加伤（真正扣层在燎原猛攻里） */
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        if (!ScorchingAssaultPower.isSingleTargetAttack(card)) {
            return damage;
        }
        AbstractPower assault = AbstractDungeon.player.getPower(ScorchingAssaultPower.POWER_ID);
        if (assault == null) {
            return damage;
        }
        // 永燃自由触发时加伤已在玩家侧算过，这里不再加，防止双倍
        if (EternalBurnPower.isFreeProc(assault.amount)) {
            return damage;
        }
        if (this.amount >= assault.amount) {
            return damage + 4 * assault.amount;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
