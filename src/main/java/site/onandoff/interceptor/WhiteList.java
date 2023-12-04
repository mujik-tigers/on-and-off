package site.onandoff.interceptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpMethod;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhiteList {

	private final Map<String, Set<String>> paths = new HashMap<>();

	public static WhiteList create() {
		return new WhiteList();
	}

	public WhiteList addPathAndMethod(String path, HttpMethod httpMethod) {
		Set<String> httpMethodsOfPath = paths.computeIfAbsent(path, key -> new HashSet<>());
		httpMethodsOfPath.add(httpMethod.name());
		return this;
	}

	public boolean contains(String path, String httpMethod) {
		return paths.containsKey(path) && paths.get(path).contains(httpMethod);
	}

}
