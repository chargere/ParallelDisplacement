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
public class RageFinale extends AbstractFvvCard {
    public static final String ID = makeID("RageFinale");
    public RageFinale() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 12;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
    public void applyPowers() {
        int bonus = FvvCombatState.uselessGainedThisCombat;
        int real = baseDamage;
        baseDamage += bonus;
        super.applyPowers();
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int bonus = FvvCombatState.uselessGainedThisCombat;
        int real = baseDamage;
        baseDamage += bonus;
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void upp() {
        upgradeDamage(3);
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}