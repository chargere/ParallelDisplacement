# -*- coding: utf-8 -*-
"""生成无人怜悯流光帧 NoMercy_00..27.png (128px, 透明, 归框0.55) 到资源目录。"""
import os, numpy as np, cv2
from PIL import Image

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
REL = os.path.join(ROOT, "src", "main", "resources", "hongwenResources", "images", "relics")
TARGET = 0.55
PAD = 4
N = 28
S = 128

raw = np.asarray(Image.open(os.path.join(ROOT, r"tmp_relic_drafts\raw_CompassionFire.png")).convert("RGBA")).copy()
alpha = raw[:, :, 3]
M = (alpha > 128).astype(np.uint8)
rgb = raw[:, :, :3].astype(np.float32)

hsv = cv2.cvtColor(raw[:, :, :3], cv2.COLOR_RGB2HSV).astype(np.float32)
H, S_, V = hsv[:, :, 0], hsv[:, :, 1], hsv[:, :, 2]
h2 = np.full_like(H, 3.0)
s2 = np.clip(195 + (V - 128) * 0.3, 185, 250)
v2 = np.clip(V * 1.02, 0, 255)
red = cv2.cvtColor(np.dstack([h2, s2, v2]).astype(np.uint8), cv2.COLOR_HSV2RGB).astype(np.float32)
dark = (V < 75) & (S_ < 120)
red[dark] = rgb[dark] * 0.55

ys, xs = np.where(M > 0)
proj = (xs * 1.0 + (raw.shape[0] - ys)) / (raw.shape[0] + raw.shape[1])

k1 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (9, 9))
k2 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (21, 21))
g1 = cv2.dilate(M, k1) - M
g2 = cv2.dilate(M, k2) - cv2.dilate(M, k1)
g1b = cv2.GaussianBlur(g1.astype(np.float32), (0, 0), 2)
g2b = cv2.GaussianBlur(g2.astype(np.float32), (0, 0), 4)
glow_a = np.clip(g1b * 0.7 + g2b * 0.38, 0, 1)


def compose_frame(ph, pos):
    flow = (1.4 * np.exp(-((proj - pos) ** 2) / (2 * 0.045 ** 2))
            + 0.7 * np.exp(-((proj - (pos - 0.18)) ** 2) / (2 * 0.03 ** 2)))
    fm_full = np.zeros(raw.shape[:2], np.float32)
    fm_full[ys, xs] = np.clip(flow, 0, 1)
    fm_full = cv2.GaussianBlur(fm_full, (0, 0), 3) * 1.05
    fm = np.clip(fm_full, 0, 1) * M
    out = red.copy()
    out += np.dstack([255 * fm, 60 * fm, 90 * fm]) * 0.95
    core = np.clip(fm - 0.72, 0, 1) / 0.28
    out += np.dstack([255 * core, 220 * core, 230 * core]) * 0.85
    pulse = 0.82 + 0.18 * np.sin(ph * 2 * np.pi)
    gl = glow_a * pulse
    out_glow = out.copy()
    m = alpha < 200
    out_glow[m] = np.dstack([255 * gl[m], 45 * gl[m], 70 * gl[m]])
    a = np.clip(alpha + gl * 255 * 0.85, 0, 255).astype(np.uint8)
    return np.dstack([out_glow.astype(np.uint8), a])


def to_128(rgba):
    im = Image.fromarray(rgba)
    a = np.asarray(im)[:, :, 3]
    yx, xx = np.where(a > 40)
    bb = (max(0, xx.min() - PAD), max(0, yx.min() - PAD),
          min(im.width, xx.max() + 1 + PAD), min(im.height, yx.max() + 1 + PAD))
    W = bb[2] - bb[0]; Hh = bb[3] - bb[1]; maxd = max(W, Hh)
    sc = TARGET * S / maxd
    nw, nh = int(round(W * sc)), int(round(Hh * sc))
    c = im.crop(bb).resize((nw, nh), Image.LANCZOS)
    canvas = Image.new("RGBA", (S, S), (0, 0, 0, 0))
    canvas.paste(c, ((S - nw) // 2, (S - nh) // 2), c)
    return canvas


for i in range(N):
    ph = i / N
    tri = 1 - abs((ph * 2) % 2 - 1)
    pos = 0.18 + 0.64 * tri
    rgba = compose_frame(ph, pos)
    im = to_128(rgba)
    im.save(os.path.join(REL, "NoMercy_%02d.png" % i))
print("frames written:", N, "to", REL)
