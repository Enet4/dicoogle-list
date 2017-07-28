Dicoogle File Lister
====================

[![Build Status](https://travis-ci.org/Enet4/dicoogle-list.svg?branch=master)](https://travis-ci.org/Enet4/dicoogle-list)

This is a plugin for Dicoogle that provides a web service for storage listing.

Building
---------

This is a maven project. Simply run the following command:

```sh
mvn install
```

Installing
----------

Pass the jar file with dependencies, named with the pattern "list-{vvvvvv}-jar-with-dependencies.jar",
to the "Plugins" folder in Dicoogle's working directory.

Configuring
-----------

This plugin requires no configurations.

Using the Web Service API
-------------------------

### **GET** `/files/list?uri=«uri»`

Retrieve the list of files at the base URI.

Query string parameters:

  - _uri_ : the URI of the root (e.g. `file:/CT/20150101`);
  - _depth_ (optional): the maximum depth of the listed files relative to the base URI, use 0 or negative for unlimited depth;
  - _psize_ (optional): the maximum number of files to list in the output;
  - _poffset_ (optional): can be used to skip the first `poffset` files.

**Examples:**

For the following files in the `file:` store:

```txt
/test.dcm
/CT/
|- 001.dcm
|- 002.dcm
\- 20150514/
   \- 001.dcm
/MR/
\- 0.dcm
```

#### `GET /files/list?uri=file:/`

```json
{
  "uri": "file:/",
  "files": [
    "file:/test.dcm",
    "file:/CT/001.dcm",
    "file:/CT/002.dcm",
    "file:/CT/20150514/001.dcm",
    "file:/MR/0.dcm"
  ]
}
```

#### `GET /files/list?uri=file:/&depth=1`

```json
{
  "uri": "file:/",
  "files": [
    "file:/test.dcm",
  ]
}
```

#### `GET /files/list?uri=file:/CT&depth=2`

```json
{
  "uri": "file:/CT",
  "files": [
    "file:/CT/001.dcm",
    "file:/CT/002.dcm",
    "file:/CT/20150514/001.dcm"
  ]
}
```

#### `GET /files/list?uri=file:/CT&depth=1`

```json
{
  "uri": "file:/CT",
  "files": [
    "file:/CT/001.dcm",
    "file:/CT/002.dcm"
  ]
}
```

#### `GET /files/list?uri=file:/&psize=2`

```json
{
  "uri": "file:/",
  "files": [
    "file:/test.dcm",
    "file:/CT/001.dcm"
  ]
}
```

#### `GET /files/list?uri=file:/CT&poffset=1`

```json
{
  "uri": "file:/CT",
  "files": [
    "file:/CT/002.dcm",
    "file:/CT/20150514/001.dcm"
  ]
}
```
