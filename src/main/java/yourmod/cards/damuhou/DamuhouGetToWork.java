package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 动手！ */
public class DamuhouGetToWork extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouGetToWork");

    public DamuhouGetToWork() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
        tags.add(AbstractCard.CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamuhouCombatState.addThisTurnNextCombo(magicNumber);
        DamuhouCombatState.nextAttackHealOnKill = secondMagic;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(2);
    }
}
