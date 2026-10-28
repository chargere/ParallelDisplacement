package yourmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

/**
 * 选人界面标题特效：底色 + 灯带连续从左往右扫过（按像素/顶点插值，非整字闪烁）。
 * 鸿文：主标题黄底红灯带；副标题白底红灯带。
 * 多米尼克：主标题蓝底炫彩灯带；副标题青底粉灯带。
 * fvv蛋蛋：主/副标题白底黄流光呼吸灯带。
 * 战斗精英：主/副标题粉底紫流光呼吸灯带。
 * 精绝王者：主/副标题紫黑底紫呼吸灯带。
 * 时间旅人：赛博霓虹乱码；时/人与查/斯成对闪出，闪出时明显减速。
 */
public final class HongwenNameFx {
    /** 主标题：黄底 + 流动红灯带 */
    public static final Color TITLE_RED = new Color(0.95f, 0.28f, 0.08f, 1f);
    public static final Color TITLE_YELLOW = new Color(1f, 0.88f, 0.25f, 1f);
    /** 副标题：白底 + 流动红灯带 */
    public static final Color SUB_RED = new Color(1f, 0.22f, 0.08f, 1f);
    public static final Color SUB_WHITE = new Color(1f, 0.97f, 0.94f, 1f);

    /** 多米尼克主标题：蓝底 + 炫彩呼吸灯带 */
    public static final Color DOMINIC_TITLE_BLUE = new Color(0.28f, 0.52f, 0.95f, 1f);
    /** 多米尼克副标题：青底 + 粉色呼吸灯带 */
    public static final Color DOMINIC_SUB_CYAN = new Color(0.45f, 0.92f, 0.95f, 1f);
    public static final Color DOMINIC_SUB_PINK = new Color(1f, 0.45f, 0.72f, 1f);

    /** fvv蛋蛋：白底 + 黄色流光呼吸灯带 */
    public static final Color FVV_TITLE_WHITE = new Color(1f, 1f, 1f, 1f);
    public static final Color FVV_TITLE_YELLOW = new Color(1f, 0.88f, 0.20f, 1f);

    /** 战斗精英：粉底 + 紫色流光呼吸灯带 */
    public static final Color DAMUHOU_TITLE_PINK = new Color(1f, 0.62f, 0.82f, 1f);
    public static final Color DAMUHOU_TITLE_PURPLE = new Color(0.58f, 0.28f, 0.95f, 1f);

    /** 精绝王者：可见的紫黑底 + 炫彩灯带 */
    public static final Color JINGJUE_TITLE_BLACK = new Color(0.36f, 0.12f, 0.52f, 1f);
    public static final Color JINGJUE_TITLE_PURPLE = new Color(0.82f, 0.38f, 1f, 1f);

    /** 时间旅人：青 / 品红霓虹 */
    public static final Color TIME_CYAN = new Color(0.18f, 0.98f, 1f, 1f);
    public static final Color TIME_MAGENTA = new Color(1f, 0.16f, 0.78f, 1f);
    public static final Color TIME_WHITE = new Color(0.95f, 0.98f, 1f, 1f);

    private static final Color TMP = new Color();
    private static final Color BAND_TMP = new Color();

    /** 灯带完整扫过一次约 2.8s */
    private static final float BAND_PERIOD_MS = 2800f;
    /** 炫彩色相完整轮转约 2.2s */
    private static final float RAINBOW_PERIOD_MS = 2200f;
    /** 灯带半宽相对整段文字宽度 */
    private static final float BAND_HALF_RATIO = 0.32f;
    /** 灯带半宽下限（像素），避免短字串过窄 */
    private static final float BAND_HALF_MIN_PX = 36f;
    /** 火控字更短：窄灯带才看得出扫过 */
    private static final float FIRE_BAND_HALF_RATIO = 0.14f;
    private static final float FIRE_BAND_HALF_MIN_PX = 10f;
    /** 嵌在字母里的小星，对准开口较大的字母 */
    private static final float[] STAR_FRACS = {0.20f, 0.36f, 0.51f, 0.64f, 0.78f, 0.91f};
    private static final GlyphLayout STAR_GL = new GlyphLayout();
    private static final Color STAR_COLOR = new Color();
    private static final Color HALF_BASE_TMP = new Color();
    private static final Color TIME_GLYPH_TMP = new Color();
    private static final Color TIME_SPLIT_CYAN = new Color(0.18f, 0.98f, 1f, 0.55f);
    private static final Color TIME_SPLIT_MAGENTA = new Color(1f, 0.16f, 0.78f, 0.55f);

