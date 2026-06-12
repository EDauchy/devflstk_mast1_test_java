package com.example;

public class BookingResult {
    private final boolean accepted;
    private final String rejectionReason;

    private BookingResult(boolean accepted, String rejectionReason) {
        this.accepted = accepted;
        this.rejectionReason = rejectionReason;
    }

    public static BookingResult accepted() {
        return new BookingResult(true, null);
    }

    public static BookingResult rejected(String reason) {
        return new BookingResult(false, reason);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
