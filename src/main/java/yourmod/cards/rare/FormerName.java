package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/** 旧日之名：未打过能力则伤害翻倍并回血；消耗。升级后固有。 */
public class FormerName extends AbstractEasyCard {
    public final static String ID = makeID("FormerName");

    public FormerName() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 6;
        exhaust = true;
        tags.add(CardTags.HEALING);
    }

    private boolean usedPowerThisCombat() {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (c.type == CardType.POWER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean clean = !usedPowerThisCombat();
        // cardsPlayedThisCombat already includes this card if it's not POWER — OK
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (clean) {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            atb(new HealAction(p, p, magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = !usedPowerThisCombat() ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        isInnate = true;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
