package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.TimeComboTracker;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 异位：起始技能（1 费，5/7 格挡）。
 * 连携：若上一张牌也是技能，额外获得 3 点格挡。
 * 命名回调 Mod 名「平行异位」。
 */
public class YiWei extends AbstractEasyCard {
    public static final String ID = makeID("YiWei");

    public YiWei() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        baseBlock = 5;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int b = block;
        if (TimeComboTracker.lastWasSkill) b += 3;
        Wiz.atb(new GainBlockAction(p, p, b));
        TimeComboTracker.recordSkill();
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
