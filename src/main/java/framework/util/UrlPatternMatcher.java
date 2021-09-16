package framework.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UrlPatternMatcher {
    private static ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();

    public boolean match(String pattern, String url) {
        if (pattern.startsWith("/"))
            pattern = pattern.substring(1);

        if(url.startsWith("/"))
            url = url.substring(1);

        String[] patternTokens = pattern.split("/");
        String[] urlTokens = url.split("/");

        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(0, 0));

        while(!queue.isEmpty()) {
            var node = queue.poll();

            if(node.url == urlTokens.length && node.pattern == patternTokens.length) {
                return true;
            } else if(node.url != urlTokens.length &&node.pattern == patternTokens.length) {
                continue;
            } else if(node.url == urlTokens.length && node.pattern != patternTokens.length) {
                if(patternTokens[node.pattern].equals("**")) {
                    queue.add(new Node(node.pattern+1, node.url));
                }
            } else {
                String p = patternTokens[node.pattern];
                String u = urlTokens[node.url];

                if(p.equals("*")) {
                    queue.add(new Node(node.pattern+1, node.url+1));
                } else if(p.equals("**")) {
                    queue.add(new Node(node.pattern+1, node.url));
                    queue.add(new Node(node.pattern+1, node.url+1));
                    queue.add(new Node(node.pattern, node.url+1));
                } else {
                    if(p.equals(u)) {
                        queue.add(new Node(node.pattern+1, node.url+1));
                    }
                }
            }
        }

        return false;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Node {
        private int pattern;
        private int url;
    }

}
