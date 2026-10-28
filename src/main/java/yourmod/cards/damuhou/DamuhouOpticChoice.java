package yourmod.cards.damuhou;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

/**
 * 棱镜瞄具选项（不进卡池）。
 */
@AutoAdd.Ignore
public class DamuhouOpticChoice extends AbstractEasyCard {
    private final Runnable onPick;

    public DamuhouOpticChoice(String id, Runnable onPick) {
        super(id, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        this.onPick = onPick;
    }

    @Override
    public void onChoseThisOption() {
        if (onPick != null) {
            onPick.run();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    @Override
    public void upp() {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DamuhouOpticChoice(cardID, onPick);
    }
}
