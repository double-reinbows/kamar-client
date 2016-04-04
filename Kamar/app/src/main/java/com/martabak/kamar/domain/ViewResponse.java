package com.martabak.kamar.domain;

import java.util.List;

/**
 * A response from a special view request.
 */
public class ViewResponse<T> {

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
    public final List<ViewResult<T>> rows;

    public ViewResponse() {
        this.total_rows = 0;
        this.offset = 0;
        this.rows = null;
    }

    public class ViewResult<T> {

        /**
         * The ID of the result in the view.
         */
        public final String id;

        /**
         * The key of the result in the view.
         */
        public final Object key;

        /**
         * The actual result object.
         */
        public final T value;

        public ViewResult() {
            this.id = null;
            this.key = null;
            this.value = null;
        }
    }

}
