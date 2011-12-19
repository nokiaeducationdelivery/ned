/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.client;

import com.google.gwt.user.client.Random;

public class NedStringGenerator {

    static final int RANDOM_ID_SIZE = 6;

    private char[] symbols = new char[36];

    public NedStringGenerator() {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    public String nextString() {
        return nextString(RANDOM_ID_SIZE);
    }

    public String nextString(int length) {
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[Random.nextInt(symbols.length)];
        return new String(buf);
    }
}
