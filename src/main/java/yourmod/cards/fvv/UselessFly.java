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
public class UselessFly extends AbstractFvvCard {
    public static final String ID = makeID("UselessFly");
    public UselessFly() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 15;
        baseSecondMagic = secondMagic = 20;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int u = UselessPower.getAmount(p);
        if (u >= magicNumber) {
            applyToSelf(new UselessAscendPower(p, 2));
        } else {
            int doubled = Math.min(secondMagic, u * 2);
            int need = doubled - u;
            if (need > 0) UselessPower.gainSilent(p, need);
            else if (doubled < u) UselessPower.spend(p, u - doubled);
        }
    }
    public void upp() {
        upgradeMagicNumber(-3);
        upgradeSecondMagic(5);
    }
}