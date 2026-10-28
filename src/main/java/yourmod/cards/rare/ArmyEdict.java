package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ConductAmpPower;
import yourmod.powers.StaticMarkPower;
import yourmod.util.HongwenHooks;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/** 万军之敕：全体静电；本回合传导+50%；传导≥3次则费用0。 */
public class ArmyEdict extends AbstractEasyCard {
    public final static String ID = makeID("ArmyEdict");

    public ArmyEdict() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 50;
        exhaust = true;
    }

    private void updateCostFromConduct() {
        if (HongwenHooks.conductCountThisCombat >= 3) {
            setCostForTurn(0);
        }
    }

    @Override
    public void applyPowers() {
        updateCostFromConduct();
        super.applyPowers();
    }

    @Override
    public void triggerWhenDrawn() {
        updateCostFromConduct();
    }

    @Override
    public void triggerOnOtherCardPlayed(com.megacrit.cardcrawl.cards.AbstractCard c) {
        updateCostFromConduct();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mo ->
                atb(new ApplyPowerAction(mo, p, new StaticMarkPower(mo, magicNumber), magicNumber)));
        atb(new ApplyPowerAction(p, p, new ConductAmpPower(p, secondMagic), secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(25);
    }
}
