package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarMarkBarrage extends AbstractDominicCard {
    public static final String ID = makeID("StarMarkBarrage");

    public StarMarkBarrage() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 7;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        boolean hadMark = m != null && m.hasPower(StarMarkPower.POWER_ID);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (hadMark) {
            atb(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
