package yourmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.util.JingjueDesc;
import yourmod.util.TexLoader;

import static yourmod.ModFile.makeRelicPath;
import static yourmod.ModFile.modID;

/**
 * 遗物基类：按 ID 自动加载贴图与描边；color 非空则归入该角色卡池。
 */
public abstract class AbstractEasyRelic extends CustomRelic {
    /** 所属卡牌颜色；null 表示全角色共享 */
    public AbstractCard.CardColor color;

    public AbstractEasyRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        this(setId, tier, sfx, null);
    }

    public AbstractEasyRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color) {
        super(setId, TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + ".png")), tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + "Outline.png"));
        this.color = color;
    }

    public String getUpdatedDescription() {
        return JingjueDesc.isolate(DESCRIPTIONS[0]);
    }
}