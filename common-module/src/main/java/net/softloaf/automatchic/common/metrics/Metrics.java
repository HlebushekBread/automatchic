package net.softloaf.automatchic.common.metrics;

public final class Metrics {
    private Metrics() {}

    public static final String USERS_REGISTERED_TOTAL = "app.users.registered.total";
    public static final String USERS_DELETED_TOTAL = "app.users.deleted.total";
    public static final String USERS_CLEANED_TOTAL = "app.users.cleaned.total";
    public static final String USERS_CONFIRMED_TOTAL = "app.users.confirmed.total";

    public static final String USERS_UNCONFIRMED_CURRENT = "app.users.unconfirmed.current";
    public static final String USERS_CONFIRMED_CURRENT = "app.users.confirmed.current";
    public static final String SUBJECTS_CURRENT = "app.subjects.current";
    public static final String TASKS_CURRENT = "app.tasks.current";
    public static final String LINKS_CURRENT = "app.links.current";

    public static final String METHOD_TAG = "method";
    public static final String POST_TAG_VAL = "post";
    public static final String GET_TAG_VAL = "get";
}
