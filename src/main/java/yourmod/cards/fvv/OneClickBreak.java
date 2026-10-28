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
public class OneClickBreak extends AbstractFvvCard {
    public static final String ID = makeID("OneClickBreak");
    public OneClickBreak() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 3;
        isMultiDamage = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spent = UselessPower.spendAll(p);
        if (spent < 1) return;
        for (int i = 0; i < spent; i++) {
            allDmg(AbstractGameAction.AttackEffect.FIRE);
        }
    }
    public void applyPowers() {
        int n = Math.max(1, UselessPower.getAmount(AbstractDungeon.player));
        // preview one wave
        super.applyPowers();
    }
    public void upp() { upgradeDamage(1); }
}