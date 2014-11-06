package query

public interface Repository {
    List<Map> findAll(Map<String, Object> filterSpec)
}