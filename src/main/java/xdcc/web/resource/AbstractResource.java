package xdcc.web.resource;

import net.codestory.http.Context;
import org.bson.types.ObjectId;

public abstract class AbstractResource {

  protected ObjectId parseObjectId(String id) {
    if (!ObjectId.isValid(id)) {
      throw new InvalidObjectIdException(id);
    }

    return new ObjectId(id);
  }

  protected void addAccessControlHeader(Context context) {
    context.response().setHeader("Access-Control-Allow-Origin", "http://xdcc-webapp.herokuapp.com");
  }
}
