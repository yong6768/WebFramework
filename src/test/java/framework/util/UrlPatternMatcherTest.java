package framework.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UrlPatternMatcherTest {

    @Test
    void tmp() {
        UrlPatternMatcher urlPatternMatcher = new UrlPatternMatcher();

        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**", "/a/b/c/d/e/f")).isTrue();




    }

    @Test
    void testUrlPattern() {
        UrlPatternMatcher urlPatternMatcher = new UrlPatternMatcher();

        assertThat(urlPatternMatcher.match("/**", "/asdf/hfo/fdasubfod")).isTrue();
        assertThat(urlPatternMatcher.match("/**/fdasubfod", "/asdf/hfo/fdasubfod")).isTrue();
        assertThat(urlPatternMatcher.match("/a/b/c/d/e", "/a/b/c/d/e")).isTrue();
        assertThat(urlPatternMatcher.match("/*/*/*/d/e", "/a/b/c/d/e")).isTrue();
        assertThat(urlPatternMatcher.match("/a/**/e", "/a/b/c/d/e")).isTrue();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f", "/a/b/c/d/e/f")).isTrue();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**", "/a/b/c/d/e/f")).isTrue();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**/**", "/a/b/c/d/e/f")).isTrue();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**/**/*", "/a/b/c/d/e/f/c")).isTrue();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**", "/a/b/c/d/e/f/a/gv/as/d")).isTrue();

        assertThat(urlPatternMatcher.match("/**/hfo", "/asdf/hfo/fdasubfod")).isFalse();
        assertThat(urlPatternMatcher.match("/*", "/asdf/hfo/fdasubfod")).isFalse();
        assertThat(urlPatternMatcher.match("/a/b/c/d/e", "/a/b/c/d/f")).isFalse();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/*", "/a/b/c/d/e/f")).isFalse();
        assertThat(urlPatternMatcher.match("/", "/a")).isFalse();
        assertThat(urlPatternMatcher.match("/*/b/*/d/*/f/**/**/*", "/a/b/c/d/e/f")).isFalse();

    }

}