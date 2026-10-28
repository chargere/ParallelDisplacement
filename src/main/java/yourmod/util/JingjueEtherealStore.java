package yourmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

/** 虚化：手牌离开后 N 回合以 0 费返回。 */
public class JingjueEtherealStore {
    public static final ArrayList<Entry> entries = new ArrayList<>();

    public static class Entry {
        public AbstractCard card;
        public int turns;
        public boolean empower;
    }

    public static void clear() {
        entries.clear();
    }

    public static void add(AbstractCard card, int turns, boolean empower) {
        Entry e = new Entry();
        e.card = card;
        e.turns = turns;
        e.empower = empower;
        entries.add(e);
    }

    public static void atStartOfTurn() {
        if (AbstractDungeon.player == null) {
            return;
        }
        Iterator<Entry> it = entries.iterator();
        while (it.hasNext()) {
            Entry e = it.next();
            e.turns--;
            if (e.turns <= 0) {
                AbstractCard c = e.card;
                c.setCostForTurn(0);
                if (e.empower) {
                    if (c.baseDamage > 0) {
                        c.baseDamage = c.baseDamage + c.baseDamage / 2;
                    }
                    if (c.baseBlock > 0) {
                        c.baseBlock = c.baseBlock + c.baseBlock / 2;
                    }
                    c.applyPowers();
                }
                AbstractDungeon.player.hand.addToHand(c);
                it.remove();
            }
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
}
