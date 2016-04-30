package com.martabak.kamar.service.response;

import java.util.List;

/**
 * A response from a special view all request.
 */
public class AllResponse<T> {

    /**
     * The total number of rows found.
     */
    public final int total_rows;

    /**
     * The offset value.
     */
    public final int offset;

    /**
     * The list of results.
     */
    public final List<AllResult<T>> rows;

    public AllResponse() {
        this.total_rows = 0;
        this.offset = 0;
        this.rows = null;
    }

    public class AllResult<T> {

        /**
         * The ID of the result in the view.
         */
        public final String id;

        /**
         * The key of the result in the view.
         */
        public final Object key;

        /**
         * The actual document.
         */
        public final T doc;

        public AllResult() {
            this.id = null;
            this.key = null;
            this.doc = null;
        }
    }

}
