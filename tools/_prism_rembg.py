# -*- coding: utf-8 -*-
"""用 rembg(U2Net) 抠 PrismOpticSystem —— 色距法对"装置+光效背景"失效后的兜底。
输入：tmp_relic_drafts/source/A_futuristic_sci_fi_tactical_m_*.png
输出：更新 raw_PrismOpticSystem.png + relics 主图/outline + jar
"""
import glob
import numpy as np
from PIL import Image, ImageFilter
from rembg import remove, new_session

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
SRC = glob.glob(ROOT + r"\tmp_relic_drafts\source\A_futuristic_sci_fi_tactical_m_*.png")[0]
OUT = ROOT + r"\src\main\resources\hongwenResources\images\relics"

im = Image.open(SRC).convert("RGB")
# 杀水印
w, h = im.size
bw, bh = int(w * 0.26), int(h * 0.10)
patch = im.crop((0, h - bh, bw, h)).transpose(Image.FLIP_LEFT_RIGHT)
im.paste(patch, (w - bw, h - bh))

session = new_session("u2net")
cut = remove(im, session=session)
cut = cut.crop(cut.split()[3].getbbox())

S = 1024
scale = S * 0.96 / max(cut.size)
nw, nh = int(cut.width * scale), int(cut.height * scale)
small = cut.resize((nw, nh), Image.LANCZOS)
canvas = Image.new("RGBA", (S, S), (0, 0, 0, 0))
canvas.paste(small, ((S - nw) // 2, (S - nh) // 2), small)
canvas.save(ROOT + r"\tmp_relic_drafts\raw_PrismOpticSystem.png")
canvas.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystem.png")

m = np.asarray(canvas.split()[3], dtype=np.uint8)
mask = Image.fromarray(m).point(lambda p: 255 if p > 128 else 0)
mask = mask.filter(ImageFilter.MaxFilter(3)).filter(ImageFilter.GaussianBlur(1.2))
base = Image.new("RGBA", canvas.size, (238, 238, 238, 0))
base.putalpha(mask)
base.resize((128, 128), Image.LANCZOS).save(OUT + r"\PrismOpticSystemOutline.png")

# 验收图
from PIL import ImageDraw

def checker(s):
    bg = Image.new("RGB", (s, s)); d = ImageDraw.Draw(bg); c = 16
    for y in range(0, s, c):
        for x in range(0, s, c):
            d.rectangle([x, y, x + c - 1, y + c - 1],
                        fill=(90, 90, 90) if (x // c + y // c) % 2 else (130, 130, 130))
    return bg

main = Image.open(OUT + r"\PrismOpticSystem.png").convert("RGBA").resize((256, 256), Image.NEAREST)
outl = Image.open(OUT + r"\PrismOpticSystemOutline.png").convert("RGBA").resize((256, 256), Image.NEAREST)
sheet = Image.new("RGB", (256 * 3 + 80, 300), (15, 15, 15))
for i, (img, bg) in enumerate([(main, checker(256)), (main, Image.new("RGB", (256, 256), (28, 26, 34))),
                               (outl, Image.new("RGB", (256, 256), (28, 26, 34)))]):
    b = bg.copy(); b.paste(img, (0, 0), img); sheet.paste(b, (20 + i * 276, 22))
sheet.save(r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview\prism_rembg.png")
print("rembg done")
