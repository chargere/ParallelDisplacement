package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

/**
 * 沉默的守望：本回合未造成伤害则在回合结束获得静默。
 */
public class SilentVigilPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SilentVigil");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public int silenceCap;
    public int silenceMult;
    private boolean dealtDamageThisTurn;

    public SilentVigilPower(AbstractCreature owner, int silenceCap, int silenceMult) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, silenceCap);
        this.silenceCap = silenceCap;
        this.silenceMult = silenceMult;
        this.dealtDamageThisTurn = false;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        dealtDamageThisTurn = false;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info != null && info.type == DamageInfo.DamageType.NORMAL) {
            dealtDamageThisTurn = true;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || dealtDamageThisTurn) {
            return;
        }
        flash();
        AbstractPower existing = owner.getPower(SilencePower.POWER_ID);
        int cur = existing == null ? 0 : existing.amount;
        int gain = Math.min(3, silenceCap - cur);
        if (gain > 0) {
            addToBot(new ApplyPowerAction(owner, owner, new SilencePower(owner, gain, silenceMult), gain));
        } else if (existing instanceof SilencePower) {
            ((SilencePower) existing).mult = silenceMult;
            existing.updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + silenceCap + strings.DESCRIPTIONS[1] + silenceMult + strings.DESCRIPTIONS[2];
    }
}
