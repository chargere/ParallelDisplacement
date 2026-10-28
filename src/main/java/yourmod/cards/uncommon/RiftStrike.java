package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.NextTurnTempStrPower;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 裂隙打击（1费）
 * 升级前：4 伤；下回合临时力量=2×本回合攻击数（含此牌）
 * 升级后：7 伤
 */
public class RiftStrike extends AbstractEasyCard {
    public final static String ID = makeID("RiftStrike");

    public RiftStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        // cardsPlayedThisTurn 在 use 时通常已包含本牌
        int attacks = HongwenHooks.countAttacksThisTurn();
        if (attacks < 1) attacks = 1;
        applyToSelf(new NextTurnTempStrPower(p, 2 * attacks));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
