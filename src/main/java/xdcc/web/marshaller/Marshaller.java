package xdcc.web.marshaller;

import java.util.Map;

public interface Marshaller<T> {
  Map<String, Object> marshall(T entity, Format format);
}
