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
public class TheoryCorrect extends AbstractFvvCard {
    public static final String ID = makeID("TheoryCorrect");
    public TheoryCorrect() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 10;
        isInnate = true;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasAtk = false;
        for (AbstractCard c : p.drawPile.group) if (c.type == CardType.ATTACK) { hasAtk = true; break; }
        if (!hasAtk) for (AbstractCard c : p.discardPile.group) if (c.type == CardType.ATTACK) { hasAtk = true; break; }
        if (!hasAtk) UselessPower.gain(p, p, magicNumber);
        else {
            UselessPower.gain(p, p, 2);
            atb(new DrawCardAction(1));
        }
    }
    public void upp() { upgradeMagicNumber(2); }
}