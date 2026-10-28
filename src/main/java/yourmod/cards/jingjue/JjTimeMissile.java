package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
public class JjTimeMissile extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeMissile");
    public JjTimeMissile() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (!JingjueCombatState.consumeHourglass(2)) return; int d = (JingjueCombatState.hourglass() + 2) * magicNumber; addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(d, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)); }
    public void upp() { upgradeMagicNumber(1); }
}
