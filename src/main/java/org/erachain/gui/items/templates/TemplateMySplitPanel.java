package org.erachain.gui.items.templates;

import org.erachain.core.item.ItemCls;
import org.erachain.core.item.persons.PersonCls;
import org.erachain.core.item.templates.TemplateCls;
import org.erachain.core.transaction.Transaction;
import org.erachain.datachain.DCSet;
import org.erachain.gui.items.ItemSplitPanel;
import org.erachain.gui.items.persons.PersonConfirmDialog;
import org.erachain.gui.items.persons.PersonInfo002;
import org.erachain.gui.items.persons.PersonSetStatusDialog;
import org.erachain.gui.models.WalletItemPersonsTableModel;
import org.erachain.gui.models.WalletItemTemplatesTableModel;
import org.erachain.gui.records.VouchRecordDialog;
import org.erachain.lang.Lang;
import org.erachain.settings.Settings;
import org.erachain.utils.URLViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;


public class TemplateMySplitPanel extends ItemSplitPanel {
    private static final long serialVersionUID = 2717571093561259483L;

    private TemplateMySplitPanel th;

    public TemplateMySplitPanel() {
        super(new WalletItemTemplatesTableModel(), "TemplateMySplitPanel");

        this.setName(Lang.getInstance().translate("My Templates"));
        th = this;
//      add items in menu


        JMenuItem setSeeInBlockexplorer = new JMenuItem(Lang.getInstance().translate("Check in Blockexplorer"));

        setSeeInBlockexplorer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    URLViewer.openWebpage(new URL("http://" + Settings.getInstance().getBlockexplorerURL()
                            + ":" + Settings.getInstance().getWebPort() + "/index/blockexplorer.html"
                            + "?template=" + th.itemTableSelected.getKey()));
                } catch (MalformedURLException e1) {
                    logger.error(e1.getMessage(), e1);                }
            }
        });

        menuTable.add(setSeeInBlockexplorer);

    }

    // show details
    @Override
    public Component getShow(ItemCls item) {
        return new InfoTemplates((TemplateCls) item);
    }

}