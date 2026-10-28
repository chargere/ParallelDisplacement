package yourmod.cards.derived;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 【衍生卡】愤怒 —— 由「怒不可遏」洗入抽牌堆
 * （1费）
 * 升级前：保留；打出后消耗；回合结束因保留仍在手中则受到 3 点伤害
 * 升级后：无数值变化（不进卡池，一般不升级）
 *
 * 注意：带 selfRetain 的牌在 DiscardAtEndOfTurn 时会先被挪到 limbo，
 * triggerOnEndOfPlayerTurn 不会对它们调用；自伤必须写在 onRetained()。
 */
@AutoAdd.Ignore
public class AngerStatus extends AbstractEasyCard {
    public final static String ID = makeID("AngerStatus");
    private static final int END_TURN_DAMAGE = 3;

    public AngerStatus() {
        super(ID, 1, CardType.STATUS, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 花 1 费打出并消耗，避免回合结束自伤
    }

    @Override
    public void onRetained() {
        addToBot(new DamageAction(AbstractDungeon.player,
                new DamageInfo(AbstractDungeon.player, END_TURN_DAMAGE, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upp() {
    }
}
