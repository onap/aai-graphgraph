package org.onap.aai.graphgraph.reader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemaRepository {
    private List<SchemaReader> readers;

    public SchemaRepository(List<SchemaReader> readers) {
        this.readers = readers;
    }

    public List<String> getAllSchemaNames(){
        return readers.stream().map(SchemaReader::getSchemaName).collect(Collectors.toList());
    }

    public SchemaReader getSchemaReader(String schemaName){
        Optional<SchemaReader> reader = readers.stream().filter(r -> schemaName.equals(r.getSchemaName())).findFirst();
        if(!reader.isPresent())
            throw new IllegalArgumentException("Schema " + schemaName + " not found");

        return reader.get();
    }
}
