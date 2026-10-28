# 平行异位 · ParallelDisplacement

> 《杀戮尖塔》(Slay the Spire) 模组 —— 五个自定角色，各自活在另一条时间线里。

你有没有过这样的瞬间——某个傍晚，街灯还没亮，你看见一个熟人站在对面。
他的脸你认得，他的姿势你熟悉，但某一刻你忽然觉得，这个人好像不该是现在这个样子。
他应该穿着不一样的衣服，说着不一样的话，走向一个你从未见过他去的地方。

那一刻，你瞥见了另一条时间线。

「平行异位」不解释任何事。它只是一道裂缝，让你站在此岸，看见彼岸的倒影动了起来。

---

## 简介

一套杀戮尖塔自定义角色扩展，包含五位**互不相关、却被同一条支流收留**的角色。
每位角色都拥有独立的资源体系与机制总线，而非在原版框架上做数值堆砌：

| 角色 | 代号 | 主题 | 核心机制 |
|------|------|------|----------|
| 鸿文君主 | Hongwen Monarch | 火 / 雷 | 烬火 × 静电 双资源连携总线 |
| 霜星旅者 | Dominic | 霜 / 星 | 霜寒资源 + 星痕(StarMark) 代价闭环 |
| 达穆候 · 战斗精英 | Damuhou | 军事射手 | 火控双模式 + 棱镜锁定 + 破绽爆发 |
| 精绝王者 | Jingjue | 时间 / 处决 | 眼印放大 + 百分比处决总线 |
| Fvv | Fvv | 彩蛋 | 蛋形态怪咖 |

---

## 基于与依赖（署名）

本项目基于 **ProTemplate** 模板开发，遵循其 MIT 许可（© 2019 VEX）。
运行与构建依赖以下开源项目，版权归各自作者所有：

- **[ModTheSpire](https://github.com/kiooeht/ModTheSpire)** `3.18.2` —— 模组加载框架
- **[BaseMod](https://github.com/daviscook477/BaseMod)** `5.29.0` —— 模组基础 API
- **[StSLib](https://github.com/StSLib/StSLib)** `1.3.2` —— 扩展工具库
- **Spriter** —— 角色骨骼动画（`static.scml`）

---

## 当前状态

⚠️ **开发中 (WIP)**。五位角色的机制与卡牌已实现并可游玩；
局内立绘目前为占位资源，美术资产正在制作中。

---

## 构建与运行

前置：已安装《杀戮尖塔》（Steam 版）、Java 8、Maven，并已订阅
ModTheSpire / BaseMod / StSLib 创意工坊项。

```bash
mvn package
# 产物 target/ParallelDisplacement.jar 会自动复制到
# Steam/steamapps/common/SlayTheSpire/mods/
```

或将打包好的 `ParallelDisplacement.jar` 手动放入 `SlayTheSpire/mods/` 后启动游戏。

---

## 许可

本项目沿用模板的 **MIT License**（© 2019 VEX）。详见 [LICENSE](LICENSE) 文件。
第三方依赖各自遵循其原有协议。

---

作者：乌鲁鲁爱堵桥 · 版本 1.14
