package com.martabak.kamar.domain;

/**
 * A user type to hold user-type and user-subtype strings.
 */
public class User {

    /**
     * The GUEST user type.
     */
    public static final String TYPE_GUEST = "GUEST";

    /**
     * The STAFF user type.
     */
    public static final String TYPE_STAFF = "STAFF";

    /**
     * The RESTAURANT STAFF user sub-type.
     */
    public static final String TYPE_STAFF_RESTAURANT = "RESTAURANT";

    /**
     * The FRONTDESK STAFF user sub-type.
     */
    public static final String TYPE_STAFF_FRONTDESK = "FRONTDESK";

    /**
     * The HOUSEKEEPING STAFF user sub-type.
     */
    public static final String TYPE_STAFF_HOUSEKEEPING = "HOUSEKEEPING";

    /**
     * The ADMIN STAFF user sub-type.
     */
    public static final String TYPE_STAFF_ADMIN = "TABLETADMIN";

    /**
     * The GUEST user password.
     */
    public static final String PASSWORD_GUEST = "guest123";

    /**
     * The FRONTDESK staff user password.
     */
    public static final String PASSWORD_FRONTDESK = "frontdesk123";

    /**
     * The RESTAURANT staff user password.
     */
    public static final String PASSWORD_RESTAURANT = "restaurant123";

    /**
     * The HOUSEKEEPING staff user password.
     */
    public static final String PASSWORD_HOUSEKEEPING = "housekeeping123";

    /**
     * The ADMIN staff user password.
     */
    public static final String PASSWORD_ADMIN = "tabletadmin123";

}
