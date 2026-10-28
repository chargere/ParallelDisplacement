# -*- coding: utf-8 -*-
"""PrismOpticSystem outline v2 —— rembg 下载被墙后的兜底。
思路：raw 的 grabCut 掩膜把"平滑暗紫光晕"也算成了前景，导致 outline 是糊团。
设备表面=高纹理(局部std) 或 高亮度(金属高光)；光雾=平滑+暗 → 剔除。
输出：新 outline + 新旧对比预览图。
"""
import numpy as np
import cv2
from PIL import Image, ImageFilter, ImageDraw

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
OUT = ROOT + r"\src\main\resources\hongwenResources\images\relics"
PREVIEW = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"

raw = Image.open(ROOT + r"\tmp_relic_drafts\raw_PrismOpticSystem.png").convert("RGBA")
arr = np.asarray(raw)
a = arr[:, :, 3]
M = (a > 128).astype(np.uint8)  # grabCut 掩膜（含光晕）

rgb = arr[:, :, :3].astype(np.float32)
lum = rgb.mean(2)

# 局部标准差（纹理）：E[X^2]-E[X]^2
k = 9
mL = cv2.blur(lum, (k, k)); m2L = cv2.blur(lum * lum, (k, k))
std = np.sqrt(np.maximum(m2L - mL * mL, 0))

core = ((M > 0) & ((std > 8.0) | (lum > 175))).astype(np.uint8)

# 闭运算弥合表面缝隙，再取最大连通体
ker = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (17, 17))
core = cv2.morphologyEx(core, cv2.MORPH_CLOSE, ker)
n, labels, stats, _ = cv2.connectedComponentsWithStats(core, 8)
if n > 1:
    biggest = 1 + int(np.argmax(stats[1:, cv2.CC_STAT_AREA]))
    core = (labels == biggest).astype(np.uint8)

# 填洞：从边界 floodfill 的补集即内部洞
ff = core.copy()
h, w = ff.shape
mask2 = np.zeros((h + 2, w + 2), np.uint8)
cv2.floodFill(ff, mask2, (0, 0), 1)
holes = (ff == 0).astype(np.uint8)
core = core | holes

# 开运算去掉残留小刺，轻微羽化边界
ker2 = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (7, 7))
core = cv2.morphologyEx(core, cv2.MORPH_OPEN, ker2)
soft = cv2.GaussianBlur(core.astype(np.float32), (5, 5), 1.0)
mask8 = (soft > 0.5).astype(np.uint8) * 255

sil = Image.fromarray(mask8, "L").filter(ImageFilter.GaussianBlur(0.6))
outline = Image.new("RGBA", raw.size, (238, 238, 238, 0))
outline.putalpha(sil)
outline.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystemOutline.png")

# ---- 预览：旧 outline vs 新 outline，深色底 ----
old = Image.open(OUT + r"\..\..\..\..\tmp_relic_drafts\_old_outline_backup.png") \
    if False else None
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
panels = [
    (main, checker(256), "main"),
    (main, Image.new("RGB", (256, 256), (28, 26, 34)), "main dark"),
    (newo, Image.new("RGB", (256, 256), (28, 26, 34)), "outline v2"),
]
for i, (img, bg, _) in enumerate(panels):
    b = bg.copy(); b.paste(img, (0, 0), img); sheet.paste(b, (20 + i * 276, 22))
sheet.save(PREVIEW + r"\prism_outline_v2.png")

# 覆盖率对比（调试用）
old_m = (np.asarray(Image.open(OUT + r"\PrismOpticSystemOutline.png").convert("RGBA"))[:, :, 3] > 128)
new_m = (mask8 > 128)
print("old outline px:", int(old_m.sum()), " new outline px:", int(new_m.sum()),
      " ratio: %.2f" % (new_m.sum() / max(old_m.sum(), 1)))
print("v2 done")
