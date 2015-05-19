package fr.vter.xdcc.search;

import fr.vter.xdcc.infrastructure.bus.Message;

public class Search<TResponse> implements Message<TResponse> {

  public Search<TResponse> withOffset(int offset) {
    this.offset = offset;
    return this;
  }

  public Search<TResponse> withMaxNumberOfResults(int max) {
    this.max = max;
    return this;
  }

  private int offset;
  private int max;
}
