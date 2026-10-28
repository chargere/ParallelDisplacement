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
public class PlacentaAscend extends AbstractFvvCard {
    public static final String ID = makeID("PlacentaAscend");
    public PlacentaAscend() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 4;
        isMultiDamage = true;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int layers = UselessPower.spendAll(p);
        layers += exhaustSlack(p.hand);
        layers += exhaustSlack(p.drawPile);
        layers += exhaustSlack(p.discardPile);
        for (int i = 0; i < layers; i++) {
            allDmg(AbstractGameAction.AttackEffect.FIRE);
        }
        if (!upgraded) applyToSelf(new SkipNextTurnPower(p));
    }
    private int exhaustSlack(com.megacrit.cardcrawl.cards.CardGroup g) {
        ArrayList<AbstractCard> rem = new ArrayList<>();
        for (AbstractCard c : g.group) {
            if (c instanceof SlackOff || c instanceof SlackOffHard) rem.add(c);
        }
        for (AbstractCard c : rem) {
            g.moveToExhaustPile(c);
        }
        return rem.size();
    }
    public void upp() {
        upgradeDamage(1);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}