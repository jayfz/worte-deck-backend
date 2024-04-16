package co.harborbytes.wortedeck.practicesession.dtos;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.text.DecimalFormat;

@Getter
@Builder
public class PracticeSessionStatsDTO {
    private int totalCount = 0;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private double averageScore = 0;
    private long secondsSpent = 0;
    private VocabularyLevel vocabularyLevel;
    private long leftSwipeCount = 0;
    private long rightSwipeCount = 0;


    public enum VocabularyLevel {
        A1,
        A2,
        B1,
        B2,
        C1,
        C2
    }

    static class CustomDoubleSerializer extends StdSerializer<Double> {

        private static final long serialVersionUID = 1L;
        private static final DecimalFormat df = new DecimalFormat("#.##");
        public CustomDoubleSerializer() {
            this(null);
        }
        public CustomDoubleSerializer(Class<Double> t) {
            super(t);
        }
        @Override
        public void serialize(Double value,
                              JsonGenerator generator, SerializerProvider arg2) throws IOException {

            generator.writeString(df.format(value));

//            String doubleVal = value.toString();
//            if(doubleVal.length() <= 3)
//                generator.writeString(doubleVal);
//            else
//                generator.writeString(doubleVal.substring(0, 3));
        }
    }
}
