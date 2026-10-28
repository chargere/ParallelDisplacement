import math
from PIL import Image, ImageDraw

S = 128
OUT = r"C:/Users/38220/Desktop/ProTemplate-master/src/main/resources/hongwenResources/images/relics/WindHueMeteorFall.png"

def dagger(blade, stripe):
    img = Image.new("RGBA", (S, S), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    cx = S // 2
    # 刀身（向上菱形）
    tip = (cx, 14)
    ml = (cx - 8, cx - 2 + 40)
    mr = (cx + 8, cx - 2 + 40)
    base = (cx, cx + 6)
    d.polygon([tip, ml, mr, base], fill=blade)
    # 护手
    d.rectangle([cx - 14, cx + 6, cx + 14, cx + 11], fill=blade)
    # 握柄
    d.rectangle([cx - 4, cx + 11, cx + 4, cx + 30], fill=(90, 80, 110, 255))
    # 柄头
    d.ellipse([cx - 6, cx + 28, cx + 6, cx + 40], fill=blade)
    # 贯穿中央纹路（亮线）
    d.line([cx, 16, cx, cx + 6], fill=stripe, width=3)
    return img

def rot(img, ang):
    return img.rotate(ang, resample=Image.BICUBIC, center=(S // 2, S // 2), expand=False)

base = Image.new("RGBA", (S, S), (0, 0, 0, 0))
# 紫色短刀（顺时针）
purple = dagger((201, 163, 255, 255), (155, 92, 255, 255))
base.alpha_composite(rot(purple, 30))
# 粉色短刀（逆时针）
pink = dagger((255, 158, 199, 255), (255, 79, 163, 255))
base.alpha_composite(rot(pink, -30))
base.save(OUT)
print("saved", OUT, base.size, base.mode)
