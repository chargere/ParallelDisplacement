package yourmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.VeilStaticPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;

/**
 * 帷幕（1费）
 * 升级前：6 格挡；弃至少1张；获得等量帷幕
 * 升级后：9 格挡
 */
public class Veil extends AbstractEasyCard {
    public final static String ID = makeID("Veil");

    public Veil() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new SelectCardsInHandAction(99, "弃置", true, false, c -> true, cards -> {
            int n = cards.size();
            for (AbstractCard c : cards) {
                att(new DiscardSpecificCardAction(c));
            }
            if (n > 0) {
                att(new ApplyPowerAction(p, p, new VeilStaticPower(p, n), n));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
