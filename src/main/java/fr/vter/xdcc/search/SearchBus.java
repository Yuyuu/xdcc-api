package fr.vter.xdcc.search;

import com.google.common.collect.Sets;
import fr.vter.xdcc.infrastructure.bus.AsynchronousBus;

import javax.inject.Inject;
import java.util.Set;

public class SearchBus extends AsynchronousBus {

  @Inject
  public SearchBus(Set<SearchHandler> handlers) {
    super(Sets.newHashSet(), handlers);
  }
}
