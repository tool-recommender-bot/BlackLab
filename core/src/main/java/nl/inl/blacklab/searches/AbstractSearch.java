package nl.inl.blacklab.searches;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import nl.inl.blacklab.exceptions.BlackLabRuntimeException;
import nl.inl.blacklab.exceptions.InvalidQuery;
import nl.inl.blacklab.search.results.QueryInfo;
import nl.inl.blacklab.search.results.SearchResult;

/**
 * Abstract base class for all Search implementations,
 * to enforce that equals() and hashCode are implemented
 * (to ensure proper caching)
 * 
 * @param <R> results type, e.g. Hits
 */
public abstract class AbstractSearch<R extends SearchResult> implements Search {
	
    private QueryInfo queryInfo;
    
    public AbstractSearch(QueryInfo queryInfo) {
        this.queryInfo = queryInfo;  
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<R> executeAsync() {
        return (CompletableFuture<R>)getFromCache(this, () -> {
            try {
                return executeInternal();
            } catch (InvalidQuery e) {
                throw new CompletionException(e);
            }
        });
    }
    
    @Override
    public final R execute() throws InvalidQuery {
        CompletableFuture<R> future = executeAsync();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw BlackLabRuntimeException.wrap(e);
        } catch (CompletionException e) {
            try {
                throw e.getCause();
            } catch (InvalidQuery e2) {
                throw e2;
            } catch (Throwable e2) {
                throw new AssertionError(e2);
            }
        }
    }
    
    protected abstract R executeInternal() throws InvalidQuery;
    
    protected CompletableFuture<? extends SearchResult> getFromCache(Search search, Supplier<? extends SearchResult> searchTask) {
        return queryInfo.index().cache().get(search, searchTask);
    }
    
    protected void cancelSearch(CompletableFuture<? extends SearchResult> future) {
        queryInfo.index().cache().remove(this);
        future.cancel(false);
    }
    
    @Override
    public QueryInfo queryInfo() {
        return queryInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((queryInfo == null) ? 0 : queryInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractSearch<?> other = (AbstractSearch<?>) obj;
        if (queryInfo == null) {
            if (other.queryInfo != null)
                return false;
        } else if (!queryInfo.equals(other.queryInfo))
            return false;
        return true;
    }

    protected static String toString(String operation, Object... param) {
        return operation + "(" + StringUtils.join(param, ", ") + ")";
    }
	
}