    /** 全角/汉字池：宽度接近中文名，尖塔字库吃得下 */
    private static final String[] TIME_GLITCH_POOL = {
            "ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ",
            "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
            "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ",
            "０", "１", "７", "８", "Ｘ", "Ｚ", "＃", "％", "＠", "※",
            "日", "目", "口", "田", "山", "火", "水", "木", "甲", "乙",
            "丙", "丁", "未", "无", "末", "永", "空", "光", "影", "零"
    };
    private static final long TIME_GLITCH_FAST_MS = 70L;
    private static final long TIME_GLITCH_SLOW_MS = 140L;
    /** 约 4 秒成对闪出一次，减速停约 1.5 秒；慢跳 140ms，别拖成定格 */
    private static final int TIME_PAIR_PERIOD_FRAMES = 240;
    private static final int TIME_PAIR_HOLD_FRAMES = 90;
    private static final String TIME_PAIR_FORBIDDEN = "时间旅人查斯";
    private static String timeTitleOut = "";
    private static long timeTitleAt = 0L;
    private static boolean timeTitlePairCached = false;
    private static String timeTrueOut = "";
    private static long timeTrueAt = 0L;
    private static boolean timeTruePairCached = false;

    private HongwenNameFx() {
    }

    /** 主标题：黄底，红色灯带从左往右流动 */
    public static void renderBreathingGradientName(SpriteBatch sb, BitmapFont font, String text,
                                                   float x, float y, float lineWidth, float lineSpacing) {
        renderFlowingBand(sb, font, text, x, y, 1f, TITLE_YELLOW, TITLE_RED, false);
    }

    public static void renderBreathingGradientName(SpriteBatch sb, BitmapFont font, String text,
                                                   float x, float y, float lineWidth, float lineSpacing,
                                                   float scale) {
        renderFlowingBand(sb, font, text, x, y, scale, TITLE_YELLOW, TITLE_RED, false);
    }

