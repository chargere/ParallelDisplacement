# -*- coding: utf-8 -*-
"""
遗物图后处理流水线
==================
输入：tmp_relic_drafts/raw_{RelicID}.png（AI 生成的 1024x1024 草图）
输出：src/main/resources/hongwenResources/images/relics/{RelicID}.png        (128x128 主图, 透明底)
      src/main/resources/hongwenResources/images/relics/{RelicID}Outline.png (128x128 白色剪影, 透明底)

步骤：抹右下角 AI 水印（左下角背景镜像覆盖）-> 中心裁方 ->
      grabCut 抠掉背景（AI 图都带深色底，不透明直接贴游戏里是一块方砖）->
      LANCZOS 缩放 128；outline = 物件剪影 -> 白色填充 + 透明底

抠图思路：边缘带估背景色 -> 色差阈值 + 边缘连通得"确定背景"，
          距离背景色很远的当"确定前景"，其余交给 grabCut 学颜色模型。
          暗底 + 暗主体（黑蹄/黑烟）单靠色差分不开，必须 grabCut 兜底。

跳过：LIGHTFEATHER/时璃相簿（未定角色），TodoItem（模板残留）。
换风格后重跑：把新草图放回 tmp_relic_drafts/raw_{ID}.png 再执行本脚本。
"""
import os, sys
import numpy as np
from scipy import ndimage
from PIL import Image, ImageFilter

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DRAFT = os.path.join(ROOT, "tmp_relic_drafts")
OUT = os.path.join(ROOT, "src", "main", "resources", "hongwenResources", "images", "relics")

SKIP = {"raw_LightFeatherGaleBlade.png", "raw_TimeGlassAlbum.png"}
FINAL_SIZE = 128


def kill_watermark(im):
    """AI 水印固定在右下角；用左下角同尺寸区域水平镜像覆盖"""
    w, h = im.size
    bw, bh = int(w * 0.26), int(h * 0.10)  # 覆盖右下 26% x 10%
    patch = im.crop((0, h - bh, bw, h)).transpose(Image.FLIP_LEFT_RIGHT)
    im.paste(patch, (w - bw, h - bh))
    return im


def estimate_bg(arr, band=16):
    """取四边条带的中位数作为背景色（每张图底色不同，必须逐图估计）"""
    bands = np.concatenate([
        arr[:band].reshape(-1, 3), arr[-band:].reshape(-1, 3),
        arr[:, :band].reshape(-1, 3), arr[:, -band:].reshape(-1, 3),
    ])
    return np.median(bands, axis=0)


def core_background(arr):
    """确定背景：与背景色近似 + 与画面边缘连通（主体内部同色区不误删）"""
    bg = estimate_bg(arr)
    dist = np.linalg.norm(arr - bg, axis=2)
    band = 16
    noise = np.concatenate([
        dist[:band].ravel(), dist[-band:].ravel(),
        dist[:, :band].ravel(), dist[:, -band:].ravel()])
    tol = float(np.clip(np.percentile(noise, 90) * 1.8, 26, 60))
    lab, _ = ndimage.label(dist < tol)
    edge = set(lab[0].tolist()) | set(lab[-1].tolist()) \
        | set(lab[:, 0].tolist()) | set(lab[:, -1].tolist())
    edge.discard(0)
    return np.isin(lab, list(edge)), dist, tol


def remove_background(im):
    """RGB PIL -> RGBA PIL：grabCut 抠背景 + 羽化 + 边缘去色溢"""
    import cv2
    rgb = im.convert("RGB")
    arr = np.asarray(rgb, dtype=np.float32)
    core, dist, tol = core_background(arr)

    bgr = cv2.cvtColor(np.asarray(rgb), cv2.COLOR_RGB2BGR)
    gc = np.full(bgr.shape[:2], cv2.GC_PR_FGD, np.uint8)
    gc[core] = cv2.GC_PR_BGD
    gc[dist > tol * 3.2] = cv2.GC_FGD
    bgd = np.zeros((1, 65), np.float64)
    fgd = np.zeros((1, 65), np.float64)
    cv2.grabCut(bgr, gc, None, bgd, fgd, 5, cv2.GC_INIT_WITH_MASK)
    fg = (gc == cv2.GC_FGD) | (gc == cv2.GC_PR_FGD)

    alpha = np.where(fg, 255, 0).astype(np.uint8)
    a = Image.fromarray(alpha).filter(ImageFilter.GaussianBlur(0.7))
    a_arr = np.asarray(a, dtype=np.float32)
    a_arr[~fg] = 0
    a_arr = np.clip(a_arr, 0, 255)

    out = arr.copy()
    af = a_arr / 255.0
    af3 = af[..., None]
    band = (af3 > 0.02) & (af3 < 0.98)
    safe = np.maximum(af3, 0.30)
    de = (out - estimate_bg(arr)[None, None, :] * (1.0 - af3)) / safe
    out = np.where(band, np.clip(de, 0, 255), out)

    return Image.fromarray(np.dstack([out, a_arr]).astype(np.uint8), "RGBA")


def make_outline(im):
    """物件剪影 -> 白色填充 + 透明底（STS outline 风格）"""
    rgba = im if im.mode == "RGBA" else remove_background(im)
    gray = rgba.convert("L")
    mask = gray.point(lambda p: 255 if p > 52 else 0)
    mask = mask.filter(ImageFilter.MaxFilter(9)).filter(ImageFilter.GaussianBlur(3))
    # 剪影 alpha 跟着抠图 alpha 走：物件没了的地方不能留白色剪影
    mask = Image.composite(mask, Image.new("L", mask.size, 0), rgba.split()[3])
    base = Image.new("RGBA", rgba.size, (238, 238, 238, 0))
    base.putalpha(mask)
    return base


def main():
    only = set(sys.argv[1:])
    ok, skip = 0, []
    for fn in sorted(os.listdir(DRAFT)):
        if not (fn.startswith("raw_") and fn.endswith(".png")) or fn in SKIP:
            continue
        rid = fn[4:-4]
        if only and rid not in only:
            continue
        im = Image.open(os.path.join(DRAFT, fn)).convert("RGB")
        im = kill_watermark(im)
        s = min(im.size)
        im = im.crop(((im.width - s) // 2, (im.height - s) // 2,
                      (im.width + s) // 2, (im.height + s) // 2))
        main_png = remove_background(im).resize((FINAL_SIZE, FINAL_SIZE), Image.LANCZOS)
        out_png = make_outline(im).resize((FINAL_SIZE, FINAL_SIZE), Image.LANCZOS)
        main_png.save(os.path.join(OUT, rid + ".png"))
        out_png.save(os.path.join(OUT, rid + "Outline.png"))
        ok += 1
    print("处理 %d 张（主图+outline，透明底）" % ok)
    for f in sorted(os.listdir(DRAFT)):
        if f.startswith("raw_") and f not in SKIP:
            rid = f[4:-4]
            if not os.path.exists(os.path.join(OUT, rid + ".png")):
                print("[?] 未落盘:", rid)


if __name__ == "__main__":
    main()
