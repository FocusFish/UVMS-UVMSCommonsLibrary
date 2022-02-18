package fish.focus.uvms.commons.date;

import java.util.Date;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.bind.adapter.JsonbAdapter;

public class JsonBDateAdapter implements JsonbAdapter<Date, JsonValue> {

    @Override
    public JsonValue adaptToJson(Date date) {
        return Json.createValue(date.getTime());
    }

    @Override
    public Date adaptFromJson(JsonValue json) {
        if (json.getValueType().equals(ValueType.NUMBER)) {
            return new Date(((JsonNumber)json).longValue());
        } else {
            return Date.from(DateUtils.stringToDate(((JsonString)json).getString()));
        }
    }
}
