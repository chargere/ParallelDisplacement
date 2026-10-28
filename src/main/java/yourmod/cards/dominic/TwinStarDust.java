package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;

public class TwinStarDust extends AbstractDominicCard {
    public static final String ID = makeID("TwinStarDust");

    public TwinStarDust() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 2;
        setFrostPay(1, FrostPayType.DELAY, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    if (m != null && !m.isDeadOrEscaped()) {
                        calculateCardDamage(m);
                        int d = damage;
                        if (m.hasPower(StarMarkPower.POWER_ID)) {
                            d += 3;
                        }
                        att(new DamageAction(m, new DamageInfo(p, d, DamageInfo.DamageType.THORNS),
                                AttackEffect.SLASH_HORIZONTAL));
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
