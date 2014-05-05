package xdcc.web.filter;

import net.codestory.http.filters.Filter;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.internal.Context;
import net.codestory.http.payload.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogRequestFilter implements Filter {
  private static final Logger LOG = LoggerFactory.getLogger(LogRequestFilter.class);

  @Override
  public Payload apply(String uri, Context context, PayloadSupplier nextFilter) throws IOException {
    LOG.info("{} => {}", context.getClientAddress(), uri);
    return nextFilter.get();
  }
}
