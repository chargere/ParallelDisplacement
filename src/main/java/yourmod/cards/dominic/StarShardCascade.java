package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarShardCascade extends AbstractDominicCard {
    public static final String ID = makeID("StarShardCascade");

    public StarShardCascade() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 4;
        setFrostPay(3, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(
                            null, true, AbstractDungeon.cardRandomRng);
                    if (target != null && !target.isDeadOrEscaped()) {
                        int hit = damage;
                        if (target.hasPower(StarMarkPower.POWER_ID)) {
                            hit += 4;
                        }
                        addToTop(new DamageAction(target,
                                new DamageInfo(p, hit, DamageInfo.DamageType.THORNS),
                                AttackEffect.SLASH_HORIZONTAL));
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
