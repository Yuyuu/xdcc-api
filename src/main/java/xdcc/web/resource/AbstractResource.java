package xdcc.web.resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public abstract class AbstractResource {

  protected ObjectId parseObjectId(String id) {
    if (!ObjectId.isValid(id)) {
      throw new InvalidObjectIdException(id);
    }

    return new ObjectId(id);
  }

  protected Map errors(List<String> codes) {
    Map<String, String> errorData = Maps.newHashMap();
    codes.forEach(code -> errorData.put("message", code));

    List<Map<String, String>> errorList = Lists.newArrayList();
    errorList.add(errorData);

    Map<String, List<Map<String, String>>> errorMap = Maps.newHashMap();
    errorMap.put("errors", errorList);

    return errorMap;
  }
}
