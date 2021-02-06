/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities.diff;

import java.util.List;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class Path {

    private static final Log4j2Helper LOG = new Log4j2Helper(Path.class);

    private EditCommand editCommand;

    private Path prev;

    public Path(EditCommand editCommand, Path prev) {
        super();
        this.editCommand = editCommand;
        this.prev = prev;
    }

    public void getEditScript(List<EditCommand> shortestEditScript) {

        if (prev != null) {

            prev.getEditScript(shortestEditScript);

            shortestEditScript.add(editCommand);
        }
    }

    public void print(String tab) {
        LOG.info(tab + "[editCommand=" + editCommand + "]");

        if (prev != null) {
            prev.print(tab + "\t");
        } else {
            LOG.info(tab + "Prev: null");
        }
    }

}
