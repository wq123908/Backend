package com.example.bill.parser;

import com.example.bill.exception.FileParseException;
import java.io.InputStream;

public abstract class AbstractFileParser {
    public abstract void validateHeader(InputStream is) throws FileParseException;
    
    public abstract void parseContent(InputStream is) throws FileParseException;
    
    public final void process(InputStream is) throws FileParseException {
        validateHeader(is);
        parseContent(is);
    }
}