package de.kaleidox.luller.model;

import org.comroid.annotations.Default;

import java.util.function.BiPredicate;

public enum StringComparison {
    @Default CaseSensitive {
        @Override
        public boolean test(String left, String right, BiPredicate<String, String> check) {
            return check.test(left, right);
        }
    }, CaseInsensitive {
        @Override
        public boolean test(String left, String right, BiPredicate<String, String> check) {
            return check.test(left.toLowerCase(), right.toLowerCase());
        }
    };

    public abstract boolean test(String left, String right, BiPredicate<String, String> check);
}
