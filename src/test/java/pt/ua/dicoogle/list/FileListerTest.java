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

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.ua.dicoogle.sdk.IndexerInterface;
import pt.ua.dicoogle.sdk.QueryInterface;
import pt.ua.dicoogle.sdk.StorageInputStream;
import pt.ua.dicoogle.sdk.StorageInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.ServerSettingsReader;
import pt.ua.dicoogle.sdk.datastructs.Report;
import pt.ua.dicoogle.sdk.datastructs.SearchResult;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;
import pt.ua.dicoogle.sdk.task.JointQueryTask;
import pt.ua.dicoogle.sdk.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */
public class FileListerTest {

    static class EmptyFile implements StorageInputStream {

        private final URI uri;

        public EmptyFile(URI uri) {
            this.uri = uri;
        }

        @Override
        public URI getURI() {
            return this.uri;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getSize() throws IOException {
            return 0;
        }
    }

    static class StorageMock implements StorageInterface {
        private final List<URI> uris;

        StorageMock(List<URI> uris) {
            this.uris = uris;
        }

        static StorageMock fromStrings(Stream<String> uris) {
            return new StorageMock(uris
                    .map(URI::create)
                    .collect(Collectors.toList()));
        }

        @Override
        public String getScheme() {
            return "file";
        }

        @Override
        public boolean handles(URI uri) {
            return true;
        }

        @Override
        public Iterable<StorageInputStream> at(URI uri, Object... objects) {
            final String root = uri.toString();
            return this.uris.stream()
                    .filter(u -> u.toString().startsWith(root))
                    .map(EmptyFile::new)
                    .collect(Collectors.toList());
        }

        @Override
        public URI store(DicomObject dicomObject, Object... objects) {
            throw new UnsupportedOperationException();
        }

        @Override
        public URI store(DicomInputStream dicomInputStream, Object... objects) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(URI uri) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            return "mock";
        }

        @Override
        public boolean enable() {
            return true;
        }

        @Override
        public boolean disable() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void setSettings(ConfigurationHolder configurationHolder) {
        }

        @Override
        public ConfigurationHolder getSettings() {
            return null;
        }
    }

    static DicooglePlatformInterface dicoogle;

    @BeforeClass
    public static void initClass() {
        dicoogle = new DicooglePlatformInterface() {

            private final StorageInterface store =
                    StorageMock.fromStrings(Arrays.asList(
                            "file:/test.dcm",
                            "file:/CT/001.dcm",
                            "file:/CT/002.dcm",
                            "file:/CT/20150514/001.dcm",
                            "file:/MR/0.dcm").stream());

            @Override
            public IndexerInterface requestIndexPlugin(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public QueryInterface requestQueryPlugin(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Collection<IndexerInterface> getAllIndexPlugins() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Collection<QueryInterface> getAllQueryPlugins() {
                throw new UnsupportedOperationException();
            }

            @Override
            public StorageInterface getStoragePluginForSchema(String s) {
                return this.getStorageForSchema(s);
            }

            @Override
            public StorageInterface getStorageForSchema(URI uri) {
                return getStorageForSchema(uri.getScheme());
            }

            @Override
            public Iterable<StorageInputStream> resolveURI(URI uri, Object... args) {
                StorageInterface store = this.getStorageForSchema(uri);
                if (store == null) return null;
                return store.at(uri, args);
            }

            @Override
            public Collection<StorageInterface> getStoragePlugins(boolean b) {
                return Collections.singleton(this.store);
            }

            @Override
            public StorageInterface getStorageForSchema(String s) {
                if ("file".equals(s))
                    return store;
                return null;
            }

            @Override
            public Collection<QueryInterface> getQueryPlugins(boolean b) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<String> getQueryProvidersName(boolean b) {
                throw new UnsupportedOperationException();
            }

            @Override
            public QueryInterface getQueryProviderByName(String s, boolean b) {
                throw new UnsupportedOperationException();
            }

            @Override
            public JointQueryTask queryAll(JointQueryTask jointQueryTask, String s, Object... objects) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Task<Iterable<SearchResult>> query(String s, String s1, Object... objects) {
                throw new UnsupportedOperationException();
            }

            @Override
            public JointQueryTask query(JointQueryTask jointQueryTask, List<String> list, String s, Object... objects) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Task<Report>> index(URI uri) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Report> indexBlocking(URI uri) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ServerSettingsReader getSettings() {
                throw new UnsupportedOperationException();
            }
        };

        assertNotNull(dicoogle.getStorageForSchema("file"));
    }

    @Test
    public void testLister() {
        final DicoogleFileLister lister = new DicoogleFileLister(dicoogle);
        assertEquals(
                Arrays.asList("file:/test.dcm",
                        "file:/CT/001.dcm",
                        "file:/CT/002.dcm",
                        "file:/CT/20150514/001.dcm",
                        "file:/MR/0.dcm"),
                lister.filesIn(URI.create("file:/"), 0)
                    .map(f -> f.getURI().toString())
                    .collect(Collectors.toList()));

        assertEquals(
                Arrays.asList("file:/CT/001.dcm",
                        "file:/CT/002.dcm",
                        "file:/CT/20150514/001.dcm"),
                lister.filesIn(URI.create("file:/CT"), 2)
                    .map(f -> f.getURI().toString())
                    .collect(Collectors.toList()));

        assertEquals(
                Arrays.asList("file:/CT/001.dcm",
                        "file:/CT/002.dcm"),
                lister.filesIn(URI.create("file:/CT"), 1)
                    .map(f -> f.getURI().toString())
                    .collect(Collectors.toList()));
    }
}
