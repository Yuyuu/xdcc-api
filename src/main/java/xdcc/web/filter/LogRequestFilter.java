package xdcc.web.filter;

import net.codestory.http.Context;
import net.codestory.http.filters.Filter;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.payload.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LogRequestFilter implements Filter {

  @Override
  public Payload apply(String uri, Context context, PayloadSupplier nextFilter) throws Exception {
    LOG.info("{} => {} {}", context.request().clientAddress(), context.method(), uri);
    return nextFilter.get();
  }

  private final static Logger LOG = LoggerFactory.getLogger(LogRequestFilter.class);
}
