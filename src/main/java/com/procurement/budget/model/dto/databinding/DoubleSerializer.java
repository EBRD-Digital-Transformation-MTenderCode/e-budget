package com.procurement.budget.model.dto.databinding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;

public class DoubleSerializer extends StdSerializer<Double> {

    private static final String FORMATTER = ".##";

    public DoubleSerializer() {
        super(Double.class);
    }

    @Override
    public void serialize(final Double value,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        final DecimalFormat decimalFormatter = new DecimalFormat(FORMATTER);
        jsonGenerator.writeNumber(decimalFormatter.format(value));
    }

    @Override
    public Class<Double> handledType() {
        return Double.class;
    }
}
