package org.erachain.gui.items.templates;

import org.erachain.core.item.ItemCls;
import org.erachain.core.item.templates.TemplateCls;
import org.erachain.core.transaction.Transaction;
import org.erachain.datachain.DCSet;
import org.erachain.gui.items.ItemSplitPanel;
import org.erachain.gui.records.VouchRecordDialog;
import org.erachain.lang.Lang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TemplatesFavoriteSplitPanel extends ItemSplitPanel {
    private static final long serialVersionUID = 2717571093561259483L;
    private TemplatesFavoriteSplitPanel th;

    public TemplatesFavoriteSplitPanel() {
        super(new FavoriteTemplatesTableModel(), "TemplatesFavoriteSplitPanel");
        this.setName(Lang.getInstance().translate("Favorite Templates"));
        th = this;
        JMenuItem vouch_menu = new JMenuItem(Lang.getInstance().translate("Vouch"));
        vouch_menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DCSet db = DCSet.getInstance();
                Transaction trans = db.getTransactionFinalMap().get(((TemplateCls) th.itemMenu).getReference());
                new VouchRecordDialog(trans.getBlockHeight(), trans.getSeqNo());

            }
        });
        th.menuTable.add(vouch_menu);
    }

    // show details
    @Override
    public Component getShow(ItemCls item) {
        return new InfoTemplates((TemplateCls) item);
    }

}