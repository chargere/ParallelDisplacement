package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class AuroraResonance extends AbstractDominicCard {
    public static final String ID = makeID("AuroraResonance");

    public AuroraResonance() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
        selfRetain = true;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 3);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int played = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        // 当前牌已计入 cardsPlayedThisTurn，减去自身
        int bonus = Math.max(0, played - 1);
        int total = damage + bonus * magicNumber;
        atb(new com.megacrit.cardcrawl.actions.common.DamageAction(
                m, new com.megacrit.cardcrawl.cards.DamageInfo(p, total, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void applyPowers() {
        int played = AbstractDungeon.actionManager == null ? 0
                : AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        int bonus = played * magicNumber;
        int realBase = baseDamage;
        baseDamage = realBase + bonus;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int played = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        int bonus = played * magicNumber;
        int realBase = baseDamage;
        baseDamage = realBase + bonus;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }

    private static void atb(com.megacrit.cardcrawl.actions.AbstractGameAction a) {
        yourmod.util.Wiz.atb(a);
    }
}
