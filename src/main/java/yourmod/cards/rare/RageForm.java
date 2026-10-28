package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.derived.TripleAngerStatus;
import yourmod.powers.RageFormPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 牢郑形态（2费，升级不降费）
 * 升级前：回合开始将「愤怒愤怒愤怒」×层数加入手牌（1→5）
 * 升级后：改为加入「愤怒愤怒愤怒+」（1 能量，抽 2）
 */
public class RageForm extends AbstractEasyCard {
    public final static String ID = makeID("RageForm");

    public RageForm() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        cardsToPreview = new TripleAngerStatus();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new RageFormPower(p, 1, upgraded));
    }

    @Override
    public void upp() {
        if (cardsToPreview != null) {
            cardsToPreview.upgrade();
        }
        uDesc();
    }
}
