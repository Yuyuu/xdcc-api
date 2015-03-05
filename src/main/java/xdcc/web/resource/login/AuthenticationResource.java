package xdcc.web.resource.login;

import fr.xdcc.api.infrastructure.persistence.mongo.MongoUserService;
import fr.xdcc.api.model.MongoUser;
import net.codestory.http.annotations.Post;
import net.codestory.http.payload.Payload;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AuthenticationResource {

  @Inject
  public AuthenticationResource(MongoUserService mongoUserService) {
    this.mongoUserService = mongoUserService;
  }

  @Post("/login")
  public Payload auth(MongoUser candidateUser) {
    MongoUser mongoUser = mongoUserService.findByLogin(candidateUser.getLogin());
    if (mongoUser == null || !BCrypt.checkpw(candidateUser.getPassword(), mongoUser.getPassword())) {
      return Payload.badRequest();
    }
    // TODO *** Send token
    return Payload.ok();
  }

  private final MongoUserService mongoUserService;
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationResource.class);
}
