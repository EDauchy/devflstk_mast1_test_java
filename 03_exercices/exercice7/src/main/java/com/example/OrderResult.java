package com.example;

public class OrderResult {
    private final boolean accepted;
    private final String rejectionReason;
    private final OrderReceipt receipt;

    private OrderResult(boolean accepted, String rejectionReason, OrderReceipt receipt) {
        this.accepted = accepted;
        this.rejectionReason = rejectionReason;
        this.receipt = receipt;
    }

    public static OrderResult accepted(OrderReceipt receipt) {
        return new OrderResult(true, null, receipt);
    }

    public static OrderResult rejected(String reason) {
        return new OrderResult(false, reason, null);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public OrderReceipt getReceipt() {
        return receipt;
    }
}
