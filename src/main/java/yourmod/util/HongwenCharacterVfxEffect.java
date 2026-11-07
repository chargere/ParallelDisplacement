package yourmod.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import yourmod.CharacterFile;

/** 让鸿文君主特效在战场绘制完成后叠加，避免被场景覆盖。 */
public class HongwenCharacterVfxEffect extends AbstractGameEffect {
    public HongwenCharacterVfxEffect() {
        duration = -1.0F;
        isDone = false;
    }

    @Override
    public void update() {
        if (!(AbstractDungeon.player instanceof CharacterFile) || !Wiz.isInCombat()) {
            isDone = true;
            HongwenCharacterVfx.finishEffect();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.player instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            HongwenCharacterVfx.render(
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) AbstractDungeon.player, sb);
        }
    }

    @Override
    public void dispose() {
    }
}
