package org.elearning.enums;

import java.util.EnumSet;
import java.util.Set;

public enum CategoryStatus {
    ACTIVE,
    INACTIVE,
    ARCHIVED;

    private static final Set<CategoryStatus> FROM_ACTIVE   = EnumSet.of(INACTIVE, ARCHIVED);
    private static final Set<CategoryStatus> FROM_INACTIVE = EnumSet.of(ACTIVE, ARCHIVED);
    private static final Set<CategoryStatus> FROM_ARCHIVED = EnumSet.noneOf(CategoryStatus.class);

    public boolean canTransitionTo(CategoryStatus target) {
        if (target == null) return false;
        switch (this) {
            case ACTIVE:   return FROM_ACTIVE.contains(target);
            case INACTIVE: return FROM_INACTIVE.contains(target);
            case ARCHIVED: return FROM_ARCHIVED.contains(target);
            default:       return false;
        }
    }

    public static CategoryStatus fromString(String status) {
        if (status == null) return null;
        try {
            return CategoryStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown CategoryStatus: " + status, ex);
        }
    }
}

