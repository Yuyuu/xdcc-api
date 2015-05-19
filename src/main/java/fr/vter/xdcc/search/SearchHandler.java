package fr.vter.xdcc.search;

import fr.vter.xdcc.infrastructure.bus.MessageHandler;

public interface SearchHandler<TSearch extends Search<TResponse>, TResponse> extends MessageHandler<TSearch, TResponse> {
}
