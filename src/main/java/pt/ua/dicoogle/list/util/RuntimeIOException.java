/**
 * Copyright (C) 2017 UA.PT Bioinformatics - http://bioinformatics.ua.pt/
 *
 * This file is part of Dicoogle/list.
 *
 * Dicoogle/list is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dicoogle/list is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Dicoogle.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ua.dicoogle.list.util;

/**
 *
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
public class RuntimeIOException extends RuntimeException {

    /**
     * Creates a new instance of <code>RuntimeIOException</code> without detail message.
     */
    public RuntimeIOException() {
    }


    /**
     * Constructs an instance of <code>RuntimeIOException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RuntimeIOException(String msg) {
        super(msg);
    }

    public RuntimeIOException(String msg, Throwable thr) {
        super(msg, thr);
    }

    public RuntimeIOException(Throwable thr) {
        super(thr);
    }
}
