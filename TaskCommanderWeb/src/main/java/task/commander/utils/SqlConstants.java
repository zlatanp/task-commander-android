package task.commander.utils;

public class SqlConstants {

    public static final String ACTIVE = "`active` = true";

    public static final String UPDATE = "UPDATE ";

    public static final String SOFT_DELETE = " SET `active` = false, updated = now() WHERE id = ?";
}