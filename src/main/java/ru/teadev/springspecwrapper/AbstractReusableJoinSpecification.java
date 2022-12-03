package ru.teadev.springspecwrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import static java.text.MessageFormat.format;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

@Slf4j
abstract class AbstractReusableJoinSpecification<T> implements Specification<T> {

    protected final <F, J> Join<F, J> getOrCreateJoin(From<?, F> from, JoinInfo<? super F, J> joinInfoData) {

        final JoinType joinType = joinInfoData.getType();
        final JoinInfo.Fetching joinFetching = joinInfoData.getFetching();
        final String joinAttributeName = joinInfoData.getAttributeName();
        final String joinAlias = generateAlias(from, joinAttributeName);

        //noinspection unchecked
        Join<F, J> existedJoin = (Join<F, J>) findByAlias(from.getJoins(), joinAlias);
        //noinspection unchecked,rawtypes
        Join<F, J> existedFetch = (Join<F, J>) findByAlias((Set) from.getFetches(), joinAlias);


        Join<F, J> target;
        Join<F, J> opposite;
        BiFunction<String, JoinType, Join<F, J>> doJoin;

        switch (joinFetching) {
            case JOIN: {
                target = existedJoin;
                opposite = existedFetch;
                //noinspection Convert2MethodRef
                doJoin = (attrName, type) -> from.join(attrName, type);
                break;
            }
            case FETCH: {
                target = existedFetch;
                opposite = existedJoin;
                //noinspection unchecked
                doJoin = (attrName, type) -> (Join<F, J>) from.fetch(attrName, type);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + joinFetching);
        }


        if (opposite != null) {
            throw new IllegalArgumentException(
                    format("Forbidden to use JOIN and JOIN-FETCH at the same time! Alias = {0}", joinAlias));
        }

        if (target != null) {
            if (target.getJoinType() != joinType) {
                throw new IllegalArgumentException(
                        format("Previously added JOIN (or JOIN-FETCH) has a different type! Alias = {0}, Previously added type = {1}, New type = {2}",
                                joinAlias, target.getJoinType(), joinType));
            }

            log.debug("Reused JOIN (or JOIN-FETCH) with alias = " + joinAlias);
            return target;
        }

        Join<F, J> join = doJoin.apply(joinAttributeName, joinType);
        join.alias(joinAlias);
        return join;
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

    @Nullable
    private Join<?, ?> findByAlias(@NonNull Set<? extends Join<?, ?>> joins, @NonNull String alias) {

        List<Join<?, ?>> joinList = new ArrayList<>(joins);

        for (Join<?, ?> join : joinList) {
            if (alias.equals(join.getAlias())) {
                return join;
            }
        }

        return null;
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