    /** 副标题：白底，红色灯带从左往右流动 */
    public static void renderRedWhiteSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                              float x, float y) {
        renderFlowingBand(sb, font, text, x, y, 1f, SUB_WHITE, SUB_RED, false);
    }

    /** 多米尼克主标题：蓝底，炫彩色灯带呼吸扫过 */
    public static void renderDominicTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing) {
        renderFlowingBand(sb, font, text, x, y, 1f, DOMINIC_TITLE_BLUE, null, true);
    }

    public static void renderDominicTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing,
                                          float scale) {
        renderFlowingBand(sb, font, text, x, y, scale, DOMINIC_TITLE_BLUE, null, true);
    }

    /** 多米尼克副标题：青底，粉色灯带呼吸扫过 */
    public static void renderDominicSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                             float x, float y) {
        renderFlowingBand(sb, font, text, x, y, 1f, DOMINIC_SUB_CYAN, DOMINIC_SUB_PINK, false);
    }

    /** fvv蛋蛋主标题：白底，黄色流光呼吸灯带 */
    public static void renderFvvTitle(SpriteBatch sb, BitmapFont font, String text,
                                      float x, float y, float lineWidth, float lineSpacing) {
        renderFlowingBand(sb, font, text, x, y, 1f, FVV_TITLE_WHITE, breathingYellow(), false);
    }

    public static void renderFvvTitle(SpriteBatch sb, BitmapFont font, String text,
                                      float x, float y, float lineWidth, float lineSpacing,
                                      float scale) {
        renderFlowingBand(sb, font, text, x, y, scale, FVV_TITLE_WHITE, breathingYellow(), false);
    }

    /** fvv蛋蛋副标题：同款白底黄流光 */
    public static void renderFvvSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                         float x, float y) {
        renderFlowingBand(sb, font, text, x, y, 1f, FVV_TITLE_WHITE, breathingYellow(), false);
    }

    /** 战斗精英主标题：粉底，紫色流光呼吸灯带 */
    public static void renderDamuhouTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing) {
        renderFlowingBand(sb, font, text, x, y, 1f, DAMUHOU_TITLE_PINK, breathingPurple(), false);
    }

    public static void renderDamuhouTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing,
                                          float scale) {
        renderFlowingBand(sb, font, text, x, y, scale, DAMUHOU_TITLE_PINK, breathingPurple(), false);
    }

    /** 战斗精英副标题：同款粉底紫流光 */
    public static void renderDamuhouSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                             float x, float y) {
        renderFlowingBand(sb, font, text, x, y, 1f, DAMUHOU_TITLE_PINK, breathingPurple(), false);
    }

    /** 精绝王者主标题：紫黑底，炫彩灯带扫过 */
    public static void renderJingjueTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing) {
        renderFlowingBand(sb, font, text, x, y, 1f, JINGJUE_TITLE_BLACK, null, true);
    }

    public static void renderJingjueTitle(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float lineWidth, float lineSpacing,
                                          float scale) {
        renderFlowingBand(sb, font, text, x, y, scale, JINGJUE_TITLE_BLACK, null, true);
    }

    /** 精绝王者副标题：同款紫黑炫彩 */
    public static void renderJingjueSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                             float x, float y) {
        renderFlowingBand(sb, font, text, x, y, 1f, JINGJUE_TITLE_BLACK, null, true);
    }

    /** 时间旅人主标题：乱码循环，时与人成对闪出 */
    public static void renderTimeTravelerTitle(SpriteBatch sb, BitmapFont font, String text,
                                              float x, float y, float lineWidth, float lineSpacing) {
        renderTimeGlitchName(sb, font, scrambleTitle(text), x, y, 1f);
    }

    public static void renderTimeTravelerTitle(SpriteBatch sb, BitmapFont font, String text,
                                              float x, float y, float lineWidth, float lineSpacing,
                                              float scale) {
        renderTimeGlitchName(sb, font, scrambleTitle(text), x, y, scale);
    }

    /** 时间旅人副标题：点号骨架；查与斯和时/人同一拍闪出 */
    public static void renderTimeTravelerSubtitle(SpriteBatch sb, BitmapFont font, String text,
                                                 float x, float y) {
        renderTimeGlitchName(sb, font, scrambleTrueName(text), x, y, 1f);
    }

    /** 青/品红错位叠字，始终霓虹，不再整词发白。 */
    private static void renderTimeGlitchName(SpriteBatch sb, BitmapFont font, String shown,
                                            float x, float y, float scale) {
        if (sb == null || font == null || shown == null || shown.isEmpty()) {
            return;
        }
        float oldScale = font.getData().scaleX;
        Color oldColor = font.getColor().cpy();
        font.getData().setScale(scale);
        try {
            boolean pair = inPairWindow();
            float split = (pair ? 1.8f : 2.6f) * Settings.scale * scale;
            drawTimeLayer(sb, font, shown, x - split, y, TIME_SPLIT_CYAN);
            drawTimeLayer(sb, font, shown, x + split, y, TIME_SPLIT_MAGENTA);
            drawTimeLayer(sb, font, shown, x, y, TIME_WHITE);
        } finally {
            font.setColor(oldColor);
            font.getData().setScale(oldScale);
        }
    }

    private static void drawTimeLayer(SpriteBatch sb, BitmapFont font, String text,
                                      float x, float y, Color tint) {
        BitmapFontCache cache = font.getCache();
        cache.clear();
        cache.setText(text, x, y);
        long now = System.currentTimeMillis();
        float flickerSpeed = inPairWindow() ? 140f : 90f;

        int pages = font.getRegions().size;
        for (int page = 0; page < pages; page++) {
            float[] v = cache.getVertices(page);
            int n = cache.getVertexCount(page);
            for (int i = 0; i < n; i += 5) {
                if (tint.a < 0.99f) {
                    TIME_GLYPH_TMP.set(tint);
                } else {
                    float flicker = 0.5f + 0.5f * MathUtils.sin(now / flickerSpeed + v[i] * 0.04f);
                    TIME_GLYPH_TMP.set(TIME_CYAN).lerp(TIME_MAGENTA, flicker);
                }
                TIME_GLYPH_TMP.a = tint.a;
                v[i + 2] = TIME_GLYPH_TMP.toFloatBits();
            }
        }
        cache.draw(sb);
    }

    private static boolean inPairWindow() {
        long frame = Gdx.graphics.getFrameId();
        return (frame % TIME_PAIR_PERIOD_FRAMES) < TIME_PAIR_HOLD_FRAMES;
    }

    private static long glitchInterval() {
        return inPairWindow() ? TIME_GLITCH_SLOW_MS : TIME_GLITCH_FAST_MS;
    }

    private static String scrambleTitle(String src) {
        if (src == null || src.isEmpty()) {
            return src;
        }
        boolean pair = inPairWindow();
        long now = System.currentTimeMillis();
        if (timeTitleOut != null && !timeTitleOut.isEmpty()
                && timeTitlePairCached == pair
                && now - timeTitleAt < glitchInterval()) {
            return timeTitleOut;
        }
        int[] cps = src.codePoints().toArray();
        StringBuilder sb = new StringBuilder(src.length() + 8);
        for (int i = 0; i < cps.length; i++) {
            if (pair && i == 0) {
                sb.append('时');
            } else if (pair && i == cps.length - 1) {
                sb.append('人');
            } else {
                sb.append(pickGlitch(TIME_PAIR_FORBIDDEN));
            }
        }
        timeTitleOut = sb.toString();
        timeTitleAt = now;
        timeTitlePairCached = pair;
        return timeTitleOut;
    }

    /** 点号保留；平时全乱码。成对窗口里首字查、末字斯，与时/人同一拍。 */
    private static String scrambleTrueName(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return pattern;
        }
        boolean pair = inPairWindow();
        long now = System.currentTimeMillis();
        if (timeTrueOut != null && !timeTrueOut.isEmpty()
                && timeTruePairCached == pair
                && now - timeTrueAt < glitchInterval()) {
            return timeTrueOut;
        }
        int[] cps = pattern.codePoints().toArray();
        StringBuilder sb = new StringBuilder(pattern.length() + 8);
        for (int i = 0; i < cps.length; i++) {
            int cp = cps[i];
            if (cp == '.' || cp == '·') {
                sb.appendCodePoint(cp);
            } else if (pair && i == 0) {
                sb.append('查');
            } else if (pair && i == cps.length - 1) {
                sb.append('斯');
            } else {
                sb.append(pickGlitch(TIME_PAIR_FORBIDDEN));
            }
        }
        timeTrueOut = sb.toString();
        timeTrueAt = now;
        timeTruePairCached = pair;
        return timeTrueOut;
    }

    private static String pickGlitch(String forbidden) {
        String glyph;
        int guard = 0;
        do {
            glyph = TIME_GLITCH_POOL[MathUtils.random(TIME_GLITCH_POOL.length - 1)];
            guard++;
        } while (guard < 12 && forbidden != null && forbidden.indexOf(glyph) >= 0);
        return glyph;
    }

    /** 半自动底色：红热 */
    public static final Color FIRE_HALF_BASE = new Color(0.92f, 0.16f, 0.10f, 1f);

    /** 火控开关：半自动红热扫光，全自动紫极光扫光。x/y 为左上角。 */
    public static void renderFireModeLabel(SpriteBatch sb, BitmapFont font, String text,
                                           float x, float y, float scale, boolean fullAuto) {
        if (sb == null || font == null || text == null || text.isEmpty()) {
            return;
        }
        float oldScale = font.getData().scaleX;
        Color oldColor = font.getColor().cpy();
        font.getData().setScale(scale);
        try {
            Color outline = new Color(0.04f, 0.02f, 0.08f, 0.92f);
            float o = 2.4f * Settings.scale;
            FontHelper.renderFontLeftTopAligned(sb, font, text, x - o, y, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x + o, y, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x, y - o, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x, y + o, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x - o, y - o, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x + o, y - o, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x - o, y + o, outline);
            FontHelper.renderFontLeftTopAligned(sb, font, text, x + o, y + o, outline);
            if (fullAuto) {
                renderFlowingBand(sb, font, text, x, y, scale, DAMUHOU_TITLE_PINK, breathingPurple(), false,
                        FIRE_BAND_HALF_RATIO, FIRE_BAND_HALF_MIN_PX);
            } else {
                renderFlowingBand(sb, font, text, x, y, scale, pulsingHalfBase(), hotRedBand(), false,
                        FIRE_BAND_HALF_RATIO, FIRE_BAND_HALF_MIN_PX);
            }
            renderInlaidStars(sb, font, text, x, y, fullAuto);
        } finally {
            font.setColor(oldColor);
            font.getData().setScale(oldScale);
        }
    }

    /** 黄色灯带亮度随时间呼吸 */
    private static Color breathingYellow() {
        float glow = 0.88f + 0.12f * MathUtils.sin(System.currentTimeMillis() / 700f);
        BAND_TMP.set(Math.min(1f, FVV_TITLE_YELLOW.r * glow),
                Math.min(1f, FVV_TITLE_YELLOW.g * glow),
                Math.min(1f, FVV_TITLE_YELLOW.b * (0.7f + 0.3f * glow)), 1f);
        return BAND_TMP;
    }

    /** 半自动整词呼吸：暗红 ↔ 亮红 */
    private static Color pulsingHalfBase() {
        float pulse = 0.5f + 0.5f * MathUtils.sin(System.currentTimeMillis() / 340f);
        HALF_BASE_TMP.set(0.32f + 0.63f * pulse, 0.02f + 0.14f * pulse, 0.02f, 1f);
        return HALF_BASE_TMP;
    }

    /** 扫过的热边保持橙白，不跟底一起变暗 */
    private static Color hotRedBand() {
        float shimmer = 0.82f + 0.18f * MathUtils.sin(System.currentTimeMillis() / 220f);
        BAND_TMP.set(1f, 0.62f * shimmer, 0.18f * shimmer, 1f);
        return BAND_TMP;
    }

    /** 紫色灯带亮度随时间呼吸 */
    private static Color breathingPurple() {
        float glow = 0.82f + 0.18f * MathUtils.sin(System.currentTimeMillis() / 640f);
        BAND_TMP.set(Math.min(1f, DAMUHOU_TITLE_PURPLE.r * (0.85f + 0.2f * glow)),
                Math.min(1f, DAMUHOU_TITLE_PURPLE.g * glow),
                Math.min(1f, DAMUHOU_TITLE_PURPLE.b * (0.9f + 0.1f * glow)), 1f);
        return BAND_TMP;
    }

    /** 紫黑灯带：暗紫 ↔ 亮紫 */
    private static Color breathingJingjuePurple() {
        float glow = 0.5f + 0.5f * MathUtils.sin(System.currentTimeMillis() / 580f);
        BAND_TMP.set(
                0.28f + 0.52f * glow,
                0.06f + 0.22f * glow,
                0.42f + 0.58f * glow,
                1f);
        return BAND_TMP;
    }

    /**
     * 用 BitmapFontCache 顶点色做连续扫光：同一字左右可不同色，灯带平滑流过。
     * {@code rainbowBand=true} 时灯带色相随时间/位置轮转（炫彩）。
     */
    private static void renderFlowingBand(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float scale,
                                          Color base, Color band, boolean rainbowBand) {
        renderFlowingBand(sb, font, text, x, y, scale, base, band, rainbowBand,
                BAND_HALF_RATIO, BAND_HALF_MIN_PX);
    }

    private static void renderFlowingBand(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, float scale,
                                          Color base, Color band, boolean rainbowBand,
                                          float halfRatio, float halfMin) {
        if (sb == null || font == null || text == null || text.isEmpty() || base == null) {
            return;
        }
        if (!rainbowBand && band == null) {
            return;
        }

        float oldScale = font.getData().scaleX;
        Color oldColor = font.getColor().cpy();
        font.getData().setScale(scale);

        try {
            BitmapFontCache cache = font.getCache();
            cache.clear();
            GlyphLayout layout = cache.setText(text, x, y);
            float totalW = Math.max(1f, layout.width);
            float bandHalf = bandHalf(totalW, halfRatio, halfMin);
            float bandCenter = bandCenter(totalW, halfRatio, halfMin);
            float hueShift = (System.currentTimeMillis() % (long) RAINBOW_PERIOD_MS) / RAINBOW_PERIOD_MS;

            int pages = font.getRegions().size;
            for (int page = 0; page < pages; page++) {
                float[] v = cache.getVertices(page);
                int n = cache.getVertexCount(page);
                for (int i = 0; i < n; i += 5) {
                    float localX = v[i] - x;
                    float dist = Math.abs(localX - bandCenter);
                    float t = MathUtils.clamp(1f - dist / bandHalf, 0f, 1f);
                    t = t * t * (3f - 2f * t);

                    Color bandColor = band;
                    if (rainbowBand) {
                        float hue = (hueShift + localX / totalW * 0.55f) % 1f;
                        if (hue < 0f) {
                            hue += 1f;
                        }
                        float breath = 0.88f + 0.12f * MathUtils.sin(hueShift * MathUtils.PI2);
                        hsvToRgb(hue, 0.85f, breath, BAND_TMP);
                        bandColor = BAND_TMP;
                    }

                    TMP.set(base).lerp(bandColor, t);
                    TMP.a = 1f;
                    v[i + 2] = TMP.toFloatBits();
                }
            }

            cache.draw(sb);
        } finally {
            font.setColor(oldColor);
            font.getData().setScale(oldScale);
        }
    }

    private static float bandHalf(float totalW, float ratio, float min) {
        return Math.max(min, totalW * ratio);
    }

    /** 相对文字左缘的灯带中心 x */
    private static float bandCenter(float totalW, float ratio, float min) {
        float half = bandHalf(totalW, ratio, min);
        float span = totalW + half * 2f;
        return ((System.currentTimeMillis() % (long) BAND_PERIOD_MS) / BAND_PERIOD_MS) * span - half;
    }

    /**
     * 小星点缀在字母里：高度大约是字高的一小截，不发光晕。
     */
    private static void renderInlaidStars(SpriteBatch sb, BitmapFont font, String text,
                                          float x, float y, boolean fullAuto) {
        TextureAtlas.AtlasRegion star = ImageMaster.TINY_STAR;
        if (star == null) {
            return;
        }
        STAR_GL.setText(font, text);
        float tw = Math.max(1f, STAR_GL.width);
        float th = Math.max(1f, STAR_GL.height);
        float midY = y - th * 0.38f;
        float sweep = bandCenter(tw, FIRE_BAND_HALF_RATIO, FIRE_BAND_HALF_MIN_PX);
        float half = bandHalf(tw, FIRE_BAND_HALF_RATIO, FIRE_BAND_HALF_MIN_PX);
        long now = System.currentTimeMillis();
        float packed = Math.max(1f, star.packedWidth);

        for (int i = 0; i < STAR_FRACS.length; i++) {
            float cx = x + tw * STAR_FRACS[i];
            float cy = midY + MathUtils.sin(i * 2.3f) * th * 0.06f;

            float twinkle = 0.5f + 0.5f * MathUtils.sin(now / (240f + i * 41f) + i * 1.9f);
            twinkle = twinkle * twinkle;
            twinkle = twinkle * twinkle;
            float dist = Math.abs((cx - x) - sweep);
            float catchLight = MathUtils.clamp(1f - dist / Math.max(8f, half), 0f, 1f);
            catchLight = catchLight * catchLight;
            float bright = Math.max(0.35f + 0.65f * twinkle, catchLight);

            Color tint = fullAuto ? DAMUHOU_TITLE_PINK : FIRE_HALF_BASE;
            STAR_COLOR.set(Color.WHITE).lerp(tint, 0.25f);
            STAR_COLOR.a = 0.45f + 0.55f * bright;

            float s = (th * (0.14f + 0.06f * bright)) / packed;
            float w = star.packedWidth;
            float h = star.packedHeight;
            sb.setColor(STAR_COLOR);
            sb.draw(star, cx - w / 2f, cy - h / 2f, w / 2f, h / 2f, w, h, s, s, 0f);
        }
        sb.setColor(Color.WHITE);
    }

    /** hue/sat/val 均在 0~1 */
    private static void hsvToRgb(float h, float s, float v, Color out) {
        float hh = (h % 1f) * 6f;
        if (hh < 0f) {
            hh += 6f;
        }
        int i = (int) hh;
        float f = hh - i;
        float p = v * (1f - s);
        float q = v * (1f - s * f);
        float t = v * (1f - s * (1f - f));
        switch (i) {
            case 0:
                out.set(v, t, p, 1f);
                break;
            case 1:
                out.set(q, v, p, 1f);
                break;
            case 2:
                out.set(p, v, t, 1f);
                break;
            case 3:
                out.set(p, q, v, 1f);
                break;
            case 4:
                out.set(t, p, v, 1f);
                break;
            default:
                out.set(v, p, q, 1f);
                break;
        }
    }
}
