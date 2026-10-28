package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 灏搁骞?*/
public class JjCorpseScent extends AbstractJingjueCard {
    public static final String ID = makeID("JjCorpseScent");
    public JjCorpseScent() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveImaginary(m, magicNumber); addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
