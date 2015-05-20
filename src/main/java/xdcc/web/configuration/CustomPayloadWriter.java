package xdcc.web.configuration;

import net.codestory.http.Request;
import net.codestory.http.Response;
import net.codestory.http.compilers.CompilerFacade;
import net.codestory.http.io.Resources;
import net.codestory.http.misc.Env;
import net.codestory.http.payload.Payload;
import net.codestory.http.payload.PayloadWriter;
import net.codestory.http.templating.Site;

public class CustomPayloadWriter extends PayloadWriter {

  public CustomPayloadWriter(Request request, Response response, Env env, Site site, Resources resources, CompilerFacade compilers) {
    super(request, response, env, site, resources, compilers);
  }

  @Override
  protected Payload errorPage(int errorCode, Throwable e) {
    return errorAsJson(errorCode, e);
  }
}
