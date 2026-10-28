package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class StardustImpact extends AbstractDominicCard {
    public static final String ID = makeID("StardustImpact");

    public StardustImpact() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 4;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int nonAtk = 0;
        for (com.megacrit.cardcrawl.cards.AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c != this && c.type != CardType.ATTACK) {
                nonAtk++;
            }
        }
        int total = damage + nonAtk * magicNumber;
        atb(new com.megacrit.cardcrawl.actions.common.DamageAction(
                m, new com.megacrit.cardcrawl.cards.DamageInfo(p, total, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void applyPowers() {
        int nonAtk = countNonAttack();
        int realBase = baseDamage;
        baseDamage = realBase + nonAtk * magicNumber;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int nonAtk = countNonAttack();
        int realBase = baseDamage;
        baseDamage = realBase + nonAtk * magicNumber;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    private int countNonAttack() {
        if (AbstractDungeon.actionManager == null) {
            return 0;
        }
        int nonAtk = 0;
        for (com.megacrit.cardcrawl.cards.AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type != CardType.ATTACK) {
                nonAtk++;
            }
        }
        return nonAtk;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }

    private static void atb(com.megacrit.cardcrawl.actions.AbstractGameAction a) {
        yourmod.util.Wiz.atb(a);
    }
}
