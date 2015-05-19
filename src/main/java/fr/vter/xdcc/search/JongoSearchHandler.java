package fr.vter.xdcc.search;

import org.jongo.Jongo;

import javax.inject.Inject;

public abstract class JongoSearchHandler<TSearch extends Search<TResponse>, TResponse> implements SearchHandler<TSearch, TResponse> {

  public Jongo getJongo() {
    return jongo;
  }

  @Inject
  void setJongo(Jongo jongo) {
    this.jongo = jongo;
  }

  @Override
  public TResponse execute(TSearch search) {
    return execute(search, jongo);
  }

  protected abstract TResponse execute(TSearch search, Jongo jongo);

  private Jongo jongo;
}
