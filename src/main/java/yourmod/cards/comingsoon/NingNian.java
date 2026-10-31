package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.XingNianPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 凝念：起始技能（1 费），获得 2 点星念（升级后 3 点）。
 * 星念是时间旅人的核心资源，用于驱动风色x落陨的形态切换。
 */
public class NingNian extends AbstractEasyCard {
    public static final String ID = makeID("NingNian");

    public NingNian() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF, ComingSoonTimeCharacter.Enums.TIME_COLOR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int gain = upgraded ? 3 : 2;
        Wiz.applyToSelf(new XingNianPower(p, gain));
        ComboSystem.record(p, this); // 把「技」推进全局连携队列
    }

    @Override
    public void upp() {
        // 升级仅改变获得星念数，已在 use 中按 upgraded 区分
    }
}
