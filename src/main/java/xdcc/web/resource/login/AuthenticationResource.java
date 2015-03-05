package xdcc.web.resource.login;

import com.auth0.jwt.JWTSigner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.xdcc.api.infrastructure.persistence.mongo.MongoUserService;
import fr.xdcc.api.model.MongoUser;
import net.codestory.http.annotations.Post;
import net.codestory.http.payload.Payload;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Map;

public class AuthenticationResource {

  @Inject
  public AuthenticationResource(MongoUserService mongoUserService) {
    this.mongoUserService = mongoUserService;
  }

  @Post("/login")
  public Payload auth(MongoUser candidateUser) {
    MongoUser mongoUser = mongoUserService.findByLogin(candidateUser.getLogin());
    if ((mongoUser == null) || (!BCrypt.checkpw(candidateUser.getPassword(), mongoUser.getPassword()))) {
      LOG.info("Authentication failed: invalid login or password");
      return Payload.badRequest();
    }

    LinkedList<String> audience = Lists.newLinkedList();
    audience.add("xdcc-api");
    audience.add("xdcc-webapp-express");

    Map<String, Object> claims = Maps.newHashMap();
    claims.put("iss", "xdcc-api");
    claims.put("aud", audience);
    claims.put("exp", System.currentTimeMillis() / 1000L + 86400L); // 1-day validity

    String token = signer.sign(claims);

    String data = "{\"token\": \"" + token + "\"}";

    LOG.info("Authentication successful, sending token");
    return new Payload("application/json;charset=UTF-8", data, 200);
  }

  private final JWTSigner signer = new JWTSigner(System.getenv("JWT_SECRET"));
  private final MongoUserService mongoUserService;
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationResource.class);
}
