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
package pt.ua.dicoogle.list;

import pt.ua.dicoogle.sdk.StorageInputStream;
import pt.ua.dicoogle.sdk.StorageInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;

import java.net.URI;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
public class DicoogleFileLister {

    private DicooglePlatformInterface dicoogle;

    public DicoogleFileLister(DicooglePlatformInterface dicoogle) {
        this.dicoogle = dicoogle;
    }

    public DicoogleFileLister() {
        this.dicoogle = null;
    }

    public void setPlatformInterface(DicooglePlatformInterface platform) {
        this.dicoogle = platform;
    }

    public Stream<StorageInputStream> filesIn(URI root, int depth, Object... args) {
        assert root != null;

        StorageInterface store = this.dicoogle.getStorageForSchema(root);

        Iterable<StorageInputStream> it = store.at(root, args);

        Stream<StorageInputStream> s = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(it.iterator(), 0), false);

        if (depth <= 0) {
            return s;
        }

        final int rootN = numPathParts(root);
        return s.filter(f -> numPathParts(f.getURI()) - rootN <= depth);
    }

    private static int numPathParts(URI uri) {
        String path = uri.getPath();
        if (path == null) path = "";
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.split("/").length;
    }
}
