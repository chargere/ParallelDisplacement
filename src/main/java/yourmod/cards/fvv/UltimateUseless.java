package yourmod.cards.fvv;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.FvvEggCharacter;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;
import java.util.Map;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;
public class UltimateUseless extends AbstractFvvCard {
    public static final String ID = makeID("UltimateUseless");
    public UltimateUseless() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 0;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int u = UselessPower.spendAll(p);
        baseDamage = Math.min(99, u * u);
        calculateCardDamage(m);
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
        baseDamage = 0;
    }
    public void applyPowers() {
        int u = UselessPower.getAmount(AbstractDungeon.player);
        baseDamage = Math.min(99, u * u);
        super.applyPowers();
        isDamageModified = true;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int u = UselessPower.getAmount(AbstractDungeon.player);
        baseDamage = Math.min(99, u * u);
        super.calculateCardDamage(mo);
        isDamageModified = true;
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}