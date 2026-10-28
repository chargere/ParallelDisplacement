package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 雷火轮回（0费）
 * 升级前：交换生命与格挡（生命不低于10）；消耗
 * 升级后：不再消耗
 */
public class ThunderFireCycle extends AbstractEasyCard {
    public final static String ID = makeID("ThunderFireCycle");

    public ThunderFireCycle() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                int hp = p.currentHealth;
                int block = p.currentBlock;
                p.currentBlock = 0;
                int newHp = Math.max(10, block);
                int newBlock = hp;
                if (newHp < hp) {
                    p.damage(new DamageInfo(p, hp - newHp, DamageInfo.DamageType.HP_LOSS));
                } else if (newHp > hp) {
                    p.heal(newHp - hp);
                }
                if (newBlock > 0) {
                    p.addBlock(newBlock);
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
