package xdcc.web.filter;

import com.auth0.jwt.JWTVerifier;
import net.codestory.http.Context;
import net.codestory.http.filters.Filter;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.payload.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AuthenticationFilter implements Filter {

  public AuthenticationFilter(List<String> uriPrefixes, String issuer) {
    this.uriPrefixes = uriPrefixes;
    tokenVerifier = new JWTVerifier(System.getenv("JWT_SECRET"), "xdcc:api", issuer);
  }

  @Override
  public boolean matches(String uri, Context context) {
    for (String uriPrefix : uriPrefixes) {
      if ((uri.equals(uriPrefix)) || (uri.startsWith(uriPrefix + "/"))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Payload apply(String uri, Context context, PayloadSupplier nextFilter) throws Exception {
    String authorizationHeader = context.header("Authorization");
    if (authorizationHeader == null) {
      return Payload.unauthorized("/bot");
    }

    String[] splitHeader = authorizationHeader.split(" ");
    String title = splitHeader[0];
    String token = splitHeader[1];

    if (!title.equals("Basic")) {
      return Payload.unauthorized("/bot");
    }

    try {
      tokenVerifier.verify(token);
    } catch (Exception exception) {
      LOG.info("Token verification failed: {}", exception.getMessage());
      return Payload.unauthorized("/bot");
    }

    return nextFilter.get();
  }

  private final List<String> uriPrefixes;
  private final JWTVerifier tokenVerifier;
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);
}
