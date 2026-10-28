# -*- coding: utf-8 -*-
"""PrismOpticSystem outline v3 —— 基于 debug 结论：raw 掩膜边界本身很贴合设备，
问题只在左上断开的光斑碎片 + 旧 outline 是更早一轮失败产物。
做法：alpha>128 → 最大连通体(丢碎片) → 清小白斑 → 填洞 → 轻闭运算 → 羽化。
"""
import numpy as np
import cv2
from PIL import Image, ImageFilter, ImageDraw

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
OUT = ROOT + r"\src\main\resources\hongwenResources\images\relics"
PREVIEW = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"

raw = Image.open(ROOT + r"\tmp_relic_drafts\raw_PrismOpticSystem.png").convert("RGBA")
arr = np.asarray(raw)
M = (arr[:, :, 3] > 128).astype(np.uint8)

# 最大连通体（丢掉左上光斑碎片）
n, labels, stats, _ = cv2.connectedComponentsWithStats(M, 8)
biggest = 1 + int(np.argmax(stats[1:, cv2.CC_STAT_AREA]))
core = (labels == biggest).astype(np.uint8)
# 清掉 <2000px 的残余小斑
n2, labels2, stats2, _ = cv2.connectedComponentsWithStats(core, 8)
for i in range(1, n2):
    if stats2[i, cv2.CC_STAT_AREA] < 2000:
        core[labels2 == i] = 0

# 填洞
ff = core.copy()
h, w = ff.shape
mask2 = np.zeros((h + 2, w + 2), np.uint8)
cv2.floodFill(ff, mask2, (0, 0), 1)
core = core | (ff == 0).astype(np.uint8)

# 轻闭运算平滑锯齿 + 羽化
ker = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5))
core = cv2.morphologyEx(core, cv2.MORPH_CLOSE, ker)
soft = cv2.GaussianBlur(core.astype(np.float32), (5, 5), 1.0)
mask8 = (soft > 0.5).astype(np.uint8) * 255

sil = Image.fromarray(mask8, "L").filter(ImageFilter.GaussianBlur(0.5))
outline = Image.new("RGBA", raw.size, (238, 238, 238, 0))
outline.putalpha(sil)
outline.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystemOutline.png")

# ---- 预览 ----
def checker(s):
    bg = Image.new("RGB", (s, s)); d = ImageDraw.Draw(bg); c = 16
    for y in range(0, s, c):
        for x in range(0, s, c):
            d.rectangle([x, y, x + c - 1, y + c - 1],
                        fill=(90, 90, 90) if (x // c + y // c) % 2 else (130, 130, 130))
    return bg

main = Image.open(OUT + r"\PrismOpticSystem.png").convert("RGBA").resize((256, 256), Image.NEAREST)
newo = outline.resize((256, 256), Image.NEAREST)
sheet = Image.new("RGB", (256 * 3 + 80, 300), (15, 15, 15))
for i, (img, bg) in enumerate([
    (main, checker(256)),
    (main, Image.new("RGB", (256, 256), (28, 26, 34))),
    (newo, Image.new("RGB", (256, 256), (28, 26, 34))),
]):
    b = bg.copy(); b.paste(img, (0, 0), img); sheet.paste(b, (20 + i * 276, 22))
sheet.save(PREVIEW + r"\prism_outline_v3.png")
print("v3 done, outline px:", int((mask8 > 128).sum()))
