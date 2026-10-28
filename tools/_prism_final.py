# -*- coding: utf-8 -*-
"""PrismOpticSystem 最终版 —— 基于 cD 核心（std纹理+亮度，剔除平滑暗包络）。
主图与 outline 一并重建：主图去掉灰色包络更干净，outline 是设备真实剪影。
"""
import numpy as np
import cv2
from PIL import Image, ImageFilter, ImageDraw

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
OUT = ROOT + r"\src\main\resources\hongwenResources\images\relics"
PREVIEW = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"

raw = Image.open(ROOT + r"\tmp_relic_drafts\raw_PrismOpticSystem.png").convert("RGBA")
arr = np.asarray(raw)
M = np.ascontiguousarray((arr[:, :, 3] > 128).astype(np.uint8))
rgb = arr[:, :, :3].astype(np.float32)
lum = rgb.mean(2)
mL = cv2.blur(lum, (9, 9)); m2 = cv2.blur(lum * lum, (9, 9))
s9 = np.sqrt(np.maximum(m2 - mL * mL, 0))

# 包络 = 平滑；阴影残留 = 平滑且很暗
env = ((s9 < 6) & (lum < 110)) | ((s9 < 10) & (lum < 55))
core = M * (1 - env.astype(np.uint8)) * ((s9 > 7) | (lum > 150))
core = np.ascontiguousarray(core.astype(np.uint8))

# 最大连通体 + 清小斑
n, lab, st, _ = cv2.connectedComponentsWithStats(core, 8)
b = 1 + int(np.argmax(st[1:, cv2.CC_STAT_AREA]))
core = np.ascontiguousarray((lab == b).astype(np.uint8))
n2, lab2, st2, _ = cv2.connectedComponentsWithStats(core, 8)
for i in range(1, n2):
    if st2[i, cv2.CC_STAT_AREA] < 2500:
        core[lab2 == i] = 0

# 填洞 → 闭运算平滑 → 开运算去刺
ff = core.copy()
pad = np.zeros((core.shape[0] + 2, core.shape[1] + 2), np.uint8)
cv2.floodFill(ff, pad, (0, 0), 1)
core = core | (ff == 0).astype(np.uint8)
k5 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5))
core = cv2.morphologyEx(core, cv2.MORPH_CLOSE, k5)
core = cv2.morphologyEx(core, cv2.MORPH_OPEN, cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (3, 3)))

# 轻羽化 alpha（半像素级抗锯齿，不糊边）
soft = cv2.GaussianBlur(core.astype(np.float32), (3, 3), 0.8)
alpha_f = np.clip(soft, 0, 1)

# ---- 主图：原 RGB + 新 alpha ----
main1024 = np.dstack([arr[:, :, :3], (alpha_f * 255).astype(np.uint8)])
main_im = Image.fromarray(main1024, "RGBA")
main_im.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystem.png")

# ---- outline：白剪影 ----
sil = Image.fromarray((alpha_f * 255).astype(np.uint8), "L").filter(ImageFilter.GaussianBlur(0.4))
outline = Image.new("RGBA", raw.size, (238, 238, 238, 0))
outline.putalpha(sil)
outline.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystemOutline.png")

# ---- 预览：棋盘/深色卡底/浅色底 + outline ----
def checker(s):
    bg = Image.new("RGB", (s, s)); d = ImageDraw.Draw(bg); c = 16
    for y in range(0, s, c):
        for x in range(0, s, c):
            d.rectangle([x, y, x + c - 1, y + c - 1],
                        fill=(90, 90, 90) if (x // c + y // c) % 2 else (130, 130, 130))
    return bg

m256 = main_im.resize((256, 256), Image.LANCZOS)
o256 = outline.resize((256, 256), Image.LANCZOS)
sheet = Image.new("RGB", (256 * 4 + 100, 300), (15, 15, 15))
panels = [
    (m256, checker(256), "checker"),
    (m256, Image.new("RGB", (256, 256), (28, 26, 34)), "dark card"),
    (m256, Image.new("RGB", (256, 256), (200, 198, 205)), "light"),
    (o256, Image.new("RGB", (256, 256), (28, 26, 34)), "outline"),
]
for i, (img, bg, _) in enumerate(panels):
    b2 = bg.copy(); b2.paste(img, (0, 0), img); sheet.paste(b2, (20 + i * 276, 22))
sheet.save(PREVIEW + r"\prism_final.png")
print("final done")
