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
public class JjIceFinale extends AbstractJingjueCard {
    public static final String ID = makeID("JjIceFinale");
    public JjIceFinale() { super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY); baseBlock = 20; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); Wiz.forAllMonstersLiving(mo -> { addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, 2, false), 2)); addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, upgraded ? 2 : 1, false), upgraded ? 2 : 1)); }); }
    public void upp() { upgradeBlock(6); }
}
