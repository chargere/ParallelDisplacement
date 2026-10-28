package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.HealHalvePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

/**
 * 命火·契约（0费）
 * 升级前：保留；生命降至1获等量格挡；壁垒；回复减半
 * 升级后：再获得 5 点格挡
 */
public class LifeFirePact extends AbstractEasyCard {
    public final static String ID = makeID("LifeFirePact");

    public LifeFirePact() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                int lost = Math.max(0, p.currentHealth - 1);
                if (lost > 0) {
                    p.damage(new DamageInfo(p, lost, DamageInfo.DamageType.HP_LOSS));
                    p.addBlock(lost);
                }
                isDone = true;
            }
        });
        applyToSelf(new BarricadePower(p));
        applyToSelf(new HealHalvePower(p));
        if (upgraded) {
            blck();
        }
    }

    @Override
    public void upp() {
        baseBlock = 5;
        block = 5;
        upgradedBlock = true;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
