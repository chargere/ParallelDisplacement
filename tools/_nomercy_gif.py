# -*- coding: utf-8 -*-
"""无人怜悯(NoMercy) 红色异色 + 流光动图(gif)。流光沿刀轴来回扫动 + 外圈红晕脉动。
用于效果预览；StS 遗物图标本体仍为静态 png(见 _mercy_pair.py)。"""
import os, numpy as np, cv2
from PIL import Image

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
PREV = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"
TARGET = 0.55
PAD = 4
N = 28
OUT = 160  # gif 边长

raw = np.asarray(Image.open(os.path.join(ROOT, r"tmp_relic_drafts\raw_CompassionFire.png")).convert("RGBA")).copy()
alpha = raw[:, :, 3]
M = (alpha > 128).astype(np.uint8)
rgb = raw[:, :, :3].astype(np.float32)

# 红色异色底（静止）
hsv = cv2.cvtColor(raw[:, :, :3], cv2.COLOR_RGB2HSV).astype(np.float32)
H, S, V = hsv[:, :, 0], hsv[:, :, 1], hsv[:, :, 2]
h2 = np.full_like(H, 3.0)
s2 = np.clip(195 + (V - 128) * 0.3, 185, 250)
v2 = np.clip(V * 1.02, 0, 255)
red = cv2.cvtColor(np.dstack([h2, s2, v2]).astype(np.uint8), cv2.COLOR_HSV2RGB).astype(np.float32)
dark = (V < 75) & (S < 120)
red[dark] = rgb[dark] * 0.55

# 轴投影（主体像素）
ys, xs = np.where(M > 0)
proj = (xs * 1.0 + (raw.shape[0] - ys)) / (raw.shape[0] + raw.shape[1])

# 外圈红晕（静止）
k1 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (9, 9))
k2 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (21, 21))
g1 = cv2.dilate(M, k1) - M
g2 = cv2.dilate(M, k2) - cv2.dilate(M, k1)
g1b = cv2.GaussianBlur(g1.astype(np.float32), (0, 0), 2)
g2b = cv2.GaussianBlur(g2.astype(np.float32), (0, 0), 4)
glow_a = np.clip(g1b * 0.7 + g2b * 0.38, 0, 1)

def frame_128(out_rgb):
    out_rgba = np.dstack([out_rgb.astype(np.uint8),
                          np.clip(alpha + glow_a * 255 * 0.85, 0, 255).astype(np.uint8)])
    im = Image.fromarray(out_rgba)
    a = np.asarray(im)[:, :, 3]
    yx, xx = np.where(a > 40)
    bb = (max(0, xx.min() - PAD), max(0, yx.min() - PAD),
          min(im.width, xx.max() + 1 + PAD), min(im.height, yx.max() + 1 + PAD))
    W = bb[2] - bb[0]; Hh = bb[3] - bb[1]; maxd = max(W, Hh)
    sc = TARGET * OUT / maxd
    nw, nh = int(round(W * sc)), int(round(Hh * sc))
    c = im.crop(bb).resize((nw, nh), Image.LANCZOS)
    canvas = Image.new("RGBA", (OUT, OUT), (0, 0, 0, 0))
    canvas.paste(c, ((OUT - nw) // 2, (OUT - nh) // 2), c)
    return canvas

frames = []
for i in range(N):
    ph = i / N
    # 主光带位置：0.18<->0.82 来回 (三角波)
    tri = 1 - abs((ph * 2) % 2 - 1)        # 0..1..0
    pos = 0.18 + 0.64 * tri
    flow = (1.4 * np.exp(-((proj - pos) ** 2) / (2 * 0.045 ** 2))
            + 0.7 * np.exp(-((proj - (pos - 0.18)) ** 2) / (2 * 0.03 ** 2)))
    fm_full = np.zeros(raw.shape[:2], np.float32)
    fm_full[ys, xs] = np.clip(flow, 0, 1)
    fm_full = cv2.GaussianBlur(fm_full, (0, 0), 3) * 1.05
    fm = np.clip(fm_full, 0, 1) * M
    out = red.copy()
    out += np.dstack([255 * fm, 60 * fm, 90 * fm]) * 0.95        # 红色流光
    core = np.clip(fm - 0.72, 0, 1) / 0.28
    out += np.dstack([255 * core, 220 * core, 230 * core]) * 0.85  # 白核
    # 外晕脉动
    pulse = 0.82 + 0.18 * np.sin(ph * 2 * np.pi)
    out_glow = out.copy()
    gl = glow_a * pulse
    out_glow[(alpha < 200)] = np.dstack([255 * gl[(alpha < 200)], 45 * gl[(alpha < 200)], 70 * gl[(alpha < 200)]])
    canvas = frame_128(out_glow)
    # 保留透明：量化后把 alpha<=128 区域映射到 index 255 作为透明色
    q = canvas.convert("RGB").quantize(colors=255, method=Image.FASTOCTREE, dither=Image.NONE)
    mask = np.asarray(canvas)[:, :, 3] <= 128
    pal = q.load()
    yy, xx = np.where(mask)
    for y, x in zip(yy.tolist(), xx.tolist()):
        pal[x, y] = 255
    q.info["transparency"] = 255
    frames.append(q)

frames[0].save(os.path.join(PREV, "NoMercy_flow.gif"),
               save_all=True, append_images=frames[1:], duration=70, loop=0,
               disposal=2, transparency=255)
print("gif saved, frames", len(frames))
