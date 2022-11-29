package ru.teadev.springspecwrapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Slf4j
abstract class AbstractReusableJoinSpecification<T> implements Specification<T> {

    protected final <F, J> Join<F, J> getOrCreateJoin(From<?, F> from, JoinInfo<? super F, J> joinInfoData) {

        final String joinAttributeName = joinInfoData.getAttributeName();
        final String alias = generateAlias(from, joinAttributeName);

        //noinspection unchecked
        Optional<Join<F, J>> existedJoin =
                (Optional<Join<F, J>>) (Optional<? extends Join<?, ?>>)
                        findJoin(from.getJoins(), alias);

        if (existedJoin.isPresent()) {
            log.debug("Reused join with alias = " + alias);
            return existedJoin.get();

        } else {
            Join<Object, Object> join = from.join(joinAttributeName);
            join.alias(alias);
            //noinspection unchecked
            return (Join<F, J>) join;
        }
    }

    private <F extends From<?, E>, E extends FA, FA> String generateAlias(F from, String joinAttributeName) {

        if (from instanceof Root<?>) {
            return joinAttributeName;

        } else {
            String fromAlias = from.getAlias();
            if (isBlank(fromAlias)) {
                throw new IllegalArgumentException();
            }

            return fromAlias + "_" + joinAttributeName;
        }
    }

    private Optional<Join<?, ?>> findJoin(@NonNull Set<? extends Join<?, ?>> joins, @NonNull String alias) {

        List<Join<?, ?>> joinList = new ArrayList<>(joins);

        for (Join<?, ?> join : joinList) {
            if (alias.equals(join.getAlias())) {
                return Optional.of(join);
            }
        }

        return Optional.empty();
    }

    /**
     * Copy-pasted from org.apache.commons commons-lang3 3.12.0
     */
    private static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copy-pasted from org.apache.commons commons-lang3 3.12.0
     */
    private static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

}
