package com.ssauuuuuu.backend.parser;

import com.ssauuuuuu.backend.dto.AlipayBillDTO;
import com.ssauuuuuu.backend.exception.FileParseException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractFileParser {
    public abstract void validateHeader(InputStream is) throws FileParseException;
    
    public abstract  List<AlipayBillDTO>  parseContent(InputStream is) throws FileParseException;

}