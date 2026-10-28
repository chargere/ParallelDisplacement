package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
public class JjTimePause extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimePause");
    public JjTimePause() { super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.keepResources = true; addToBot(new DrawCardAction(p, 2)); }
    public void upp() { upgradeBaseCost(1); }
}
