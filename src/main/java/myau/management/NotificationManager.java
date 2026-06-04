package myau.management;

import myau.Myau;
import myau.module.modules.Notification;
import myau.util.ChatUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {
    private static final int MAX_ENTRIES = 32;

    public static class NotificationEntry {
        public final String message;
        public final long startMillis;
        public final long durationMillis;
        public final int color; // RGB

        public NotificationEntry(String message, long durationMillis) {
            this(message, durationMillis, 0xFFFFFF);
        }

        public NotificationEntry(String message, long durationMillis, int color) {
            this.message = message;
            this.durationMillis = durationMillis;
            this.color = color;
            this.startMillis = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return this.durationMillis >= 0 && System.currentTimeMillis() - this.startMillis >= this.durationMillis;
        }

        public long getAge() {
            return System.currentTimeMillis() - this.startMillis;
        }
    }

    private final List<NotificationEntry> entries = new ArrayList<>();

    public synchronized void add(String message) {
        this.add(message, 3000L);
    }

    public synchronized void add(String message, long durationMillis) {
        this.add(message, durationMillis, 0xFFFFFF);
    }

    public synchronized void add(String message, int color) {
        this.add(message, 3000L, color);
    }

    public synchronized void add(String message, long durationMillis, int color) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        this.cleanupExpired();
        while (this.entries.size() >= MAX_ENTRIES) {
            this.entries.remove(0);
        }

        Notification notification = Myau.moduleManager == null ? null : (Notification) Myau.moduleManager.modules.get(Notification.class);
        if (notification != null) {
            if (!notification.isEnabled()) {
                return;
            }
            if (notification.chat.getValue()) {
                ChatUtil.sendFormatted("&7[&bNotification&7]&r " + message);
                return;
            }
        }

        this.entries.add(new NotificationEntry(message, durationMillis, color));
    }

    public synchronized List<NotificationEntry> getActive() {
        // cleanup expired entries and return a copy of active entries (newest last)
        this.cleanupExpired();
        return new ArrayList<>(this.entries);
    }

    private void cleanupExpired() {
        Iterator<NotificationEntry> it = this.entries.iterator();
        while (it.hasNext()) {
            if (it.next().isExpired()) {
                it.remove();
            }
        }
    }

    public synchronized void clear() {
        this.entries.clear();
    }
}
