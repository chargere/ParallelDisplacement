package yourmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import yourmod.ComingSoonTimeCharacter;

/** 战斗内的七条连携法则图鉴入口与面板。 */
public final class ComboRulesUI {
    private static final Texture PIXEL = ImageMaster.WHITE_SQUARE_IMG;
    private static final Hitbox buttonHb = new Hitbox(112.0F, 42.0F);
    private static boolean open;

    private static final String[][] RULES = {
            {"攻 · 攻 · 攻", "连星", "全体伤害 5，星念 +1"},
            {"攻 · 攻 · 技", "回环", "能量 +1，星念 +2"},
            {"攻 · 技 · 攻", "溯流", "格挡 +6，目标伤害 5"},
            {"技 · 攻 · 技", "错位", "易伤 1、虚弱 1，星念 +1"},
            {"技 · 技 · 技", "凝滞", "抽 1 张牌，星念 +3"},
            {"技 · 技 · 攻", "并流", "格挡 +5，星念 +2"},
            {"攻 · 能 · 攻", "相续", "目标伤害 8，星念 +1"}
    };

    private ComboRulesUI() {
    }

    public static void update() {
        if (!active()) {
            open = false;
            return;
        }
        layoutButton();
        buttonHb.update();
        if (InputHelper.justClickedLeft && buttonHb.hovered) {
            buttonHb.clickStarted = true;
        }
        if (buttonHb.clicked) {
            buttonHb.clicked = false;
            InputHelper.justClickedLeft = false;
            open = !open;
            CardCrawlGame.sound.play("UI_CLICK_1");
        } else if (open && InputHelper.justClickedLeft && !buttonHb.hovered) {
            open = false;
            InputHelper.justClickedLeft = false;
        }
    }

    public static void render(SpriteBatch sb) {
        if (!active()) return;
        layoutButton();
        renderButton(sb);
        if (open) renderPanel(sb);
    }

    private static boolean active() {
        AbstractPlayer p = AbstractDungeon.player;
        return p instanceof ComingSoonTimeCharacter && Wiz.isInCombat();
    }

    private static void layoutButton() {
        float scale = Settings.scale;
        float x = Settings.WIDTH * 0.5F + 205.0F * scale;
        float y = Settings.HEIGHT * 0.61F;
        buttonHb.move(x, y);
    }

    private static void renderButton(SpriteBatch sb) {
        float scale = Settings.scale;
        float alpha = buttonHb.hovered ? 0.92F : 0.72F;
        sb.setColor(new Color(0.08F, 0.16F, 0.28F, alpha));
        sb.draw(PIXEL, buttonHb.x, buttonHb.y, buttonHb.width, buttonHb.height);
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, "法则",
                buttonHb.cX, buttonHb.cY + 2.0F * scale,
                new Color(0.82F, 0.95F, 1.0F, 1.0F));
        sb.setColor(Color.WHITE);
    }

    private static void renderPanel(SpriteBatch sb) {
        float scale = Settings.scale;
        float panelX = 112.0F * scale;
        float panelY = 78.0F * scale;
        float panelW = Settings.WIDTH - 224.0F * scale;
        float panelH = Settings.HEIGHT - 156.0F * scale;

        // 背景只压暗，不把战场完全盖黑，保留时间旅人的青紫色氛围。
        sb.setColor(new Color(0.01F, 0.025F, 0.075F, 0.58F));
        sb.draw(PIXEL, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(new Color(0.025F, 0.075F, 0.145F, 0.96F));
        sb.draw(PIXEL, panelX, panelY, panelW, panelH);
        drawBorder(sb, panelX, panelY, panelW, panelH, scale);

        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, "时间法则",
                Settings.WIDTH * 0.5F, panelY + panelH - 42.0F * scale,
                new Color(0.86F, 0.97F, 1.0F, 1.0F));
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, "将牌型排列成三拍，触发对应的时间法则",
                Settings.WIDTH * 0.5F, panelY + panelH - 72.0F * scale,
                new Color(0.65F, 0.80F, 0.94F, 1.0F));

        float gap = 18.0F * scale;
        float cardW = (panelW - 3.0F * gap) * 0.5F;
        float cardH = 82.0F * scale;
        float leftX = panelX + gap;
        float rightX = panelX + panelW * 0.5F + gap * 0.5F;
        float topY = panelY + panelH - 142.0F * scale;
        for (int i = 0; i < RULES.length; i++) {
            int column = i < 4 ? 0 : 1;
            int row = column == 0 ? i : i - 4;
            float x = column == 0 ? leftX : rightX;
            float y = topY - row * (cardH + gap);
            drawRuleCard(sb, RULES[i], x, y, cardW, cardH, scale);
        }
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, "点击空白处关闭",
                Settings.WIDTH * 0.5F, panelY + 26.0F * scale,
                new Color(0.58F, 0.70F, 0.85F, 1.0F));
        sb.setColor(Color.WHITE);
    }

    private static void drawRuleCard(SpriteBatch sb, String[] rule, float x, float y,
                                     float width, float height, float scale) {
        sb.setColor(new Color(0.035F, 0.12F, 0.20F, 0.96F));
        sb.draw(PIXEL, x, y, width, height);
        sb.setColor(new Color(0.22F, 0.60F, 0.82F, 0.72F));
        sb.draw(PIXEL, x, y, 4.0F * scale, height);

        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, rule[0],
                x + 22.0F * scale, y + height - 27.0F * scale,
                new Color(0.72F, 0.91F, 1.0F, 1.0F));
        FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, rule[1],
                x + 22.0F * scale, y + 31.0F * scale,
                new Color(0.96F, 0.80F, 1.0F, 1.0F));
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, rule[2],
                x + 132.0F * scale, y + 33.0F * scale,
                new Color(0.84F, 0.92F, 0.98F, 1.0F));
    }

    private static void drawBorder(SpriteBatch sb, float x, float y, float width, float height, float scale) {
        Color border = new Color(0.32F, 0.74F, 0.92F, 0.82F);
        sb.setColor(border);
        sb.draw(PIXEL, x, y + height - 2.0F * scale, width, 2.0F * scale);
        sb.draw(PIXEL, x, y, width, 2.0F * scale);
        sb.draw(PIXEL, x, y, 2.0F * scale, height);
        sb.draw(PIXEL, x + width - 2.0F * scale, y, 2.0F * scale, height);
        sb.setColor(Color.WHITE);
    }
}
