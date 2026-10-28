package yourmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.ComingSoonTimeCharacter;

import static yourmod.ModFile.makeID;

/**
 * 风色x落陨：时间旅人初始遗物（先古遗物）。
 * 稀有度采用 SPECIAL（名染紫，呼应"循环2 紫色"），效果敬请期待。
 * 真名"风色x落陨"保留用户原始命名风格。
 */
public class WindHueMeteorFall extends AbstractEasyRelic {
    public static final String ID = makeID("WindHueMeteorFall");

    public WindHueMeteorFall() {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL,
                ComingSoonTimeCharacter.Enums.TIME_COLOR);
    }
}
