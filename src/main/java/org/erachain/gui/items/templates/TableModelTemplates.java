package org.erachain.gui.items.templates;

import org.erachain.core.item.ItemCls;
import org.erachain.core.item.templates.TemplateCls;
import org.erachain.datachain.DCSet;
import org.erachain.datachain.ItemTemplateMap;
import org.erachain.gui.items.TableModelItemsSearch;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class TableModelTemplates extends TableModelItemsSearch {
    public static final int COLUMN_KEY = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS = 2;
    public static final int COLUMN_FAVORITE = 3;

    public TableModelTemplates() {
        super(DCSet.getInstance().getItemTemplateMap(), new String[]{"Key", "Name", "Creator", "Favorite"},
                new Boolean[]{false, true, true, false});
        super.COLUMN_FAVORITE = COLUMN_FAVORITE;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (this.list == null || row > this.list.size() - 1) {
            return null;
        }

        TemplateCls template = (TemplateCls) list.get(row);

        switch (column) {
            case COLUMN_KEY:

                return template.getKey();

            case COLUMN_NAME:

                return template.viewName();

            case COLUMN_ADDRESS:

                return template.getOwner().getPersonAsString();

            case COLUMN_FAVORITE:

                return template.isFavorite();

        }

        return null;
    }

}
