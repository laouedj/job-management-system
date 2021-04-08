package org.prototype.study.job;

import java.time.LocalDateTime;
import java.util.Date;

public enum DataType implements AbstractDataType{

    LONG,
    DOUBLE,
    STRING,
    DATE {
        @Override
        public LocalDateTime getValue(Object input) {
            return LocalDateTime.parse((String)input);
        }
    };

    @Override
    public Object getValue(Object input) {
        return input;
    }
}
