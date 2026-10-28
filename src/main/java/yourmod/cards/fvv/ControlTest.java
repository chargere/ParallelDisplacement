package yourmod.cards.fvv;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction;
import yourmod.FvvEggCharacter;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;
public class ControlTest extends AbstractFvvCard {
    public static final String ID = makeID("ControlTest");
    public ControlTest() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        int extra = UselessPower.getEffective(p) * magicNumber;
        if (extra > 0) {
            AbstractMonster lowest = null;
            for (AbstractMonster mo : getEnemies()) {
                if (lowest == null || mo.currentHealth < lowest.currentHealth) lowest = mo;
            }
            if (lowest != null) {
                atb(new DamageAction(lowest, new DamageInfo(p, extra, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}