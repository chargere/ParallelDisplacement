package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashMap;
import java.util.Map;

/** 终焉·灭星斩：全体伤害，若有击杀则连锁，最后若手牌无攻击则格挡 */
public class FinalCutChainAction extends AbstractGameAction {
    private static final int MAX_DEPTH = 20;

    private final AbstractPlayer player;
    private final int[] multiDamage;
    private final int blockAmount;
    private final int depth;
    private final boolean finishWithBlockCheck;

    public FinalCutChainAction(AbstractPlayer player, int[] multiDamage, int blockAmount) {
        this(player, multiDamage, blockAmount, 0, true);
    }

    private FinalCutChainAction(AbstractPlayer player, int[] multiDamage, int blockAmount, int depth, boolean finishWithBlockCheck) {
        this.player = player;
        this.multiDamage = multiDamage;
        this.blockAmount = blockAmount;
        this.depth = depth;
        this.finishWithBlockCheck = finishWithBlockCheck;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (player == null || AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().monsters == null
                || multiDamage == null) {
            isDone = true;
            return;
        }

        Map<AbstractMonster, Integer> hpBefore = new HashMap<>();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != null && !m.isDeadOrEscaped()) {
                hpBefore.put(m, m.currentHealth);
            }
        }

        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                boolean killed = false;
                if (AbstractDungeon.getCurrRoom() != null
                        && AbstractDungeon.getCurrRoom().monsters != null) {
                    for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (m != null && hpBefore.containsKey(m)
                                && (m.isDead || m.isDying || m.currentHealth <= 0)) {
                            killed = true;
                            break;
                        }
                    }
                }
                if (killed && depth + 1 < MAX_DEPTH) {
                    addToTop(new FinalCutChainAction(player, multiDamage, blockAmount, depth + 1, finishWithBlockCheck));
                } else if (finishWithBlockCheck && player.hand != null) {
                    addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            boolean hasAttack = false;
                            if (player.hand != null) {
                                for (AbstractCard c : player.hand.group) {
                                    if (c != null && c.type == AbstractCard.CardType.ATTACK) {
                                        hasAttack = true;
                                        break;
                                    }
                                }
                            }
                            if (!hasAttack && blockAmount > 0) {
                                addToTop(new GainBlockAction(player, player, blockAmount));
                            }
                            isDone = true;
                        }
                    });
                }
                isDone = true;
            }
        });

        addToTop(new DamageAllEnemiesAction(player, multiDamage, DamageInfo.DamageType.NORMAL, AttackEffect.SLASH_HEAVY));
        isDone = true;
    }
}
