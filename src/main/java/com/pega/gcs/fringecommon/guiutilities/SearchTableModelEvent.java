/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class SearchTableModelEvent extends TableModelEvent {

    private static final long serialVersionUID = -6075650634045939506L;

    public SearchTableModelEvent(TableModel source) {
        super(source);
    }

}
