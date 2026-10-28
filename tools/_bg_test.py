# -*- coding: utf-8 -*-
"""遗物图去背景：试验脚本（验证效果后再并入 gen_relic_icons.py）"""
import os
import numpy as np
from scipy import ndimage
from PIL import Image, ImageFilter

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
DRAFT = os.path.join(ROOT, "tmp_relic_drafts")
OUTDIR = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"


def estimate_bg(arr, band=16):
    """取四边条带的中位数作为背景色（每张图底色不同，必须逐图估计）"""
    bands = np.concatenate([
        arr[:band].reshape(-1, 3), arr[-band:].reshape(-1, 3),
        arr[:, :band].reshape(-1, 3), arr[:, -band:].reshape(-1, 3),
    ])
    return np.median(bands, axis=0)


def bg_mask(arr, tol=None):
    """与背景色近似的像素 + 只保留与画面边缘连通的部分（主体内部同色区不误删）
    tol 缺省时自适应：以边缘带背景噪声的 90 分位为基准放大——
    背景平（暗底）就用小阈值保住暗色主体，背景花（渐变/噪点）才放宽"""
    bg = estimate_bg(arr)
    dist = np.linalg.norm(arr - bg, axis=2)
    if tol is None:
        band = 16
        noise = np.concatenate([
            dist[:band].ravel(), dist[-band:].ravel(),
            dist[:, :band].ravel(), dist[:, -band:].ravel()])
        tol = float(np.clip(np.percentile(noise, 90) * 1.8, 26, 60))
    seed = dist < tol
    lab, _ = ndimage.label(seed)
    edge = set(lab[0].tolist()) | set(lab[-1].tolist()) \
        | set(lab[:, 0].tolist()) | set(lab[:, -1].tolist())
    edge.discard(0)
    core = np.isin(lab, list(edge))
    return core, dist, tol


def grabcut_refine(rgb, core_bg, sure_fg):
    """用色差法的确定背景/确定前景当先验，grabCut 学颜色模型处理模糊地带"""
    import cv2
    img = cv2.cvtColor(np.asarray(rgb.convert("RGB")), cv2.COLOR_RGB2BGR)
    gc = np.full(img.shape[:2], cv2.GC_PR_FGD, np.uint8)
    gc[core_bg] = cv2.GC_PR_BGD
    gc[sure_fg] = cv2.GC_FGD
    bgd = np.zeros((1, 65), np.float64)
    fgd = np.zeros((1, 65), np.float64)
    cv2.grabCut(img, gc, None, bgd, fgd, 5, cv2.GC_INIT_WITH_MASK)
    return (gc == cv2.GC_FGD) | (gc == cv2.GC_PR_FGD)


def remove_bg(im, tol=None, feather=0.7, decont=True):
    """im: RGB PIL -> RGBA PIL（grabCut 抠图 + 边缘羽化 + 可选去色溢）"""
    rgb = im.convert("RGB")
    arr = np.asarray(rgb, dtype=np.float32)
    core, dist, tol = bg_mask(arr, tol)
    fg = grabcut_refine(rgb, core, dist > tol * 3.2)
    mask = ~fg
    bg = estimate_bg(arr)

    alpha = np.where(mask, 0, 255).astype(np.uint8)
    a = Image.fromarray(alpha).filter(ImageFilter.GaussianBlur(feather))
    # 羽化会把 alpha 外扩进背景区：判定为背景的像素硬压回 0，
    # 过渡带（tol ~ tol*1.5）保留半透明做柔和边缘
    a_arr = np.asarray(a, dtype=np.float32)
    a_arr[mask] = 0
    a_arr = np.clip(a_arr, 0, 255)

    out = np.asarray(rgb, dtype=np.float32)
    if decont:
        # 去色溢：只处理边缘半透明带（0.02<a<0.98），alpha 过低不外推（会炸白）
        af = a_arr / 255.0
        af3 = af[..., None]
        band = (af3 > 0.02) & (af3 < 0.98)
        safe = np.maximum(af3, 0.30)
        de = (out - bg[None, None, :] * (1.0 - af3)) / safe
        out = np.where(band, np.clip(de, 0, 255), out)
    rgba = np.dstack([out, a_arr]).astype(np.uint8)
    return Image.fromarray(rgba, "RGBA")


def checker(size, cell=16, c1=(235, 235, 235), c2=(190, 190, 190)):
    """透明底可视化棋盘格"""
    n = size // cell + 1
    arr = np.zeros((size, size, 3), dtype=np.uint8)
    for i in range(n):
        for j in range(n):
            v = c1 if (i + j) % 2 == 0 else c2
            arr[i * cell:(i + 1) * cell, j * cell:(j + 1) * cell] = v
    return Image.fromarray(arr)


def main():
    ids = ["AuroraPocketWatch", "FurnaceBreath", "CompassionFire",
           "JjBlackHoof", "EternalStormAnchor", "JjCamelSkin"]
    S = 256
    sheet = Image.new("RGB", (S * 3, S * len(ids) + 40), (255, 255, 255))
    from PIL import ImageDraw
    d = ImageDraw.Draw(sheet)
    d.text((10, 10), "左：原始（带底色）  中：去背后（棋盘格可见透明）  右：叠在游戏深底上", fill=(30, 30, 30))
    for k, rid in enumerate(ids):
        raw = Image.open(os.path.join(DRAFT, "raw_%s.png" % rid)).convert("RGB")
        s = min(raw.size)
        raw = raw.crop(((raw.width - s) // 2, (raw.height - s) // 2,
                        (raw.width + s) // 2, (raw.height + s) // 2)).resize((S, S), Image.LANCZOS)
        cut = remove_bg(raw)
        y = 40 + k * S
        sheet.paste(raw, (0, y))
        sheet.paste(checker(S), (S, y))
        sheet.paste(cut, (S, y), cut)
        bg_game = Image.new("RGB", (S, S), (58, 48, 38))
        bg_game.paste(cut, (0, 0), cut)
        sheet.paste(bg_game, (S * 2, y))
    p = os.path.join(OUTDIR, "relic_bg_test.png")
    sheet.save(p)
    print("saved", p, sheet.size)

    # 顺便统计每张去掉了多少
    for rid in ids:
        raw = Image.open(os.path.join(DRAFT, "raw_%s.png" % rid)).convert("RGB")
        cut = remove_bg(raw)
        a = np.asarray(cut.split()[3])
        print("  %-22s 透明占比 %.1f%%" % (rid, (a < 16).mean() * 100))


if __name__ == "__main__":
    main()
