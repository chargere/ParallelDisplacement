package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 我的81! 整活金卡：81 费全体 81 伤，保留一次永久 -1 费。 */
public class JjMy81 extends AbstractJingjueCard {
    public static final String ID = makeID("JjMy81");
    public static final int BASE_COST = 81;

    public JjMy81() {
        super(ID, BASE_COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 81;
        isMultiDamage = true;
        selfRetain = true;
        baseMagicNumber = magicNumber = 1;
        applyGrownCost();
    }

    @Override
    public void applyPowers() {
        applyGrownCost();
        super.applyPowers();
    }

    @Override
    public void onRetained() {
        int step = Math.max(1, magicNumber);
        if (misc < BASE_COST) {
            misc = Math.min(BASE_COST, misc + step);
            applyGrownCost();
            superFlash();
            syncMasterDeck();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        jjHitAll(AbstractGameAction.AttackEffect.SMASH);
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        if (copy instanceof JjMy81) {
            ((JjMy81) copy).applyGrownCost();
        }
        return copy;
    }

    void applyGrownCost() {
        int grown = Math.max(0, BASE_COST - misc);
        this.cost = grown;
        this.isCostModified = misc > 0;
        if (!this.freeToPlayOnce && !this.isCostModifiedForTurn) {
            this.costForTurn = grown;
        }
    }

    private void syncMasterDeck() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid) && c instanceof JjMy81) {
                c.misc = this.misc;
                ((JjMy81) c).applyGrownCost();
            }
        }
    }
}
