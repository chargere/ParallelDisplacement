# -*- coding: utf-8 -*-
"""怜悯の火(CompassionFire) = 银白金折刀原色；无人怜悯(NoMercy) = 红色异色加强版+红色流光。
从 raw_CompassionFire.png（已抠图）生成两张遗物主图+outline，归框 0.55。
"""
import os, numpy as np, cv2
from PIL import Image

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
REL = os.path.join(ROOT, "src", "main", "resources", "hongwenResources", "images", "relics")
PREV = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"
TARGET = 0.55
PAD = 4

raw = np.asarray(Image.open(os.path.join(ROOT, r"tmp_relic_drafts\raw_CompassionFire.png")).convert("RGBA")).copy()
alpha = raw[:, :, 3]
M = (alpha > 128).astype(np.uint8)


def frame_55(img_rgba):
    """主体 bbox 归框到 0.55 居中，128x128"""
    a = np.asarray(img_rgba)[:, :, 3]
    ys, xs = np.where(a > 40)
    bb = (max(0, xs.min() - PAD), max(0, ys.min() - PAD),
          min(img_rgba.width, xs.max() + 1 + PAD), min(img_rgba.height, ys.max() + 1 + PAD))
    W = bb[2] - bb[0]; H = bb[3] - bb[1]; maxd = max(W, H)
    sc = TARGET * 128.0 / maxd
    nw, nh = int(round(W * sc)), int(round(H * sc))
    c = img_rgba.crop(bb).resize((nw, nh), Image.LANCZOS)
    canvas = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
    canvas.paste(c, ((128 - nw) // 2, (128 - nh) // 2), c)
    return canvas, bb, (nw, nh)


def save_with_outline(img128, base):
    a = np.asarray(img128)[:, :, 3]
    m = (a > 30).astype(np.uint8) * 255
    cnts, _ = cv2.findContours(m, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    ol = np.zeros((128, 128, 4), np.uint8)
    cv2.drawContours(ol, cnts, -1, (255, 255, 255, 255), 2, cv2.LINE_AA)
    img128.save(os.path.join(REL, base + ".png"))
    Image.fromarray(ol).save(os.path.join(REL, base + "Outline.png"))


# ---------- 1. CompassionFire：原色 ----------
cf = Image.fromarray(raw)
cf128, cfbb, cfsz = frame_55(cf)
save_with_outline(cf128, "CompassionFire")
print("CompassionFire bbox", cfbb, "size", cfsz)

# ---------- 2. NoMercy：红色异色 + 流光 ----------
hsv = cv2.cvtColor(raw[:, :, :3], cv2.COLOR_RGB2HSV).astype(np.float32)
H, S, V = hsv[:, :, 0], hsv[:, :, 1], hsv[:, :, 2]
rgb = raw[:, :, :3].astype(np.float32)

# 染红：非暗黑区全部转红；暗部保留黑
red = rgb.copy()
h2 = np.full_like(H, 3.0)          # 红
s2 = np.clip(200 + (V - 128) * 0.3, 190, 250)   # 高饱和
v2 = np.clip(V * 1.02, 0, 255)      # 保持明暗层次
red_hsv = np.dstack([h2, s2, v2])
red = cv2.cvtColor(red_hsv.astype(np.uint8), cv2.COLOR_HSV2RGB).astype(np.float32)
dark = (V < 75) & (S < 120)
red[dark] = rgb[dark] * 0.55        # 黑柄压暗保留

# 流光：沿刀轴(左下刀尖->右上柄)的投影亮带
ys, xs = np.where(M > 0)
d = np.diag([1, -1]).astype(np.float32)
proj = (xs * 1.0 + (raw.shape[0] - ys)) / (raw.shape[0] + raw.shape[1])  # 0..1, 小=刀尖
flow = np.zeros_like(proj)
for c0, wd, amp in [(0.30, 0.05, 1.0), (0.55, 0.035, 0.8), (0.78, 0.025, 0.6)]:
    flow += amp * np.exp(-((proj - c0) ** 2) / (2 * wd ** 2))
fm_full = np.zeros(raw.shape[:2], np.float32)
fm_full[ys, xs] = np.clip(flow, 0, 1)
fm_full = cv2.GaussianBlur(fm_full, (0, 0), 3) * 1.1
fm = np.clip(fm_full, 0, 1) * M       # 只在刀身内
# 亮红流光叠加（additive）
glowc = np.dstack([255 * fm, 60 * fm, 90 * fm])
red = np.clip(red + glowc * 0.9, 0, 255)
# 流光核心白线（最亮处）
core = np.clip(fm - 0.75, 0, 1) / 0.25
red = np.clip(red + np.dstack([255 * core, 220 * core, 225 * core]) * 0.8, 0, 255)

# 外圈红色 glow（能量感）
k1 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (9, 9))
k2 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (21, 21))
g1 = cv2.dilate(M, k1) - M
g2 = cv2.dilate(M, k2) - cv2.dilate(M, k1)
g1b = cv2.GaussianBlur(g1.astype(np.float32), (0, 0), 2)
g2b = cv2.GaussianBlur(g2.astype(np.float32), (0, 0), 4)
out = red.copy()
oa = alpha.astype(np.float32) / 255
glow_a = np.clip(g1b * 0.75 + g2b * 0.40, 0, 1)
# premultiply 合成：先 glow 后主体
bg = 1 - glow_a
out_rgb = out * oa[:, :, None] * (1 - glow_a[:, :, None] * 0)  # 占位
# 简化：直接组合 RGBA，glow 用独立 alpha 层
final_a = np.clip(alpha + glow_a * 255 * 0.85, 0, 255).astype(np.uint8)
# glow 像素颜色（在主体外）：红
glow_only = (alpha < 200)
out[glow_only] = np.dstack([255 * np.ones(glow_only.sum()), 45 * np.ones(glow_only.sum()), 70 * np.ones(glow_only.sum())])
nm_rgba = np.dstack([out.astype(np.uint8), final_a])

nm128, nmbb, nmsz = frame_55(Image.fromarray(nm_rgba))
save_with_outline(nm128, "NoMercy")
print("NoMercy bbox", nmbb, "size", nmsz)

# ---------- 3. 验收图：两张 x (棋盘/深卡底) ----------
def checker(sz, n=8):
    c = np.zeros((sz, sz, 3), np.uint8)
    for i in range(n):
        for j in range(n):
            v = 200 if (i + j) % 2 == 0 else 150
            c[i * sz // n:(i + 1) * sz // n, j * sz // n:(j + 1) * sz // n] = v
    return Image.fromarray(c)

tiles = []
for img in [cf128, nm128]:
    for bgm in [checker(200), Image.new("RGB", (200, 200), (28, 26, 34))]:
        cell = bgm.copy()
        big = img.resize((200, 200), Image.LANCZOS)
        cell.paste(big, (0, 0), big)
        tiles.append(cell)
sheet = Image.new("RGB", (200 * 4 + 30, 230), (15, 15, 15))
dd = ImageDraw = __import__("PIL.ImageDraw", fromlist=["Draw"]).Draw(sheet)
labels = ["CF checker", "CF dark", "NM checker", "NM dark"]
for i, t in enumerate(tiles):
    x = 10 + i * (200 + 10)
    sheet.paste(t, (x, 20))
    dd.text((x, 4), labels[i], fill=(170, 170, 170))
sheet.save(os.path.join(PREV, "mercy_pair.png"))
print("preview saved")
